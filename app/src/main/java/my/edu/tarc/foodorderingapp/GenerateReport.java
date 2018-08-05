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

import static android.view.View.INVISIBLE;

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
    Spinner reportTypeSpinner, foodTypeSpinner;
    popularMenuListAdapter popularMenuListAdapter;
    staffListAdapter staffListAdapter;
    ProgressDialog pd;
    TextView startDateTV, endDateTV ,col3TV, col4TV,col5TV, col6TV, typeTV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_report);

        startDateTV = (TextView) findViewById(R.id.startDate2TV);
        endDateTV = (TextView) findViewById(R.id.endDate2TV);
        col3TV = (TextView)findViewById(R.id.col3TV);
        col4TV = (TextView)findViewById(R.id.col4TV);
        col5TV = (TextView)findViewById(R.id.col5TV);
        col6TV = (TextView)findViewById(R.id.col6TV);
        menuListView = (ListView) findViewById(R.id.reportLV);
        reportTypeSpinner = (Spinner)findViewById(R.id.reportTypeSpinner);
        foodTypeSpinner = (Spinner)findViewById(R.id.foodTypeSpinner);
        typeTV = (TextView)findViewById(R.id.typeTV);

        reportTypeSpinner.setClickable(true);

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
                if(reportTypeSpinner.getItemAtPosition(pos).equals("Popular Menu")){

                    typeTV.setVisibility(view.VISIBLE);
                    foodTypeSpinner.setVisibility(view.VISIBLE);
                    foodTypeSpinner.setClickable(true);
                }
                else if(reportTypeSpinner.getItemAtPosition(pos).equals("Monthly Sales")){

                    typeTV.setVisibility(view.VISIBLE);
                    foodTypeSpinner.setVisibility(view.VISIBLE);
                    foodTypeSpinner.setClickable(true);
                }
                else if(reportTypeSpinner.getItemAtPosition(pos).equals("Staff Summary")){


                    typeTV.setVisibility(INVISIBLE);
                    foodTypeSpinner.setVisibility(INVISIBLE);
                    foodTypeSpinner.setClickable(false);
                }
                verifyInput();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        foodTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                verifyInput();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    public void verifyInput(){

        if(startDate != null && endDate != null) {

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
                col3TV.setText("");
                col5TV.setText("");
                col4TV.setText("");
                col6TV.setText("");
            }
            else{
                pd.show();
                String reportType = reportTypeSpinner.getSelectedItem().toString();

                if(reportType.equals("Staff Summary")){
                    getStaff_task1(newStartDateFormat, newEndDateFormat);
                }
                else if(reportType.equals("Monthly Sales") || reportType.equals("Popular Menu")) {
                    getMenuList(newStartDateFormat, newEndDateFormat, reportType);
                }


            }
        }


    }

    public void getMenuList(final Date startDate, final Date endDate, final String reportType){
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
                        String tempMenuDesc = document.getData().get("menuDesc").toString();
                        Boolean tempMenuAvailability = Boolean.parseBoolean(document.getData().get("menuStatus").toString());
                        double menuPrice = document.getDouble("menuPrice");

                        Menu newmenu = new Menu(tempMenuID, tempMenuName, tempMenuDesc, tempMenuType, tempMenuAvailability, menuPrice);
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
                    getPopularMenu(menuList, startDate, endDate, reportType);
            }
        });
    }


    public void getPopularMenu(final ArrayList<Menu> menuList, Date startDate, Date endDate, final String reportType){
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
                        double tempSales = Double.parseDouble(document.getData().get("subtotal").toString());
                        boolean exist = false;

                        //Below code is to insert "menu id" and "order quantity" into an arraylist
                        for (int j = 0; j < existingMenu.size(); j++) {

                            if (existingMenu.size() == 0) {
                                exist = false;
                            } else {
                                if (tempMenuID == existingMenu.get(j).getMenuID()) {     //If menuID already exist, they add quantity to the menuID

                                    int newQuantity = existingMenu.get(j).getQuantity() + tempQuantity;
                                    double newSales = existingMenu.get(j).getSales() + tempSales;
                                    existingMenu.get(j).setQuantity(newQuantity);
                                    existingMenu.get(j).setSales(newSales);
                                    exist = true;
                                }
                            }
                        }
                        if (exist == false) {    //menuID not exist, so add a new menuID together with its quantity
                            menu newmenu = new menu(tempMenuID, tempQuantity, tempSales);
                            existingMenu.add(newmenu);
                        }
                    }
                    //Bubble Sort

                        boolean swapped = true;
                        while (swapped) {
                            swapped = false;
                            for (int i = 1; i < existingMenu.size(); i++) {
                                menu temp = null;
                                if(reportType.equals("Popular Menu")){
                                    if (existingMenu.get(i - 1).getQuantity() < existingMenu.get(i).getQuantity()) {   //if everyting is sorted, swapped remain false because
                                        temp = existingMenu.get(i - 1);                                              //the if statements cannot be entered,
                                        existingMenu.set(i - 1, existingMenu.get(i));                                //then swapped = true cannot be executed, hence exit loop
                                        existingMenu.set(i, temp);
                                        swapped = true;
                                    }
                                }
                                else if(reportType.equals("Monthly Sales")){
                                    if (existingMenu.get(i - 1).getSales() < existingMenu.get(i).getSales()) {   //if everyting is sorted, swapped remain false because
                                        temp = existingMenu.get(i - 1);                                              //the if statements cannot be entered,
                                        existingMenu.set(i - 1, existingMenu.get(i));                                //then swapped = true cannot be executed, hence exit loop
                                        existingMenu.set(i, temp);
                                        swapped = true;
                                    }
                                }
                            }
                        }


                    ArrayList<menu> filteredFoodTypeMenu = new ArrayList<menu>();

                    for(int i = 0; i < existingMenu.size(); i++ ){
                        for(int j=0 ; j<menuList.size() ; j++){
                            if (existingMenu.get(i).getMenuID() == menuList.get(j).getMenuID()) {
                                if (menuList.get(j).getType().equals(foodTypeSpinner.getSelectedItem().toString())) {
                                    filteredFoodTypeMenu.add(existingMenu.get(i));
                                }
                            }
                        }
                    }

                    popularMenuListAdapter = new popularMenuListAdapter(filteredFoodTypeMenu, menuList, reportType, getApplicationContext());
                    menuListView.setAdapter(popularMenuListAdapter);
                    col3TV.setWidth(700);
                    col4TV.setWidth(0);
                    col5TV.setWidth(300);
                    col6TV.setWidth(0);
                    if(reportType.equals("Popular Menu")){
                        col3TV.setText("Menu Name");
                        col3TV.setWidth(700);
                        col5TV.setText("Total Quantity");
                        col5TV.setWidth(300);
                    }
                    else if(reportType.equals("Monthly Sales")){
                        col3TV.setText("Menu Name");
                        col3TV.setWidth(700);
                        col5TV.setText("Total Sales(RM)");
                        col5TV.setWidth(300);
                    }
                    pd.dismiss();
                }
            }
        });
    }

    //nd getStaff_task1 and getStaff_task2 to solve firestore "Range filter on different fields" limitation
    public void getStaff_task1(final Date startDate, final Date endDate){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final ArrayList<staff> staffList = new ArrayList<staff>();

        db.collection("Staff").whereGreaterThanOrEqualTo("staffLeaveDate", startDate).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot document : task.getResult()){

                        String staffID = document.getData().get("staffID").toString();
                        String staffName = document.getData().get("staffName").toString();
                        String staffPosition = document.getData().get("staffPosition").toString();
                        String staffStatus = document.getData().get("staffStatus").toString();

                        staff newStuff = new staff(staffID, staffName, staffPosition, staffStatus);
                        staffList.add(newStuff);
                    }
                    getStaff_task2(endDate, staffList);
                }
            }
        });
    }

    public void getStaff_task2(Date endDate, final ArrayList<staff> staffList1){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final ArrayList<staff> staffList2 = new ArrayList<staff>();
        final ArrayList<staff> staffListCombined = new ArrayList<staff>();

        db.collection("Staff").whereLessThanOrEqualTo("staffJoinDate", endDate).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot document : task.getResult()){

                        String staffID = document.getData().get("staffID").toString();
                        String staffName = document.getData().get("staffName").toString();
                        String staffPosition = document.getData().get("staffPosition").toString();
                        String staffStatus = document.getData().get("staffStatus").toString();

                        staff newStuff = new staff(staffID, staffName, staffPosition, staffStatus);
                        staffList2.add(newStuff);
                    }

                    for(int i = 0; i < staffList1.size(); i++){
                        for(int j = 0; j < staffList2.size(); j++){
                            if(staffList1.get(i).getStaffID().equals(staffList2.get(j).getStaffID())){
                                staffListCombined.add(staffList1.get(i));
                            }
                        }
                    }

                    //Bubble Sort

                    if(staffList1.size()!=0 && staffList2.size()!=0) {
                        boolean swapped = true;
                        while (swapped) {
                            swapped = false;
                            for (int i = 1; i > staffListCombined.size(); i++) {
                                staff temp = null;
                                if (staffListCombined.get(i - 1).getStaffName().compareTo(staffListCombined.get(i).getStaffName()) > 0) {   //if everyting is sorted, swapped remain false because
                                    temp = staffListCombined.get(i - 1);                                              //the if statements cannot be entered,
                                    staffListCombined.set(i - 1, staffListCombined.get(i));                                //then swapped = true cannot be executed, hence exit loop
                                    staffListCombined.set(i, temp);
                                    swapped = true;
                                }
                            }
                        }
                    }
                    staffListAdapter = new staffListAdapter(staffListCombined, getApplicationContext());
                    menuListView.setAdapter(staffListAdapter);
                    col3TV.setWidth(100);
                    col4TV.setWidth(420);
                    col5TV.setWidth(190);
                    col6TV.setWidth(190);
                    col3TV.setText("ID");
                    col4TV.setText("Name");
                    col5TV.setText("Position");
                    col6TV.setText("Status");
                    pd.dismiss();
                }


            }
        });
    }

    public class staffListAdapter extends BaseAdapter {
        private ArrayList<staff> staffList;
        private Context context;


        public staffListAdapter(ArrayList<staff> staffList, Context context) {
            this.staffList = staffList;
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
            return staffList.size();
        }

        @Override
        public staff getItem(int pos) {
            return staffList.get(pos);
        }

        @Override
        public View getView(int pos, View convertView, ViewGroup viewGroup) {
            View view1 = convertView;
            if(view1==null){
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view1 = inflater.inflate(R.layout.staff_list_view,null);
            }
            //Toast.makeText(getApplicationContext(), menu.get(pos).getMenuID() + ": " + menu.get(pos).getQuantity(), Toast.LENGTH_LONG).show();
            TextView staffIDTV = (TextView)view1.findViewById(R.id.staffIDTV);
            TextView staffNameTV = (TextView)view1.findViewById(R.id.staffNameTV);
            TextView staffPositionTV = (TextView)view1.findViewById(R.id.staffPositionTV);
            TextView staffStatusTV = (TextView)view1.findViewById(R.id.staffStatusTV);

            staffIDTV.setText(staffList.get(pos).getStaffID());
            staffNameTV.setText(staffList.get(pos).getStaffName());
            staffPositionTV.setText(staffList.get(pos).getStaffPosition());
            staffStatusTV.setText(staffList.get(pos).getStaffStatus());

            return view1;
        }
    }


    public class popularMenuListAdapter extends BaseAdapter {
        private ArrayList<menu> menu;
        private ArrayList<Menu> menuItems;
        private String reportType;
        private Context context;


        public popularMenuListAdapter(ArrayList<GenerateReport.menu> menu, ArrayList<Menu> menuItems, String reportType, Context context) {
            this.menu = menu;
            this.menuItems = menuItems;
            this.reportType = reportType;
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
            if(reportType.equals("Monthly Sales")){
                menuQuantityTV.setText(Double.toString(menu.get(pos).getSales()));
            }
            else if(reportType.equals("Popular Menu")) {
                menuQuantityTV.setText(Integer.toString(menu.get(pos).getQuantity()));
            }
            return view1;
        }
    }

    ///Custom object class for sorting menuID based on quantity
    public class menu{
        private int menuID;    //used for popular menu
        private int quantity;  //used for popular menu
        private double sales;  //this field only use for monthly sales

        public menu(int menuID, int quantity, double sales) {
            this.menuID = menuID;
            this.quantity = quantity;
            this.sales = sales;
        }

        public double getSales() {
            return sales;
        }

        public void setSales(double sales) {
            this.sales = sales;
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

    public class staff{
        private String staffID;
        private String staffName;
        private String staffPosition;
        private String staffStatus;

        public staff(String staffID, String staffName, String staffPosition, String staffStatus) {
            this.staffID = staffID;
            this.staffName = staffName;
            this.staffPosition = staffPosition;
            this.staffStatus = staffStatus;
        }

        public String getStaffID() {
            return staffID;
        }

        public void setStaffID(String staffID) {
            this.staffID = staffID;
        }

        public String getStaffName() {
            return staffName;
        }

        public void setStaffName(String staffName) {
            this.staffName = staffName;
        }

        public String getStaffPosition() {
            return staffPosition;
        }

        public void setStaffPosition(String staffPosition) {
            this.staffPosition = staffPosition;
        }

        public String getStaffStatus() {
            return staffStatus;
        }

        public void setStaffStatus(String staffStatus) {
            this.staffStatus = staffStatus;
        }
    }

}
