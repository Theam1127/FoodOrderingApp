package my.edu.tarc.foodorderingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PaymentHistoryAdapter extends ArrayAdapter<PaymentHistoryClass> {

    private Context mContext;
    private List<PaymentHistoryClass> paymentHistory;
    private String date;


    public PaymentHistoryAdapter(@NonNull Context context, ArrayList<PaymentHistoryClass> list) {
        super(context,0,list);
        mContext = context;
        paymentHistory = list;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if(view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.payment_history_list_view,parent,false);
        }
        PaymentHistoryClass simpleData = paymentHistory.get(position);


        TextView tvStaffID = (TextView)view.findViewById(R.id.staffIDTextDetail);
        TextView tvOrderID = (TextView)view.findViewById(R.id.orderIDTextDetail);
        TextView tvPaymentDate = (TextView)view.findViewById(R.id.paymentDateText);
        TextView tvPaymentID = (TextView)view.findViewById(R.id.paymentIDTextDetail);
        TextView tvGrandTotal = (TextView)view.findViewById(R.id.grandTotalTextDetail);
        TextView tvAmountPaid = (TextView)view.findViewById(R.id.amountPaidTextDetail);
        TextView tvChange = (TextView)view.findViewById(R.id.changeText);
        TextView tvPaymentTime = (TextView)view.findViewById(R.id.paymentTimeText);



        tvStaffID.setText(simpleData.getStaffID());
        tvOrderID.setText(""+simpleData.getOrderID());
        tvPaymentDate.setText(simpleData.getDate());
        tvPaymentTime.setText(simpleData.getTime());
        tvPaymentID.setText(""+simpleData.getPaymentID());
        tvGrandTotal.setText(""+ simpleData.getGrandTotal());
        tvAmountPaid.setText(""+simpleData.getAmountPaid());
        tvChange.setText(""+ simpleData.getChange());


        return view;
    }
}
