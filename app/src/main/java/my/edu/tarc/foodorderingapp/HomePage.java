package my.edu.tarc.foodorderingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomePage extends AppCompatActivity {

    private Button btnOrder;
    private Button btnPayHis;
    private Button btnGenerateReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        btnOrder = (Button)findViewById(R.id.btnOrder);
        btnPayHis = (Button)findViewById(R.id.btnPayHis);
        btnGenerateReport = (Button)findViewById(R.id.btnGenerateReport);

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

    }
}
