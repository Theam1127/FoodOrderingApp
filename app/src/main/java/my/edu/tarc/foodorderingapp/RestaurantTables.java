package my.edu.tarc.foodorderingapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class RestaurantTables extends AppCompatActivity {
    TableLayout tables;
    TableRow row;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_tables);
        tables = findViewById(R.id.tablesLayout);
    }

    @Override
    protected void onResume() {
        super.onResume();
        tables.removeAllViews();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Please wait...");
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        final List<Button> allTables = new ArrayList<>();
        for (int a = 0, tableNo = 1; a < 10; a++) {
            row = new TableRow(this);
            for (int i = 0; i < 3 && tableNo < 31; i++, tableNo++) {
                final Button singleTable = new Button(this);
                TableRow.LayoutParams param = new TableRow.LayoutParams();
                param.setMargins(10, 10, 10, 10);
                param.width = 325;
                singleTable.setLayoutParams(param);
                singleTable.setText("" + tableNo);
                singleTable.setBackgroundColor(Color.GREEN);
                singleTable.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), MakeOrder.class);
                        intent.putExtra("table", Integer.valueOf(singleTable.getText().toString()));
                        startActivity(intent);
                    }
                });
                row.addView(singleTable);
                allTables.add(singleTable);
            }
            tables.addView(row);
        }

        db.collection("PlacedOrder").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (DocumentSnapshot doc : queryDocumentSnapshots) {
                    for (Button b : allTables) {
                        if (!doc.getBoolean("paid") && Integer.parseInt(doc.get("tableNumber").toString()) == Integer.parseInt(b.getText().toString()))
                            b.setBackgroundColor(Color.RED);
                    }
                }
                pd.dismiss();
            }
        });
    }
}
