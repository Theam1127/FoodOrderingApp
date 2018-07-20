package my.edu.tarc.foodorderingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ConfirmAddOrderItem extends AppCompatActivity {
    TextView tvItemName, tvUnitPrice, tvTotalAmount;
    Button btnIncrease, btnDecrease, btnConfirm, btnCancel;
    EditText etOrderQuantity;
    Menu menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_add_order_item);
        tvItemName = findViewById(R.id.tvItemName);
        tvUnitPrice = findViewById(R.id.tvUnitPrice);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        btnIncrease = findViewById(R.id.buttonIncrease);
        btnDecrease = findViewById(R.id.buttonDecrease);
        btnCancel = findViewById(R.id.btnConfirm);
        btnCancel = findViewById(R.id.buttonCancel);
        etOrderQuantity = findViewById(R.id.editTextOrderQuantity);
        Intent intent = getIntent();
        menuItem = (Menu)intent.getSerializableExtra("orderItem");
        tvItemName.setText(menuItem.getName());
        tvUnitPrice.setText(""+menuItem.getPrice());
        etOrderQuantity.setText("0");

    }
}
