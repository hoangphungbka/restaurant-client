package hust.hoangpt.restaurantclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import hust.hoangpt.restaurantclient.asynctask.LoadOrdersAsyncTask;
import hust.hoangpt.restaurantclient.model.Employees;
import hust.hoangpt.restaurantclient.model.Orders;

public class WaiterOrdersActivity extends AppCompatActivity {

    public Employees employee;
    public ListView listViewOrders;
    public ArrayList<Orders> arrayListOrders = new ArrayList<>();
    public ArrayAdapter<Orders> adapterListViewOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_waiter_orders);

        Intent callerIntent = this.getIntent();
        Bundle employeeBundle = callerIntent.getBundleExtra("employeeBundle");
        this.employee = (Employees) employeeBundle.getSerializable("employee");

        this.listViewOrders = (ListView) this.findViewById(R.id.list_orders);
        adapterListViewOrders = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayListOrders);
        this.listViewOrders.setAdapter(adapterListViewOrders);

        (new LoadOrdersAsyncTask(null, null, this)).execute();
        this.listViewOrders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Orders clickedOrder = (Orders) listViewOrders.getItemAtPosition(position);
                Bundle dataBundle = new Bundle();
                dataBundle.putSerializable("employee", employee);
                dataBundle.putSerializable("clickedOrder", clickedOrder);
                Intent waiterDetailsIntent = new Intent(WaiterOrdersActivity.this, WaiterDetailsActivity.class);
                waiterDetailsIntent.putExtra("dataBundle", dataBundle);
                WaiterOrdersActivity.this.startActivity(waiterDetailsIntent);
            }
        });
    }
}
