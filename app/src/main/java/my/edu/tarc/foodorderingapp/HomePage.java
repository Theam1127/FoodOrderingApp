package my.edu.tarc.foodorderingapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class HomePage extends AppCompatActivity {

    private Button btnOrder;
    private Button btnPayHis;
    private Button btnGenerateReport;
    private Button buttonUpdateMenu, buttonAddMenu, buttonAddStaff, buttonUpdateStaff;
    private SharedPreferences share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        btnOrder = (Button)findViewById(R.id.btnOrder);
        btnPayHis = (Button)findViewById(R.id.btnPayHis);
        btnGenerateReport = (Button)findViewById(R.id.btnGenerateReport);
        buttonAddMenu = (Button)findViewById(R.id.buttonAddMenu);
        buttonUpdateMenu = (Button)findViewById(R.id.buttonUpdateMenu);
        buttonAddStaff = (Button)findViewById(R.id.buttonAddStaff);
        buttonUpdateStaff = (Button)findViewById(R.id.buttonUpdateStaff);

        //Yeap THeam staff ID
        share = getSharedPreferences("staffID", Context.MODE_PRIVATE);
        String stid = share.getString("id",null);


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
        buttonAddMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAddMenu = new Intent(HomePage.this, AddMenuActivity.class);
                startActivity(intentAddMenu);
            }
        });
        buttonUpdateMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentUpdateMenu = new Intent(HomePage.this, UpdateMenuActivity.class);
                startActivity(intentUpdateMenu);
            }
        });
        buttonAddStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAddStaff = new Intent(HomePage.this, AddStaffActivity.class);
                startActivity(intentAddStaff);
            }
        });
        buttonUpdateStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intentUpdateStaff = new Intent(HomePage.this, AddMenuActivity.class);
                //startActivity(intentAddMenu);
            }
        });

    }
}
