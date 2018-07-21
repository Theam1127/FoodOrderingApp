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
    Button btnIncrease, btnDecrease, btnConfirm, btnCancel, btnRemoveItem;
    EditText etOrderQuantity;
    Menu menuItem;
    Orders orderItem;
    String orderItemName;
    double unitPrice, total;
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
        btnRemoveItem = findViewById(R.id.buttonRemoveItem);
        etOrderQuantity = findViewById(R.id.editTextOrderQuantity);
        menuItem = (Menu)getIntent().getSerializableExtra("menuItem");
        qty = 0;
        if(menuItem == null){
            orderItem = (Orders)getIntent().getSerializableExtra("editOrderedItem");
            orderItemName = getIntent().getStringExtra("editOrderItemName");
            unitPrice = orderItem.getTotal()/orderItem.getQuantity();
            qty = orderItem.getQuantity();
            btnRemoveItem.setVisibility(View.VISIBLE);
        }
        else{
            orderItemName = menuItem.getName();
            unitPrice = menuItem.getPrice();
        }
        tvItemName.setText(orderItemName);
        tvUnitPrice.setText("RM "+String.format("%.2f",unitPrice));
        etOrderQuantity.setText(""+qty);
        total = qty * unitPrice;
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

                double price = unitPrice;
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
                    if(!menuItem.equals(null)) {
                        Orders order = new Orders(menuItem.getMenuID(), qty, total);
                        Intent intent = new Intent();
                        intent.putExtra("confirmOrder", order);
                        setResult(AddOrderItem.ADD_ITEM_REQUEST, intent);
                        finish();
                    }
                    else{
                        orderItem.setTotal(total);
                        orderItem.setQuantity(qty);
                        Intent intent = new Intent();
                        intent.putExtra("editedOrderItem", orderItem);
                        setResult(MakeOrder.EDIT_ORDER_ITEM, intent);
                        finish();
                    }
                }
            }
        });

        btnRemoveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("editedOrderItem", orderItem);
                intent.putExtra("removeEditItem", true);
                setResult(MakeOrder.EDIT_ORDER_ITEM,intent);
                finish();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
