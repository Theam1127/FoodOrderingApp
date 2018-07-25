package my.edu.tarc.foodorderingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class PaymentHistoryDetail extends AppCompatActivity {

    private TextView tvAmountPaid;
    private TextView tvChange;
    private TextView tvGrandTotal;
    private TextView tvOrderID;
    private TextView tvPaymentDate;
    private TextView tvPaymentTime;
    private TextView tvPaymentID;
    private TextView tvStaffID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_history_detail);

        tvAmountPaid = (TextView)findViewById(R.id.amountPaidTextDetail);
        tvChange = (TextView)findViewById(R.id.changeTextDetail);
        tvGrandTotal = (TextView)findViewById(R.id.grandTotalTextDetail);
        tvOrderID = (TextView)findViewById(R.id.orderIDTextDetail);
        tvPaymentDate = (TextView)findViewById(R.id.payDateDetail);
        tvPaymentTime = (TextView)findViewById(R.id.payTimeDetail);
        tvPaymentID = (TextView)findViewById(R.id.paymentIDTextDetail);
        tvStaffID = (TextView)findViewById(R.id.staffIDTextDetail);

        Intent intent = getIntent();

        String amtPaid = intent.getStringExtra("amt");
        String change = intent.getStringExtra("chg");
        String grangTotal = intent.getStringExtra("gt");
        String orderID = intent.getStringExtra("oid");
        String payDate = intent.getStringExtra("paydate");
        String payTime = intent.getStringExtra("paytime");
        String payID = intent.getStringExtra("payid");
        String staffID = intent.getStringExtra("sid");

        tvAmountPaid.setText(amtPaid);
        tvChange.setText(change);
        tvGrandTotal.setText(grangTotal);
        tvOrderID.setText(orderID);
        tvPaymentDate.setText(payDate);
        tvPaymentTime.setText(payTime);
        tvPaymentID.setText(payID);
        tvStaffID.setText(staffID);


    }
    public void cancel(View view){
        finish();
    }
}
