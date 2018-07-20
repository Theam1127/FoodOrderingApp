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
    private List<Menu> menuList;
    private Context context;

    public OrderListAdapter(List<Menu> menuList, Context context) {
        this.menuList = menuList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return menuList.size();
    }

    @Override
    public Object getItem(int pos){
        return menuList.get(pos);
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
        menuItem.setText(menuList.get(position).getName());
        itemQty.setText(""+5);
        itemTotalPrice.setText(String.format("%.2f",menuList.get(position).getPrice()));
        return view;
    }


}
