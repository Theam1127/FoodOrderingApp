package my.edu.tarc.foodorderingapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;


public class AddOrderItem extends AppCompatActivity {
    EditText searchText;
    Spinner filterDropDown;
    ListView menuList;
    List<Menu> menu = new ArrayList<>();
    List<String> menuItems = new ArrayList<>();
    List<String> filterItems = new ArrayList<>();
    List<Menu> filteredMenuList = new ArrayList<>();
    List<String> filteredMenuName = new ArrayList<>();

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
        searchText = (EditText) findViewById(R.id.editTextSearch);
        filterDropDown = (Spinner) findViewById(R.id.filterSpinner);
        menuList = (ListView) findViewById(R.id.menuList);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        filterItems.add("All");
        db.collection("Menu").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    menu.clear();
                    menuItems.clear();
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        int menuID = Integer.parseInt(documentSnapshot.get("menuID").toString());
                        boolean available = Boolean.parseBoolean(documentSnapshot.getData().get("menuStatus").toString());
                        String name = documentSnapshot.getString("menuName");
                        double price = documentSnapshot.getDouble("menuPrice");
                        String type = documentSnapshot.getString("menuType");
                        String desc = "asdsa";
                        Menu item = new Menu(menuID, name,desc, type, available, price);
                        menu.add(item);
                        menuItems.add(name);
                        if (!filterItems.contains(documentSnapshot.getData().get("menuType").toString()))
                            filterItems.add(documentSnapshot.getData().get("menuType").toString());
                    }
                    filteredMenuName = menuItems;
                    filteredMenuList = menu;
                    menuListAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, filteredMenuName);
                    menuList.setAdapter(menuListAdapter);
                    filterListAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, filterItems);
                    filterDropDown.setAdapter(filterListAdapter);
                    pd.dismiss();

                    searchText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            filteredMenuName = new ArrayList<>();
                            filteredMenuList = new ArrayList<>();
                            for (Menu item : menu) {
                                if (item.getName().toLowerCase().contains(charSequence)) {
                                    filteredMenuList.add(item);
                                    filteredMenuName.add(item.getName());
                                }
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                            menuListAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, filteredMenuName);
                            menuList.setAdapter(menuListAdapter);
                        }
                    });

                    filterDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            filteredMenuName = new ArrayList<>();
                            filteredMenuList = new ArrayList<>();
                            if (i != 0) {
                                for (Menu item : menu) {
                                    if (item.getType().equals(adapterView.getSelectedItem().toString())) {
                                        filteredMenuList.add(item);
                                        filteredMenuName.add(item.getName());
                                    }
                                }
                            } else {
                                filteredMenuList = menu;
                                filteredMenuName = menuItems;
                            }
                            menuListAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, filteredMenuName);
                            menuList.setAdapter(menuListAdapter);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }

                    });


                    menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Intent intent = new Intent(getApplicationContext(), ConfirmAddOrderItem.class);
                            intent.putExtra("menuItem", filteredMenuList.get(i));
                            startActivityForResult(intent, ADD_ITEM_REQUEST);
                        }
                    });
                }

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
