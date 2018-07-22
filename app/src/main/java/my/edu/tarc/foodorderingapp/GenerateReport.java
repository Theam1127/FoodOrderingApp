package my.edu.tarc.foodorderingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GenerateReport extends AppCompatActivity {

    ListView menuListView;
    Spinner reportTypeSpinner;
    popularMenuListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_report);


        //get  menu database
        //read menuid and name into array
        //sort according to menuidfire
        //in orderdetail get menu name based on menuID through array

        menuListView = (ListView) findViewById(R.id.reportLV);
        reportTypeSpinner = (Spinner)findViewById(R.id.reportTypeSpinner);

        reportTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                String reportType = reportTypeSpinner.getItemAtPosition(pos).toString();
                if(reportType=="Monthly Sales")
                    getMonthlySales();
                else if(reportType=="Popular Menu")
                    getPopularMenu();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    public void getPopularMenu(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final ArrayList<menu> existingMenu = new ArrayList<menu>();


        db.collection("OrderDetail").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot document : task.getResult()){

                        int tempMenuID = Integer.parseInt(document.getData().get("menuID").toString());
                        int tempQuantity = Integer.parseInt(document.getData().get("quantity").toString());
                        boolean exist = false;

                        //Below code is to insert "menu id" and "order quantity" into an arraylist
                        for (int j = 0; j < existingMenu.size(); j++) {

                            if (existingMenu.size() == 0) {
                                exist = false;
                            } else {
                                if (tempMenuID == existingMenu.get(j).getMenuID()) {     //If menuID already exist, they add quantity to the menuID

                                    int newQuantity = existingMenu.get(j).getQuantity() + tempQuantity;
                                    existingMenu.get(j).setQuantity(newQuantity);
                                    exist = true;
                                }
                            }
                        }
                        if (exist == false) {    //menuID not exist, so add a new menuID together with its quantity
                            menu newmenu = new menu(tempMenuID, tempQuantity);
                            existingMenu.add(newmenu);
                        }

                    }
                    //Bubble Sort
                    boolean swapped = true;
                    while(swapped){
                        swapped = false;
                        for(int i = 1; i < existingMenu.size(); i ++){
                            menu temp = null;
                            if(existingMenu.get(i-1).getQuantity() < existingMenu.get(i).getQuantity()){   //if everyting is sorted, swapped remain false because
                                temp = existingMenu.get(i-1);                                              //the if statements cannot be entered,
                                existingMenu.set(i-1, existingMenu.get(i));                                //then swapped = true cannot be executed, hence exit loop
                                existingMenu.set(i, temp);
                                swapped = true;
                            }
                        }
                    }

                    adapter = new popularMenuListAdapter(existingMenu, getApplicationContext());
                    menuListView.setAdapter(adapter);
                }
            }
        });
    }

    public void getMonthlySales(){

    }

    public class popularMenuListAdapter extends BaseAdapter {
        private List<menu> menu;
        private Context context;

        public popularMenuListAdapter(List<GenerateReport.menu> menu, Context context) {
            this.menu = menu;
            this.context = context;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean areAllItemsEnabled() {
            return super.areAllItemsEnabled();
        }

        @Override
        public int getCount() {
            return menu.size();
        }

        @Override
        public menu getItem(int pos) {
            return menu.get(pos);
        }

        @Override
        public View getView(int pos, View convertView, ViewGroup viewGroup) {
            View view1 = convertView;
            if(view1==null){
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view1 = inflater.inflate(R.layout.popular_menu_list_view,null);
            }
            TextView menuNameTV = (TextView)view1.findViewById(R.id.menuNameTV);
            TextView menuQuantityTV = (TextView)view1.findViewById(R.id.menuQuantityTV);
            menuNameTV.setText(menu.get(pos).getMenuID());
            menuQuantityTV.setText(menu.get(pos).getQuantity());
            return view1;
        }
    }

    ///Custom object class for sorting menuID based on quantity
    public class menu{
        private int menuID;
        private int quantity;

        public menu(int menuID, int quantity) {
            this.menuID = menuID;
            this.quantity = quantity;
        }

        public int getMenuID() {
            return menuID;
        }

        public void setMenuID(int menuID) {
            this.menuID = menuID;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }


}
