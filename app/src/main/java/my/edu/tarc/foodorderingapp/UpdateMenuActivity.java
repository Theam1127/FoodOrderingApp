package my.edu.tarc.foodorderingapp;

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
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class UpdateMenuActivity extends AppCompatActivity {

    private EditText editName, editDesc, editPrice;
    private RadioButton radioTypeFood, radioTypeDrink, radioStatusTrue, radioStatusFalse;
    private Button buttonClear, buttonUpdate;
    private Spinner spinnerID;
    private FirebaseFirestore mFireStore;

    List<String> menuIDSpinner = new ArrayList<>();
    List<Menu> menuList =  new ArrayList<>();
    Menu menuItem;
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
        buttonUpdate = (Button) findViewById(R.id.buttonUpdateMenu);

        mFireStore =  FirebaseFirestore.getInstance();

        mFireStore.collection("Menu").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        String menuID = ""+documentSnapshot.getData().get("menuID");
                        boolean status = (boolean)documentSnapshot.getData().get("menuStatus");
                        String name = ""+documentSnapshot.getData().get("menuName");
                        double price = Double.parseDouble(""+documentSnapshot.getData().get("menuPrice"));
                        String type = ""+documentSnapshot.getData().get("menuType");
                        menuItem=new Menu(menuID, name,type,status,price);
                        menuList.add(menuItem);
                        menuIDSpinner.add(menuID);

                    }

                }

            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, menuIDSpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.notifyDataSetChanged();
        spinnerID.setAdapter(adapter);

        /*spinnerID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                editName.setText("test");
                Toast.makeText(UpdateMenuActivity.this,"asdsadtext",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                editDesc.setText("jasdjk");
            }
        });*/

    }
}
