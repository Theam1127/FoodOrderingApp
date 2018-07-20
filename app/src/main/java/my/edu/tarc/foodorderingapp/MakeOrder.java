package my.edu.tarc.foodorderingapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MakeOrder extends AppCompatActivity {
    Menu menuItem;
    List<Menu> orders;
    TextView tableNo, totalPrice;
    ListView orderList;
    OrderListAdapter adapter;
    ProgressDialog pd;
    int tableNumber, orderID;
    Button addItemBtn, makePaymentBtn;
    List<Integer> orderedItemsID;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_order);
        addItemBtn = (Button) findViewById(R.id.addItemBtn);
        makePaymentBtn = (Button) findViewById(R.id.makePaymentBtn);
        orders = new ArrayList<>();
        menuItem = new Menu();
        orderedItemsID = new ArrayList<>();
        tableNumber = 1;
        pd = new ProgressDialog(this);
        pd.setMessage("Please Wait...");
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        db = FirebaseFirestore.getInstance();
        db.collection("PlacedOrder").whereEqualTo("tableNumber", tableNumber).whereEqualTo("paid", false).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot documentSnapshot : task.getResult())
                        orderID = Integer.parseInt(documentSnapshot.getData().get("orderID").toString());
                    db.collection("OrderDetail").whereEqualTo("orderID", orderID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot documentSnapshot : task.getResult())
                                    orderedItemsID.add(Integer.parseInt(documentSnapshot.getData().get("menuID").toString()));
                                db.collection("Menu").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                            for (int id : orderedItemsID) {
                                                if (Integer.parseInt(documentSnapshot.getData().get("menuID").toString()) == id) {
                                                    boolean available = (boolean) documentSnapshot.getData().get("menuStatus");
                                                    String name = "" + documentSnapshot.getData().get("menuName");
                                                    double price = Double.parseDouble(documentSnapshot.getData().get("menuPrice").toString());
                                                    String type = "" + documentSnapshot.getData().get("menuType");
                                                    Menu item = new Menu(id, name, type, available, price);
                                                    orders.add(item);
                                                }
                                            }
                                        }

                                        adapter = new OrderListAdapter(orders, getApplicationContext());
                                        orderList.setAdapter(adapter);
                                        totalPrice = findViewById(R.id.totalPriceTV);
                                        double total = 0.00;
                                        for (int a = 0; a < orders.size(); a++)
                                            total += orders.get(a).getPrice();
                                        totalPrice.setText("RM " + String.format("%.2f", total));
                                        pd.dismiss();
                                    }
                                });

                            }
                        }
                    });
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

        tableNo = (TextView) findViewById(R.id.tableNo);
        orderList = (ListView) findViewById(R.id.orderListLV);
        tableNo.setText("" + tableNumber);
    }

}