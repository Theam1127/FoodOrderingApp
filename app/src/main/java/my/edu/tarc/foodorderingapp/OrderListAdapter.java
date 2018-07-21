package my.edu.tarc.foodorderingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class OrderListAdapter extends BaseAdapter implements ListAdapter {
    private List<Orders> orderList;
    private List<String> menuNames;
    private Context context;

    public OrderListAdapter(List<String> menuNames, List<Orders> orderList, Context context) {
        this.menuNames=menuNames;
        this.orderList = orderList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return orderList.size();
    }

    @Override
    public Object getItem(int pos){
        return orderList.get(pos);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent){
        View view = convertView;
        if(view==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.order_list_view,null);
        }
        TextView menuItem = (TextView)view.findViewById(R.id.tvMenuItem);
        TextView itemQty = (TextView)view.findViewById(R.id.tvQuantity);
        TextView itemTotalPrice = (TextView)view.findViewById(R.id.tvPrice);
        menuItem.setText(menuNames.get(position));
        itemQty.setText(""+orderList.get(position).getQuantity());
        itemTotalPrice.setText(String.format("%.2f",orderList.get(position).getTotal()));
        return view;
    }


}
