package my.edu.tarc.foodorderingapp;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;

public class MaintenanceSetting extends AppCompatActivity {
    MS_FragmentsAdapter ms_fragmentsAdapter;
    ViewPager ms_viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maintenance_setting);
        ms_fragmentsAdapter = new MS_FragmentsAdapter(getSupportFragmentManager());
        ms_viewPager = (ViewPager)findViewById(R.id.container);
        setupViewPager(ms_viewPager);

        TabLayout tabLayout = (TabLayout)findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(ms_viewPager);

    }

    private void setupViewPager(ViewPager viewPager){
        MS_FragmentsAdapter fragmentsAdapter = new MS_FragmentsAdapter(getSupportFragmentManager());
        fragmentsAdapter.addFragment(new MS_MenuMaintenance(),"Menu Maintenance");
        fragmentsAdapter.addFragment(new MS_StaffMaintenance(),"Staff Maintenance");
        viewPager.setAdapter(fragmentsAdapter);

    }
}
