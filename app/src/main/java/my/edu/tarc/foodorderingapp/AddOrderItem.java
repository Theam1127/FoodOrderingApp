package my.edu.tarc.foodorderingapp;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    List<String> menuItems = new ArrayList<>();
    List<String> filterItems = new ArrayList<>();
    ArrayAdapter menuListAdapter, filterListAdapter;
    ProgressDialog pd;
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
                    menuItems.add((String)documentSnapshot.getData().get("menuName"));
                    if(!filterItems.contains(documentSnapshot.getData().get("menuType").toString()))
                        filterItems.add(documentSnapshot.getData().get("menuType").toString());
                }
                menuListAdapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,menuItems);
                menuList.setAdapter(menuListAdapter);
                filterListAdapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_dropdown_item_1line,filterItems);
                filterDropDown.setAdapter(filterListAdapter);
                pd.dismiss();
            }
        });
    }


}
