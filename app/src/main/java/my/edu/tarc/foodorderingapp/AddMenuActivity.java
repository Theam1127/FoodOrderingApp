package my.edu.tarc.foodorderingapp;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class AddMenuActivity extends AppCompatActivity {

    ProgressDialog pd;
    private EditText editTextName,editTextDesc,editTextPrice;
    private RadioGroup radioGroupType;
    private RadioButton radioButtonTypeFood, radioButtonTypeDrink;
    private Button addMenu, reset;
    private FirebaseFirestore mFirestore;
    private String menuName, menuDesc, menuType;
    private double menuPrice;
    private int menuId;
    Menu menu = new Menu();
    int newId=100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu);

        pd = new ProgressDialog(AddMenuActivity.this);

        mFirestore = FirebaseFirestore.getInstance();

        pd.setMessage("Please Wait...");
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        editTextName = (EditText) findViewById(R.id.editTextMenuName);
        editTextDesc = (EditText) findViewById(R.id.editTextMenuDesc);
        editTextPrice = (EditText) findViewById(R.id.editTextMenuPrice);
        radioGroupType = (RadioGroup) findViewById(R.id.radioGroupMenuType);
        radioButtonTypeDrink = (RadioButton) findViewById(R.id.radioButtonDrink);
        radioButtonTypeFood = (RadioButton) findViewById(R.id.radioButtonFood);
        addMenu = (Button) findViewById(R.id.buttonMaintenance);
        reset = (Button) findViewById(R.id.buttonClear);

        mFirestore.collection("Menu").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){

                    }
                    newId = task.getResult().size()+1;
                    menuId = newId;
                }
            }
        });

        pd.dismiss();

        addMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                menuName = editTextName.getText().toString();
                menuDesc = editTextDesc.getText().toString();
                menuPrice = Double.parseDouble(editTextPrice.getText().toString());
                if(radioButtonTypeDrink.isChecked())
                    menuType = radioButtonTypeDrink.getText().toString();
                else if(radioButtonTypeFood.isChecked())
                    menuType = radioButtonTypeFood.getText().toString();

                Map<String,Object> menuMap = new HashMap<>();

                menuMap.put("menuDesc",menuDesc);
                menuMap.put("menuID",menuId);
                menuMap.put("menuName", menuName);
                menuMap.put("menuPrice",menuPrice);
                menuMap.put("menuStatus",true);
                menuMap.put("menuType", menuType);

                mFirestore.collection("Menu").add(menuMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        pd.setMessage("Please Wait...");
                        pd.setCancelable(false);
                        pd.setCanceledOnTouchOutside(false);
                        pd.show();
                        Toast.makeText(AddMenuActivity.this,"Menu "+menuName+" added "+menuId,Toast.LENGTH_SHORT).show();

                        menuId++;
                        pd.dismiss();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String error = e.getMessage();

                        Toast.makeText(AddMenuActivity.this, "Error : "+error,Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }

}
