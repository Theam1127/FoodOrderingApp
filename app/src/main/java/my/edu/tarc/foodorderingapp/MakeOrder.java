package my.edu.tarc.foodorderingapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class MakeOrder extends AppCompatActivity {
    Menu menuItem;
    List<String> menus;
    TextView tableNo, totalPrice;
    ListView orderList;
    OrderListAdapter adapter;
    ProgressDialog pd;
    int tableNumber, orderID;
    Button addItemBtn, makePaymentBtn, clearOrder;
    FirebaseFirestore db;
    List<Orders> orders;
    Orders o;
    double total;
    int nextID = 1;
    static final int ADD_ORDER_ITEM = 102, EDIT_ORDER_ITEM = 103;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_order);
        addItemBtn = (Button) findViewById(R.id.addItemBtn);
        makePaymentBtn = (Button) findViewById(R.id.makePaymentBtn);
        tableNo = (TextView) findViewById(R.id.tableNo);
        orderList = (ListView) findViewById(R.id.orderListLV);
        tableNumber = getIntent().getIntExtra("table", 0);
        pd = new ProgressDialog(MakeOrder.this);
        pd.setMessage("Please Wait...");
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
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
        tableNo.setText("" + tableNumber);
        orderList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(),ConfirmAddOrderItem.class);
                intent.putExtra("editOrderedItem", orders.get(i));
                intent.putExtra("editOrderItemName", menus.get(i));
                startActivityForResult(intent, EDIT_ORDER_ITEM);
            }
        });
        clearOrder = findViewById(R.id.btnClear);
        clearOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(MakeOrder.this);
                if(!orders.isEmpty()) {
                    alert.setTitle("Are you sure?").setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            db.collection("OrderDetail").whereEqualTo("orderID", orderID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                            db.collection("OrderDetail").document(documentSnapshot.getId()).delete();
                                        }
                                        db.collection("PlacedOrder").whereEqualTo("orderID", orderID).whereEqualTo("paid", false).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful())
                                                    for (QueryDocumentSnapshot doc : task.getResult())
                                                        db.collection("PlacedOrder").document(doc.getId()).delete();
                                                finish();
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    });
                    alert.show();
                }
            }
        });
    }





    public void loadData() {
        menus = new ArrayList<>();
        orders = new ArrayList<>();
        menuItem = new Menu();
        pd.show();
        db.collection("PlacedOrder").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                orderID = 0;
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    if (Integer.parseInt(documentSnapshot.get("tableNumber").toString()) == tableNumber && !documentSnapshot.getBoolean("paid"))
                        orderID = Integer.parseInt(documentSnapshot.get("orderID").toString());
                }
                db.collection("OrderDetail").orderBy("insertDate", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        orders.clear();
                        for (DocumentSnapshot doc : queryDocumentSnapshots) {
                            if (orderID == Integer.parseInt(doc.get("orderID").toString())) {
                                int menuID = Integer.parseInt(doc.getData().get("menuID").toString());
                                int quantity = Integer.parseInt(doc.getData().get("quantity").toString());
                                double total = Double.parseDouble(doc.getData().get("subtotal").toString());
                                o = new Orders(menuID, quantity, total);
                                orders.add(o);
                            }
                        }
                        db.collection("Menu").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                menus.clear();
                                if (task.isSuccessful()) {
                                    for (Orders order : orders)
                                        for (QueryDocumentSnapshot documentSnapshot : task.getResult())
                                            if (order.getMenuID() == Integer.parseInt(documentSnapshot.getData().get("menuID").toString()))
                                                menus.add(documentSnapshot.getData().get("menuName").toString());
                                    adapter = new OrderListAdapter(menus, orders, getApplicationContext());
                                    adapter.notifyDataSetChanged();
                                    orderList.setAdapter(adapter);
                                    totalPrice = findViewById(R.id.totalPriceTV);
                                    total = 0.0;
                                    for (int a = 0; a < orders.size(); a++)
                                        total += orders.get(a).getTotal();
                                    totalPrice.setText("RM " + String.format("%.2f", total));
                                    pd.dismiss();
                                }
                            }
                        });
                    }
                });
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_ORDER_ITEM && resultCode!=RESULT_CANCELED) {
            nextID=1;
            pd.show();
            final Orders order = (Orders) data.getSerializableExtra("addOrder");
            if (orderID == 0) {
                db.collection("PlacedOrder").orderBy("orderID", Query.Direction.DESCENDING).limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot doc : task.getResult())
                                nextID+=Integer.parseInt(doc.get("orderID").toString());
                            orderID = nextID;
                            Map<String, Object> placeOrder = new HashMap<>();
                            placeOrder.put("orderDate", FieldValue.serverTimestamp());
                            placeOrder.put("orderID", orderID);
                            placeOrder.put("paid", false);
                            placeOrder.put("tableNumber", tableNumber);
                            db.collection("PlacedOrder").add(placeOrder).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    Map<String, Object> newOrder = new HashMap<>();
                                    newOrder.put("menuID", order.getMenuID());
                                    newOrder.put("orderID", orderID);
                                    newOrder.put("quantity", order.getQuantity());
                                    newOrder.put("subtotal", order.getTotal());
                                    newOrder.put("insertDate", FieldValue.serverTimestamp());
                                    db.collection("OrderDetail").add(newOrder);
                                }
                            });
                        }
                    }
                });

            }
            else {

                db.collection("OrderDetail").whereEqualTo("orderID", orderID).whereEqualTo("menuID", order.getMenuID()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Map<String, Object> newOrder = new HashMap<>();
                            if (task.getResult().isEmpty()) {
                                newOrder.put("menuID", order.getMenuID());
                                newOrder.put("orderID", orderID);
                                newOrder.put("quantity", order.getQuantity());
                                newOrder.put("subtotal", order.getTotal());
                                newOrder.put("insertDate", FieldValue.serverTimestamp());
                                db.collection("OrderDetail").add(newOrder);
                            } else {
                                int quantity = 0;
                                double total = 0;
                                String docID = "";
                                for (QueryDocumentSnapshot d : task.getResult()) {
                                    docID = d.getId();
                                    quantity = Integer.parseInt(d.getData().get("quantity").toString());
                                    total = Double.parseDouble(d.getData().get("subtotal").toString());
                                }
                                newOrder.put("quantity", order.getQuantity() + quantity);
                                newOrder.put("subtotal", order.getTotal() + total);
                                db.collection("OrderDetail").document(docID).update(newOrder);

                            }
                        }
                    }
                });
            }

        } else if (requestCode == EDIT_ORDER_ITEM && resultCode!=RESULT_CANCELED) {
            final Orders order = (Orders) data.getSerializableExtra("editedOrderItem");
            final boolean remove = data.getBooleanExtra("removeEditItem", false);
            pd.show();
            db.collection("OrderDetail").whereEqualTo("orderID", orderID).whereEqualTo("menuID", order.getMenuID()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    int qty=0;
                    if (task.isSuccessful()) {
                        String docID = "";
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            docID = doc.getId();
                            qty = Integer.parseInt(doc.get("quantity").toString());
                        }
                        if (!remove) {
                            Map<String, Object> editOrder = new HashMap<>();
                            editOrder.put("quantity", order.getQuantity());
                            editOrder.put("subtotal", order.getTotal());
                            db.collection("OrderDetail").document(docID).update(editOrder);
                        } else {
                            db.collection("OrderDetail").document(docID).delete();
                        }
                        if(qty==order.getQuantity())
                            pd.dismiss();
                    }
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        if(orders.isEmpty() && orderID!=0)
            db.collection("PlacedOrder").whereEqualTo("orderID", orderID).whereEqualTo("paid", false).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful())
                        for(QueryDocumentSnapshot doc : task.getResult())
                            db.collection("PlacedOrder").document(doc.getId()).delete();
                    finish();
                }
            });
        else
            super.onBackPressed();
    }
}