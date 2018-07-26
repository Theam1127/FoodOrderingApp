package my.edu.tarc.foodorderingapp;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class GenerateReport extends AppCompatActivity {

    int day_calendar1 = 0;
    int day_calendar2 = 0;
    int month_calendar1 = 0;
    int month_calendar2 = 0;
    int year_calendar1 = 0;
    int year_calendar2 = 0;

    String startDate = null;
    String endDate = null;
    ListView menuListView;
    Spinner reportTypeSpinner;
    popularMenuListAdapter adapter;
    ProgressDialog pd;
    TextView startDateTV, endDateTV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_report);

        startDateTV = (TextView) findViewById(R.id.startDate2TV);
        endDateTV = (TextView) findViewById(R.id.endDate2TV);
        menuListView = (ListView) findViewById(R.id.reportLV);
        reportTypeSpinner = (Spinner)findViewById(R.id.reportTypeSpinner);

        startDateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                if(day_calendar1 == 0 && month_calendar1 == 0 && year_calendar1 == 0) {
                    day_calendar1 = calendar.get(Calendar.DAY_OF_MONTH);
                    month_calendar1 = calendar.get(Calendar.MONTH);
                    year_calendar1 = calendar.get(Calendar.YEAR);
                }
                DatePickerDialog datePickerDialog = new DatePickerDialog(GenerateReport.this, new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        day_calendar1 = day;
                        month_calendar1 = month;
                        year_calendar1 = year;

                        int month1 = month + 1;
                        startDate = day + "/" + month1 + "/" + year;
                        startDateTV.setText(startDate);
                        verifyInput();
                    }
                }, year_calendar1, month_calendar1, day_calendar1);
                datePickerDialog.show();
            }
        });

        endDateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                if(day_calendar2 == 0 && month_calendar2 == 0 && year_calendar2 == 0) {
                    day_calendar2 = calendar.get(Calendar.DAY_OF_MONTH);
                    month_calendar2 = calendar.get(Calendar.MONTH);
                    year_calendar2 = calendar.get(Calendar.YEAR);
                }
                DatePickerDialog datePickerDialog = new DatePickerDialog(GenerateReport.this, new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        day_calendar2 = day;
                        month_calendar2 = month;
                        year_calendar2 = year;

                        int month1 = month + 1;

                        endDate = day + "/" + month1 + "/" + year;
                        endDateTV.setText(endDate);
                        verifyInput();
                    }
                }, year_calendar2, month_calendar2, day_calendar2);
                datePickerDialog.show();
            }
        });


        pd = new ProgressDialog(GenerateReport.this);
        pd.setMessage("Please Wait...");
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);

        reportTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                String reportType = reportTypeSpinner.getItemAtPosition(pos).toString();
                if(reportType.equals("Monthly Sales")) {
                    getMonthlySales();
                }
                else if(reportType.equals("Popular Menu")) {
                    verifyInput();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    public void verifyInput(){

        if(startDate != null && endDate != null && reportTypeSpinner.getSelectedItem().toString().equals("Popular Menu")) {

            Date newStartDateFormat = null;
            Date newEndDateFormat = null;
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

            try {
                Date startdate = df.parse(startDate + " " + "00:00:00");
                newStartDateFormat = startdate;
                Date enddate = df.parse(endDate + " " + "23:59:59");
                newEndDateFormat = enddate;

            } catch (ParseException ex) {

            }

            if(newStartDateFormat.after(newEndDateFormat)) {
                Toast.makeText(getApplicationContext(), "Start date must be before or on the same day as end date.", Toast.LENGTH_LONG).show();
                menuListView.setAdapter(null);
            }
            else{
                pd.show();
                getPopularMenu_task1(newStartDateFormat,newEndDateFormat);
            }
        }
        //Toast.makeText(getApplicationContext(), newStartDateFormat + " and" + newEndDateFormat, Toast.LENGTH_LONG).show();

    }

    public void getPopularMenu_task1(final Date startDate, final Date endDate){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final ArrayList<Menu> menuList = new ArrayList<Menu>();

        db.collection("Menu").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot document : task.getResult()){

                        int tempMenuID = Integer.parseInt(document.getData().get("menuID").toString());
                        String tempMenuName = document.getData().get("menuName").toString();
                        String tempMenuType = document.getData().get("menuType").toString();
                        Boolean tempMenuAvailability = Boolean.parseBoolean(document.getData().get("menuStatus").toString());
                        double menuPrice = document.getDouble("menuPrice");

                        Menu newmenu = new Menu(tempMenuID, tempMenuName, tempMenuType, tempMenuAvailability, menuPrice);
                        menuList.add(newmenu);
                    }
                }
                //Bubble Sort
                boolean swapped = true;
                while(swapped){
                    swapped = false;
                    for(int i = 1; i > menuList.size(); i ++){
                        Menu temp = null;
                        if(menuList.get(i-1).getMenuID() < menuList.get(i).getMenuID()){   //if everyting is sorted, swapped remain false because
                            temp = menuList.get(i-1);                                              //the if statements cannot be entered,
                            menuList.set(i-1, menuList.get(i));                                //then swapped = true cannot be executed, hence exit loop
                            menuList.set(i, temp);
                            swapped = true;
                        }
                    }
                }
                getPopularMenu_task2(menuList, startDate, endDate);
            }
        });
    }


    public void getPopularMenu_task2(final ArrayList<Menu> menuList, Date startDate, Date endDate){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final ArrayList<menu> existingMenu = new ArrayList<menu>();


        db.collection("OrderDetail").whereGreaterThanOrEqualTo("insertDate", startDate).whereLessThanOrEqualTo("insertDate", endDate).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot document : task.getResult()){

                        //Date date = (Date) document.getData().get("insertDate");
                        //String date = document.getData().get("insertDate").toString();

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

                    adapter = new popularMenuListAdapter(existingMenu, menuList, getApplicationContext());
                    menuListView.setAdapter(adapter);
                    pd.dismiss();
                }
            }
        });
    }

    public void getMonthlySales(){

    }

    public class popularMenuListAdapter extends BaseAdapter {
        private ArrayList<menu> menu;
        private ArrayList<Menu> menuItems;
        private Context context;


        public popularMenuListAdapter(ArrayList<GenerateReport.menu> menu, ArrayList<Menu> menuItems, Context context) {
            this.menu = menu;
            this.menuItems = menuItems;
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
            //Toast.makeText(getApplicationContext(), menu.get(pos).getMenuID() + ": " + menu.get(pos).getQuantity(), Toast.LENGTH_LONG).show();
            TextView menuNameTV = (TextView)view1.findViewById(R.id.menuNameTV);
            TextView menuQuantityTV = (TextView)view1.findViewById(R.id.menuQuantityTV);
            String menuName = "<Deleted>";
            for(int i=0; i < menuItems.size(); i++){
                if (menu.get(pos).getMenuID() == menuItems.get(i).getMenuID())
                    menuName = menuItems.get(i).getName();
            }
            menuNameTV.setText(menuName );
            menuQuantityTV.setText(Integer.toString(menu.get(pos).getQuantity()));
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
