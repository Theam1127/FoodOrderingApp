package my.edu.tarc.foodorderingapp;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MakeOrder extends AppCompatActivity {
    Menu menuItem = new Menu();
    List<Menu> orders = new ArrayList<>();
    List<String> menuList = new ArrayList<>();
    TextView tableNo, totalPrice;
    ListView orderList;
    ArrayAdapter<String> adapter;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_order);
        pd = new ProgressDialog(this);
        pd.setMessage("Please Wait...");
        pd.show();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Menu").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        menuItem.setMenuID((String)document.getData().get("menuID"));
                        menuItem.setAvailable((boolean)document.getData().get("available"));
                        menuItem.setName((String)document.getData().get("name"));
                        menuItem.setPrice((double)document.getData().get("price"));
                        menuItem.setType((String)document.getData().get("type"));
                        orders.add(menuItem);
                        menuList.add(menuItem.getName()+"\t5\t"+menuItem.getPrice());
                    }
                    adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,menuList);
                    orderList.setAdapter(adapter);
                    totalPrice = findViewById(R.id.totalPriceTV);
                    double total = 0.00;
                    for(int a=0;a<orders.size();a++)
                        total+=orders.get(a).getPrice();
                    totalPrice.setText("RM "+String.format("%.2f",total));
                    pd.dismiss();
                }
            }
        });

        tableNo = (TextView)findViewById(R.id.tableNo);
        orderList = (ListView)findViewById(R.id.orderListLV);
        tableNo.setText("1");
    }
}
