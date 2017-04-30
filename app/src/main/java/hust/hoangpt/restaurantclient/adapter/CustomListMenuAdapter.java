package hust.hoangpt.restaurantclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import hust.hoangpt.restaurantclient.asynctask.LoadImageAsyncTask;
import hust.hoangpt.restaurantclient.model.MenuItems;
import hust.hoangpt.restaurantclient.R;

public class CustomListMenuAdapter extends BaseAdapter {

    private ArrayList<MenuItems> listMenuItems;
    private LayoutInflater layoutInflater;
    private Context context;

    public CustomListMenuAdapter(Context context, ArrayList<MenuItems> listMenuItems) {
        this.context = context;
        this.listMenuItems = listMenuItems;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listMenuItems.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.layout_custom_listitems, null);
            holder = new ViewHolder();
            holder.imageViewAvatar = (ImageView) convertView.findViewById(R.id.imageViewAvatar);
            holder.textViewName = (TextView) convertView.findViewById(R.id.textViewName);
            holder.textViewPrice = (TextView) convertView.findViewById(R.id.textViewPrice);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        MenuItems menuItems = listMenuItems.get(position);
        holder.textViewName.setText(menuItems.getName());
        holder.textViewPrice.setText("Price: " + menuItems.getPrice());
        String url = "http://192.168.1.119/restaurant/uploads/" + menuItems.getImage();
        (new LoadImageAsyncTask(holder.imageViewAvatar)).execute(url);
        return convertView;
    }

    @Override
    public Object getItem(int position) {
        return listMenuItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static private class ViewHolder {
        ImageView imageViewAvatar;
        TextView textViewName;
        TextView textViewPrice;
    }
}
