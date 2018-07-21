package my.edu.tarc.foodorderingapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class AddOrderItem extends AppCompatActivity {
    EditText searchText;
    Spinner filterDropDown;
    ListView menuList;
    List<Menu> menu = new ArrayList<>();
    List<String> menuItems = new ArrayList<>();
    List<String> filterItems = new ArrayList<>();
    ArrayAdapter menuListAdapter, filterListAdapter;
    ProgressDialog pd;
    static final int ADD_ITEM_REQUEST = 101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order_item);

        pd = new ProgressDialog(this);
        pd.setMessage("Please Wait...");
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        searchText = (EditText)findViewById(R.id.editTextSearch);
        filterDropDown = (Spinner)findViewById(R.id.filterSpinner);
        menuList = (ListView) findViewById(R.id.menuList);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Menu").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                    int menuID = Integer.parseInt(documentSnapshot.getData().get("menuID").toString());
                    boolean available = (boolean)documentSnapshot.getData().get("menuStatus");
                    String name = ""+documentSnapshot.getData().get("menuName");
                    double price = Double.parseDouble(documentSnapshot.getData().get("menuPrice").toString());
                    String type = ""+documentSnapshot.getData().get("menuType");
                    Menu item = new Menu(menuID, name,type,available,price);
                    menu.add(item);
                    menuItems.add(name);
                    if(!filterItems.contains(documentSnapshot.getData().get("menuType").toString()))
                        filterItems.add(documentSnapshot.getData().get("menuType").toString());
                }
                menuListAdapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,menuItems);
                menuList.setAdapter(menuListAdapter);
                filterListAdapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_dropdown_item_1line,filterItems);
                filterDropDown.setAdapter(filterListAdapter);
                pd.dismiss();

                menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(getApplicationContext(),ConfirmAddOrderItem.class);
                        intent.putExtra("menuItem", menu.get(i));
                        startActivityForResult(intent, ADD_ITEM_REQUEST);
                    }
                });
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==ADD_ITEM_REQUEST && resultCode!=RESULT_CANCELED){
            Orders order = (Orders)data.getSerializableExtra("confirmOrder");
            Intent intent = new Intent();
            intent.putExtra("addOrder", order);
            setResult(MakeOrder.ADD_ORDER_ITEM, intent);
            finish();
        }
    }
}
