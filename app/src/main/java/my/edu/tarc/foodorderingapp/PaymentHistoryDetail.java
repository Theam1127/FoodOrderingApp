package my.edu.tarc.foodorderingapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class PaymentHistoryDetail extends AppCompatActivity {

    private TextView tvAmountPaid;
    private TextView tvChange;
    private TextView tvGrandTotal;
    private TextView tvOrderID;
    private TextView tvPaymentDate;
    private TextView tvPaymentTime;
    private TextView tvPaymentID;
    private TextView tvStaffID;
    private TextView tvOrderHistory;

    private FirebaseFirestore db;
    private int menudID;
    private ArrayList<Integer> menuidList,quantityList;
    private ArrayList<String>orderHistoryList,menuName;
    private ArrayList<String>sortHistoryList;
    private ListView historyList;
    private ListAdapter adapter;
    int numOrderID;
    int quantity;
    Set<String> set;
    private ProgressDialog progressDialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_history_detail);

        db = FirebaseFirestore.getInstance();
        menuidList = new ArrayList<Integer>();
        quantityList = new ArrayList<Integer>();
        menuName = new ArrayList<String>();
        orderHistoryList = new ArrayList<String>();

        progressDialog = new ProgressDialog(PaymentHistoryDetail.this);
        progressDialog.setMessage("Retrieving Data");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);



        tvAmountPaid = (TextView)findViewById(R.id.amountPaidTextDetail);
        tvChange = (TextView)findViewById(R.id.changeTextDetail);
        tvGrandTotal = (TextView)findViewById(R.id.grandTotalTextDetail);
        tvOrderID = (TextView)findViewById(R.id.orderIDTextDetail);
        tvPaymentDate = (TextView)findViewById(R.id.payDateDetail);
        tvPaymentTime = (TextView)findViewById(R.id.payTimeDetail);
        tvPaymentID = (TextView)findViewById(R.id.paymentIDTextDetail);
        tvStaffID = (TextView)findViewById(R.id.staffIDTextDetail);
        historyList = (ListView)findViewById(R.id.orderHistoryListView);



        Intent intent = getIntent();

        String amtPaid = intent.getStringExtra("amt");
        String change = intent.getStringExtra("chg");
        String grandTotal = intent.getStringExtra("gt");
        String orderID = intent.getStringExtra("oid");
        String payDate = intent.getStringExtra("paydate");
        String payTime = intent.getStringExtra("paytime");
        String payID = intent.getStringExtra("payid");
        String staffID = intent.getStringExtra("sid");

        Double damtPaid = Double.parseDouble(amtPaid);
        Double dchange = Double.parseDouble(change);
        Double dgrandTotal = Double.parseDouble(grandTotal);

        numOrderID = Integer.parseInt(orderID);


        progressDialog.show();
        db.collection("OrderDetail")
                .whereEqualTo("orderID",numOrderID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document:task.getResult()){
                        menudID = Integer.parseInt(document.getData().get("menuID").toString());
                        menuidList.add(menudID);
                        quantity = Integer.parseInt(document.getData().get("quantity").toString());
                        quantityList.add(quantity);
                    }
                    for (int i=0;i<menuidList.size();i++){
                        db.collection("Menu")
                                .whereEqualTo("menuID", menuidList.get(i))
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if(task.isSuccessful()){
                                            for(QueryDocumentSnapshot document:task.getResult()){
                                                menuName.add(document.getData().get("menuName").toString());
                                                for(int j=0;j<menuName.size();j++)
                                                    orderHistoryList.add(String.format("%s\t\tqty:%d",menuName.get(j),quantityList.get(j)));
                                            }
                                            set = new LinkedHashSet<>(orderHistoryList);
                                            sortHistoryList = new ArrayList<>(set);
                                            adapter = new ArrayAdapter<>(PaymentHistoryDetail.this,android.R.layout.simple_list_item_1,sortHistoryList);
                                            historyList.setAdapter(adapter);
                                            progressDialog.dismiss();
                                        }else {
                                            Toast.makeText(getApplicationContext(),"Error in retrieving data",Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                    }


                }else {
                    Toast.makeText(getApplicationContext(),"Error in retrieving data",Toast.LENGTH_LONG).show();
                }
            }
        });

/*
        for(int j=0;j<menuName.size();j++){
            orderHistory = String.format("%s\t\tqty:%d",menuName.get(j),quantityList.get(j));
            orderHistoryList.add(menuName.get(j));
        }
        tvOrderHistory.setText(orderHistoryList.toString());
        */

        tvAmountPaid.setText(String.format("RM %.2f",damtPaid));
        tvChange.setText(String.format("RM %.2f",dchange));
        tvGrandTotal.setText(String.format("RM %.2f",dgrandTotal));
        tvOrderID.setText(orderID);
        tvPaymentDate.setText(payDate);
        tvPaymentTime.setText(payTime);
        tvPaymentID.setText(payID);
        tvStaffID.setText(staffID);


    }
    public void cancel(View view){
        finish();
    }
}
