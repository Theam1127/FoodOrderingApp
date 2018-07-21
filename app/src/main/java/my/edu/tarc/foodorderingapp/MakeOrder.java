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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MakeOrder extends AppCompatActivity {
    Menu menuItem;
    List<String> menus;
    TextView tableNo, totalPrice;
    ListView orderList;
    OrderListAdapter adapter;
    ProgressDialog pd;
    int tableNumber, orderID;
    Button addItemBtn, makePaymentBtn;
    List<Integer> orderedItemsID;
    FirebaseFirestore db;
    List<Orders> orders;
    Orders o;
    double total;
    int nextID = 1;
    static final int ADD_ORDER_ITEM = 102;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_order);
        addItemBtn = (Button) findViewById(R.id.addItemBtn);
        makePaymentBtn = (Button) findViewById(R.id.makePaymentBtn);
        tableNumber = 1;
        pd = new ProgressDialog(this);
        pd.setMessage("Please Wait...");
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        db = FirebaseFirestore.getInstance();
        loadData();
        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddOrderItem.class);
                startActivityForResult(intent, ADD_ORDER_ITEM);
            }
        });

        makePaymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (total != 0) {
                    Intent intent = new Intent(getApplicationContext(), MakePayment.class);
                    intent.putExtra("totalAmount", total);
                    intent.putExtra("orderID", orderID);
                    intent.putExtra("tableNumber", tableNumber);
                    startActivity(intent);
                }
            }
        });


        tableNo = (TextView) findViewById(R.id.tableNo);
        orderList = (ListView) findViewById(R.id.orderListLV);
        tableNo.setText("" + tableNumber);
    }




    public void loadData(){
        orderID = 0;
        menus = new ArrayList<>();
        orders = new ArrayList<>();
        menuItem = new Menu();
        orderedItemsID = new ArrayList<>();
        db.collection("PlacedOrder").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    nextID=1;
                    for(QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        nextID++;
                        if(Integer.parseInt(documentSnapshot.getData().get("tableNumber").toString())==tableNumber && !(boolean)documentSnapshot.getData().get("paid"))
                            orderID = Integer.parseInt(documentSnapshot.getData().get("orderID").toString());
                    }
                        db.collection("OrderDetail").orderBy("insertDate", Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                        if (Integer.parseInt(documentSnapshot.getData().get("orderID").toString()) == orderID) {
                                            int menuID = Integer.parseInt(documentSnapshot.getData().get("menuID").toString());
                                            int quantity = Integer.parseInt(documentSnapshot.getData().get("quantity").toString());
                                            double total = Double.parseDouble(documentSnapshot.getData().get("subtotal").toString());
                                            o = new Orders(menuID, quantity, total);
                                            orders.add(o);
                                        }
                                    }
                                    db.collection("Menu").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (Orders order : orders)
                                                    for (QueryDocumentSnapshot documentSnapshot : task.getResult())
                                                        if (order.getMenuID() == Integer.parseInt(documentSnapshot.getData().get("menuID").toString()))
                                                            menus.add(documentSnapshot.getData().get("menuName").toString());
                                                adapter = new OrderListAdapter(menus, orders, getApplicationContext());
                                                orderList.setAdapter(adapter);
                                                totalPrice = findViewById(R.id.totalPriceTV);
                                                total=0.0;
                                                for (int a = 0; a < orders.size(); a++)
                                                    total += orders.get(a).getTotal();
                                                totalPrice.setText("RM " + String.format("%.2f", total));
                                                pd.dismiss();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==ADD_ORDER_ITEM && resultCode!=RESULT_CANCELED){
            pd.show();
            Orders order = (Orders)data.getSerializableExtra("addOrder");
            Map<String, Object> newOrder = new HashMap<>();
            Map<String, Object> placeOrder = new HashMap<>();
            if(orderID==0) {
                orderID = nextID;
                placeOrder.put("orderDate", FieldValue.serverTimestamp());
                placeOrder.put("orderID", orderID);
                placeOrder.put("paid", false);
                placeOrder.put("tableNumber", tableNumber);
                db.collection("PlacedOrder").add(placeOrder);
            }
            newOrder.put("menuID", order.getMenuID());
            newOrder.put("orderID", orderID);
            newOrder.put("quantity", order.getQuantity());
            newOrder.put("subtotal", order.getTotal());
            newOrder.put("insertDate", FieldValue.serverTimestamp());
            db.collection("OrderDetail").add(newOrder).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(getApplicationContext(),"Order Added.",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        pd.show();
        loadData();
    }
}