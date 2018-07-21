package my.edu.tarc.foodorderingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ConfirmAddOrderItem extends AppCompatActivity {
    TextView tvItemName, tvUnitPrice, tvTotalAmount;
    Button btnIncrease, btnDecrease, btnConfirm, btnCancel;
    EditText etOrderQuantity;
    Menu menuItem;
    double total;
    int qty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_add_order_item);
        tvItemName = findViewById(R.id.tvItemName);
        tvUnitPrice = findViewById(R.id.tvUnitPrice);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        btnIncrease = findViewById(R.id.buttonIncrease);
        btnDecrease = findViewById(R.id.buttonDecrease);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnCancel = findViewById(R.id.btnBack);
        etOrderQuantity = findViewById(R.id.editTextOrderQuantity);
        menuItem = (Menu)getIntent().getSerializableExtra("menuItem");
        tvItemName.setText(menuItem.getName());
        tvUnitPrice.setText("RM "+String.format("%.2f",menuItem.getPrice()));
        etOrderQuantity.setText("0");
        total = Integer.parseInt(etOrderQuantity.getText().toString()) * menuItem.getPrice();
        tvTotalAmount.setText("RM "+String.format("%.2f",total));
        etOrderQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etOrderQuantity.selectAll();
            }
        });
        etOrderQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(etOrderQuantity.getText().toString().equals("")) {
                    etOrderQuantity.setText("0");
                    etOrderQuantity.selectAll();
                }
                qty = Integer.parseInt(etOrderQuantity.getText().toString());

                double price = menuItem.getPrice();
                total = price*qty;
                tvTotalAmount.setText(String.format("RM %.2f", total));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qty = Integer.parseInt(etOrderQuantity.getText().toString());
                qty++;
                etOrderQuantity.setText(Integer.toString(qty));
            }
        });

        btnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qty = Integer.parseInt(etOrderQuantity.getText().toString());
                if(qty!=0)
                    qty--;
                etOrderQuantity.setText(Integer.toString(qty));
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etOrderQuantity.getText().toString().equals("0"))
                    Toast.makeText(getApplicationContext(), "Quantity should not be 0", Toast.LENGTH_SHORT).show();
                else{
                    Orders order = new Orders(menuItem.getMenuID(), qty, total);
                    Intent intent = new Intent();
                    intent.putExtra("confirmOrder", order);
                    setResult(AddOrderItem.ADD_ITEM_REQUEST, intent);
                    finish();
                }
            }
        });
    }
}
