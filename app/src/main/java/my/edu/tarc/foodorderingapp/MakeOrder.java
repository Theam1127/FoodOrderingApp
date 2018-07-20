package my.edu.tarc.foodorderingapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MakeOrder extends AppCompatActivity {
    Menu menuItem;
    List<Menu> orders;
    TextView tableNo, totalPrice;
    ListView orderList;
    OrderListAdapter adapter;
    ProgressDialog pd;
    Button addItemBtn, makePaymentBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_order);
        addItemBtn = (Button)findViewById(R.id.addItemBtn);
        makePaymentBtn = (Button)findViewById(R.id.makePaymentBtn);
        orders = new ArrayList<>();
        menuItem = new Menu();
        pd = new ProgressDialog(this);
        pd.setMessage("Please Wait...");
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Menu").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        String documentID = ""+documentSnapshot.getId();
                        String menuID = ""+documentSnapshot.getData().get("menuID");
                        boolean available = (boolean)documentSnapshot.getData().get("menuStatus");
                        String name = ""+documentSnapshot.getData().get("menuName");
                        double price = Double.parseDouble(""+documentSnapshot.getData().get("menuPrice"));
                        String type = ""+documentSnapshot.getData().get("menuType");
                        menuItem=new Menu(documentID, menuID, name,type,available,price);
                        orders.add(menuItem);
                    }
                        adapter = new OrderListAdapter(orders, getApplicationContext());
                        orderList.setAdapter(adapter);
                        totalPrice = findViewById(R.id.totalPriceTV);
                        double total = 0.00;
                        for(int a=0;a<orders.size();a++)
                            total+=orders.get(a).getPrice();
                        totalPrice.setText("RM "+String.format("%.2f",total));
                        pd.dismiss();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT);
                }
            }
        });
        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddOrderItem.class);
                startActivity(intent);
            }
        });

        tableNo = (TextView)findViewById(R.id.tableNo);
        orderList = (ListView)findViewById(R.id.orderListLV);
        tableNo.setText("1");
    }
}
