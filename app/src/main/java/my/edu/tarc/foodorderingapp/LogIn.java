package my.edu.tarc.foodorderingapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class LogIn extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private Button btnLogin;

    private String strUsername;
    private String strPassword;
    private FirebaseFirestore db;
    private ProgressDialog pd;
    private SharedPreferences.Editor share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        db = FirebaseFirestore.getInstance();
        pd = new ProgressDialog(LogIn.this);
        pd.setMessage("Retrieving Data");
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);

        username = (EditText)findViewById(R.id.etUsername);
        password = (EditText)findViewById(R.id.etPassword);
        btnLogin = (Button)findViewById(R.id.btnlogin);




        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strUsername = username.getText().toString();
                strPassword = password.getText().toString();
                pd.show();
                db.collection("Security")
                        .whereEqualTo("staffID",strUsername)
                        .whereEqualTo("staffPassword",strPassword)
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            if(!task.getResult().isEmpty()) {
                                share = getSharedPreferences("staffID",MODE_PRIVATE).edit();
                                share.putString("id",strUsername);
                                share.apply();
                                Intent intent = new Intent(LogIn.this,HomePage.class);
                                startActivity(intent);
                                pd.dismiss();
                                finish();
                            }
                            else{
                                Toast.makeText(LogIn.this, "Invalid Account", Toast.LENGTH_SHORT).show();
                                pd.dismiss();
                            }
                        }else {
                            Toast.makeText(LogIn.this,"Retrieve Data Fail",Toast.LENGTH_SHORT).show();
                            pd.dismiss();
                        }
                    }
                });
            }
        });



        
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.exit(0);
    }
}
