package my.edu.tarc.foodorderingapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PaymentHistory extends AppCompatActivity {
    private Object date;
    private String stringFormatDate;
    private SimpleDateFormat dateFormat;
    private double amountPaid;
    private double change;
    private double grandTotal;
    private int orderID;
    private String paymentDate;
    private String paymentTime;
    private int paymentID;
    private String staffID;
    private String[] part;
    private PaymentHistoryClass data;
    private ArrayList<PaymentHistoryClass> arrayList;
    private PaymentHistoryAdapter adapter;
    private ListView listView;

    private TextView tvAmountPaid;
    private TextView tvChange;
    private TextView tvGrandTotal;
    private TextView tvOrderID;
    private TextView tvPaymentDate;
    private TextView tvPaymentTime;
    private TextView tvPaymentID;
    private TextView tvStaffID;

    private String strAmountPaid;
    private String strChange;
    private String strGrandTotal;
    private String strOrderID;
    private String strPaymentDate;
    private String strPaymentTime;
    private String strPaymentID;
    private String strStaffID;

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_history);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        dateFormat = new SimpleDateFormat("dd-MM-yyyy,HH:mm:ss");
        listView = (ListView)findViewById(R.id.paymentList);
        arrayList = new ArrayList<>();
        progressDialog = new ProgressDialog(PaymentHistory.this);
        progressDialog.setMessage("Retrieving Data");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

/*
//add data to firestore
        Map<String,Object> item = new HashMap<>();
        item.put("amountPaid",160);
        item.put("change",10);
        item.put("grandTotal",150);
        item.put("orderID",100);
        item.put("paymentDate",FieldValue.serverTimestamp());
        item.put("paymentID",100);
        item.put("staffID","S0001");

        db.collection("test").add(item)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_LONG).show();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Fail",Toast.LENGTH_LONG).show();
                    }
                });
*/


        progressDialog.show();
        //Load data from firestore
        db.collection("Payment").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document:task.getResult()) {

                        date = (Date) document.getData().get("paymentDate");
                        amountPaid = Double.parseDouble(document.getData().get("amountPaid").toString());
                        change = Double.parseDouble(document.getData().get("change").toString());
                        grandTotal = Double.parseDouble(document.getData().get("grandTotal").toString());
                        orderID = Integer.parseInt(document.getData().get("orderID").toString());
                        paymentID = Integer.parseInt(document.getData().get("paymentID").toString());
                        staffID = document.getData().get("staffID").toString();

                        stringFormatDate = dateFormat.format(date);
                        part = stringFormatDate.split(",");
                        paymentDate = part[0];
                        paymentTime = part[1];

                        data = new PaymentHistoryClass(amountPaid, change, grandTotal, orderID, paymentDate, paymentTime, paymentID, staffID);
                        arrayList.add(data);

                    }
                        //list view
                        adapter = new PaymentHistoryAdapter(getApplicationContext(),arrayList);
                        listView.setAdapter(adapter);
                        progressDialog.dismiss();

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                tvAmountPaid = (TextView)view.findViewById(R.id.amountPaidTextDetail);
                                strAmountPaid = tvAmountPaid.getText().toString();
                                tvChange = (TextView)view.findViewById(R.id.changeText);
                                strChange = tvChange.getText().toString();
                                tvGrandTotal = (TextView)view.findViewById(R.id.grandTotalTextDetail);
                                strGrandTotal = tvGrandTotal.getText().toString();
                                tvOrderID = (TextView)view.findViewById(R.id.orderIDTextDetail);
                                strOrderID = tvOrderID.getText().toString();
                                tvPaymentDate = (TextView)view.findViewById(R.id.paymentDateText);
                                strPaymentDate = tvPaymentDate.getText().toString();
                                tvPaymentTime = (TextView)view.findViewById(R.id.paymentTimeText);
                                strPaymentTime = tvPaymentTime.getText().toString();
                                tvPaymentID = (TextView)view.findViewById(R.id.paymentIDTextDetail);
                                strPaymentID = tvPaymentID.getText().toString();
                                tvStaffID = (TextView)view.findViewById(R.id.staffIDTextDetail);
                                strStaffID = tvStaffID.getText().toString();

                                Intent intent = new Intent(PaymentHistory.this,PaymentHistoryDetail.class);
                                intent.putExtra("amt",strAmountPaid);
                                intent.putExtra("chg",strChange);
                                intent.putExtra("gt",strGrandTotal);
                                intent.putExtra("oid",strOrderID);
                                intent.putExtra("paydate",strPaymentDate);
                                intent.putExtra("paytime",strPaymentTime);
                                intent.putExtra("payid",strPaymentID);
                                intent.putExtra("sid",strStaffID);

                                startActivity(intent);
                            }
                        });
                }
                else{
                    Toast.makeText(getApplicationContext(),"Error in retrieving data",Toast.LENGTH_LONG).show();
                }
            }
        });







    }
}
