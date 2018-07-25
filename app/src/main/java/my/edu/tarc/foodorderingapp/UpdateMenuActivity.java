package my.edu.tarc.foodorderingapp;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateMenuActivity extends AppCompatActivity {

    private EditText editName, editDesc, editPrice;
    private RadioButton radioTypeFood, radioTypeDrink, radioStatusTrue, radioStatusFalse;
    private Button buttonClear, buttonUpdate;
    private Spinner spinnerID;
    private FirebaseFirestore mFireStore;
    ProgressDialog pd;

    List<Integer> menuIdSpinner = new ArrayList<>();
    List<Menu> menuList =  new ArrayList<>();
    Menu menuItem;
    ArrayAdapter menuIdList;
    String[] menuIDSpinner;

    int tempCount=0;
    String tempType; boolean tempStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_menu);

        spinnerID = (Spinner) findViewById(R.id.spinnerId);
        editDesc = (EditText) findViewById(R.id.editMenuDesc);
        editName = (EditText) findViewById(R.id.editMenuName);
        editPrice = (EditText) findViewById(R.id.editMenuPrice);
        radioStatusFalse = (RadioButton) findViewById(R.id.radioButtonFalse);
        radioStatusTrue = (RadioButton) findViewById(R.id.radioButtonTrue);
        radioTypeDrink = (RadioButton) findViewById(R.id.radioButtonDrink2);
        radioTypeFood = (RadioButton) findViewById(R.id.radioButtonFood2);
        buttonClear = (Button) findViewById(R.id.buttonClear);

        mFireStore =  FirebaseFirestore.getInstance();

        //menuIdSpinner.add("--Select an item--");

        loadActivity();


        buttonUpdate = (Button) findViewById(R.id.buttonUpdateMenu);
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(radioTypeFood.isChecked())
                    tempType = "Food";
                else if(radioTypeDrink.isChecked())
                    tempType = "Drink";
                if(radioStatusTrue.isChecked())
                    tempStatus = true;
                else if(radioStatusFalse.isChecked())
                    tempStatus = false;
                mFireStore.collection("Menu").whereEqualTo("menuID", menuList.get(tempCount).getMenuID()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            Map<String,Object> menuMap = new HashMap<>();

                            menuMap.put("menuDesc",editDesc.getText().toString());
                            menuMap.put("menuID",menuList.get(tempCount).getMenuID());
                            menuMap.put("menuName", editName.getText().toString());
                            menuMap.put("menuPrice",Double.parseDouble(editPrice.getText().toString()));
                            menuMap.put("menuStatus",tempStatus);
                            menuMap.put("menuType", tempType);
                            String docID="";
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                docID = documentSnapshot.getId();

                            }
                            mFireStore.collection("Menu").document(docID).update(menuMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(UpdateMenuActivity.this, "Update Successfull", Toast.LENGTH_SHORT).show();
                                    loadActivity();
                                    menuList.clear();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(UpdateMenuActivity.this, "Update Fail", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
            }
        });



    }

    private void loadActivity(){
        pd = new ProgressDialog(UpdateMenuActivity.this);
        pd.setMessage("Please Wait...");
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(true);
        pd.show();
        menuList.clear();
        spinnerID.setAdapter(null);
        menuIdList=null;
        menuIdSpinner.clear();
        mFireStore.collection("Menu").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){

                    for(QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        //String tempId = "" + documentSnapshot.getData().get("menuID").toString();

                        int menuID = Integer.parseInt(""+documentSnapshot.getData().get("menuID"));
                        menuIdSpinner.add(menuID);
                        //menuIdSpinner.add(""+documentSnapshot.getData().get("menuID").toString());
                        boolean status = (boolean)documentSnapshot.getData().get("menuStatus");
                        String name = ""+documentSnapshot.getData().get("menuName");
                        double price = Double.parseDouble(""+documentSnapshot.getData().get("menuPrice"));
                        String type = ""+documentSnapshot.getData().get("menuType");
                        String desc = ""+documentSnapshot.getData().get("menuDesc");
                        menuItem=new Menu(menuID, name,desc,type,status,price);
                        menuList.add(menuItem);
                    }
                    Collections.sort(menuIdSpinner);
                    Collections.sort(menuList, new Comparator<Menu>() {
                        @Override
                        public int compare(Menu o1, Menu o2) {
                            return Double.compare(o1.getMenuID(),o2.getMenuID());
                        }
                    });
                    menuIdList = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, menuIdSpinner);
                    menuIdList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerID.setAdapter(menuIdList);

                    pd.dismiss();
                    spinnerID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            editName.setText(""+menuList.get(i).getName());

                            editDesc.setText(""+menuList.get(i).getDesc());

                            editPrice.setText(""+menuList.get(i).getPrice());

                            if((menuList.get(i).getType()).equals("Food")) {
                                radioTypeFood.toggle();
                                //tempType = radioTypeFood.getText().toString();
                            }
                            else if((menuList.get(i).getType()).equals("Drink")){
                                radioTypeDrink.toggle();
                                //tempType = radioTypeDrink.getText().toString();
                            }
                            if(menuList.get(i).isAvailable()){
                                radioStatusTrue.toggle();
                                //tempStatus = false;
                            }
                            else{
                                radioStatusFalse.toggle();
                                //tempStatus = true;
                            }
                            tempCount = i;

                            //Toast.makeText(UpdateMenuActivity.this,"asdsadtext",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                            editDesc.setText("jasdjk");
                        }
                    });

                }

            }
        });
    }
}
