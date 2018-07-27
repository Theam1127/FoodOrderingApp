package my.edu.tarc.foodorderingapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class HomePage extends AppCompatActivity {

    private ImageButton btnOrder, btnPayHis, btnGenerateReport, btnMaintenance;
    private Button btnLogout;
    private SharedPreferences share;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        btnOrder = findViewById(R.id.imageButtonOrder);
        btnPayHis = findViewById(R.id.imageButtonHistory);
        btnGenerateReport = findViewById(R.id.imageButtonReport);
        btnMaintenance = findViewById(R.id.imageButtonMaintenance);
        btnLogout = findViewById(R.id.buttonLogout);
        //Yeap THeam staff ID
        share = getSharedPreferences("staffID", Context.MODE_PRIVATE);
        String stid = share.getString("id",null);
        editor = share.edit();


        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentOrder = new Intent(HomePage.this,RestaurantTables.class);
                startActivity(intentOrder);
            }
        });

        btnPayHis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentPayHis = new Intent(HomePage.this,PaymentHistory.class);
                startActivity(intentPayHis);
            }
        });
        btnGenerateReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentReport = new Intent(HomePage.this,GenerateReport.class);
                startActivity(intentReport);
            }
        });
        btnMaintenance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this, MaintenanceSetting.class);
                startActivity(intent);
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.clear();
                editor.commit();
                Intent intent = new Intent(HomePage.this, LogIn.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
