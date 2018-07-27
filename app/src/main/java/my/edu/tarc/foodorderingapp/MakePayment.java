package my.edu.tarc.foodorderingapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MakePayment extends AppCompatActivity {
    TextView tvTotal, tvChanges, tvStatus, tvTableNo;
    EditText etAmountPaid;
    Button btnPay, btnBack;
    int nextID;
    double paidAmount, changes;
    FirebaseFirestore db;
    String staffID;
    SharedPreferences share;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_payment);
        final double total = getIntent().getDoubleExtra("totalAmount", 0.0);
        final int orderID = getIntent().getIntExtra("orderID", 0);
        final int tableNo = getIntent().getIntExtra("tableNumber", 0);
        tvTableNo = findViewById(R.id.tvtblNo);
        tvTotal = findViewById(R.id.tvTotalAmount);
        tvChanges = findViewById(R.id.tvChanges);
        tvStatus = findViewById(R.id.tvPayStatus);
        etAmountPaid = findViewById(R.id.editTextAmountPaid);
        btnPay = findViewById(R.id.buttonPay);
        btnBack = findViewById(R.id.btnBack);
        share = getSharedPreferences("staffID", Context.MODE_PRIVATE);
        staffID = share.getString("id",null);
        tvStatus.setText("");
        tvTableNo.setText(""+tableNo);
        tvTotal.setText(String.format("RM %.2f", total));
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paidAmount = etAmountPaid.getText().toString().isEmpty()? 0 : Double.parseDouble(etAmountPaid.getText().toString());
                if(paidAmount == 0){
                    etAmountPaid.setError("Please enter amount!");
                }
                else{
                    changes = paidAmount-total;
                    if(changes<0)
                        etAmountPaid.setError("Insufficient Amount!");
                    else {
                        tvChanges.setText(String.format("RM %.2f", changes));
                        tvStatus.setText("Paying...");
                        btnPay.setClickable(false);
                        btnBack.setClickable(false);
                        btnPay.setBackgroundColor(0);
                        btnBack.setBackgroundColor(0);
                        db = FirebaseFirestore.getInstance();
                        nextID = 1;
                        db.collection("Payment").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    for(QueryDocumentSnapshot doc : task.getResult())
                                        nextID++;
                                    Map<String,Object> newPayment = new HashMap<>();
                                    newPayment.put("amountPaid", paidAmount);
                                    newPayment.put("change", changes);
                                    newPayment.put("grandTotal", total);
                                    newPayment.put("orderID", orderID);
                                    newPayment.put("paymentDate", FieldValue.serverTimestamp());
                                    newPayment.put("paymentID", nextID);
                                    newPayment.put("staffID", staffID);
                                    db.collection("Payment").add(newPayment).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                            if(task.isSuccessful()) {
                                                db.collection("PlacedOrder").whereEqualTo("orderID", orderID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        String id = "";
                                                        Map<String, Object> update = new HashMap<>();
                                                        if (task.isSuccessful()) {
                                                            for (QueryDocumentSnapshot doc : task.getResult())
                                                                id = doc.getId();
                                                            update.put("paid", true);
                                                            db.collection("PlacedOrder").document(id).update(update).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    tvStatus.setText("Paid!");
                                                                    btnBack.setClickable(true);
                                                                    btnBack.setBackgroundColor(Color.LTGRAY);
                                                                }
                                                            });
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    });

                                }
                            }
                        });
                    }
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(tvStatus.getText().equals("") || tvStatus.getText().equals("Paid!"))
            super.onBackPressed();
    }
}
