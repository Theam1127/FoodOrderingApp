package my.edu.tarc.foodorderingapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddMenuActivity extends AppCompatActivity {

    private EditText editTextName,editTextDesc,editTextPrice;
    private RadioGroup radioGroupType;
    private RadioButton radioButtonTypeFood, radioButtonTypeDrink;
    private Button addMenu, reset;
    private FirebaseFirestore mFirestore;
    private String menuName, menuDesc, menuType;
    private double menuPrice;
    private int menuId;
    Menu menu = new Menu();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu);

        mFirestore = FirebaseFirestore.getInstance();

        editTextName = (EditText) findViewById(R.id.editTextMenuName);
        editTextDesc = (EditText) findViewById(R.id.editTextMenuDesc);
        editTextPrice = (EditText) findViewById(R.id.editTextMenuPrice);
        radioGroupType = (RadioGroup) findViewById(R.id.radioGroupMenuType);
        radioButtonTypeDrink = (RadioButton) findViewById(R.id.radioButtonDrink);
        radioButtonTypeFood = (RadioButton) findViewById(R.id.radioButtonFood);
        addMenu = (Button) findViewById(R.id.buttonAddMenu);
        reset = (Button) findViewById(R.id.buttonClear);

        addMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuName = editTextName.getText().toString();
                menuDesc = editTextDesc.getText().toString();
                menuPrice = Double.parseDouble(editTextPrice.getText().toString());
                menuId = 20;
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
                        Toast.makeText(AddMenuActivity.this,"Menu "+menuName+" added",Toast.LENGTH_SHORT).show();
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
