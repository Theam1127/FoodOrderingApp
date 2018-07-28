package my.edu.tarc.foodorderingapp;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MaintenanceSetting extends AppCompatActivity {
    Button buttonAddMenu, buttonUpdateMenu, buttonAddStaff, buttonUpdateStaff;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maintenance_setting);

        buttonAddMenu = findViewById(R.id.buttonAddMenu);
        buttonAddStaff = findViewById(R.id.buttonAddStaff);
        buttonUpdateMenu = findViewById(R.id.buttonUpdateMenu);
        buttonUpdateStaff = findViewById(R.id.buttonUpStaff);
        buttonAddMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAddMenu = new Intent(MaintenanceSetting.this, AddMenuActivity.class);
                startActivity(intentAddMenu);
            }
        });
        buttonUpdateMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentUpdateMenu = new Intent(MaintenanceSetting.this, UpdateMenuActivity.class);
                startActivity(intentUpdateMenu);
            }
        });
        buttonAddStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAddStaff = new Intent(MaintenanceSetting.this, AddStaffActivity.class);
                startActivity(intentAddStaff);
            }
        });
        buttonUpdateStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentUpdateStaff = new Intent(MaintenanceSetting.this,UpdateStaffActivity.class);
                startActivity(intentUpdateStaff);
            }
        });
    }
}
