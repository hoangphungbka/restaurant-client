package hust.hoangpt.restaurantclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import hust.hoangpt.restaurantclient.asynctask.LoadOrderDetailsTask;
import hust.hoangpt.restaurantclient.model.Employees;
import hust.hoangpt.restaurantclient.model.OrderDetails;
import hust.hoangpt.restaurantclient.model.Orders;

public class WaiterDetailsActivity extends AppCompatActivity {

    public Employees employee;
    public Orders clickedOrder;
    public ListView listOrderDetails;
    public ArrayList<OrderDetails> arrayListOrderDetails = new ArrayList<>();
    public ArrayAdapter<OrderDetails> arrayAdapterOrderDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_waiter_details);

        Intent callerIntent = this.getIntent();
        Bundle dataBundle = callerIntent.getBundleExtra("dataBundle");
        this.employee = (Employees) dataBundle.getSerializable("employee");
        this.clickedOrder = (Orders) dataBundle.getSerializable("clickedOrder");

        this.listOrderDetails = (ListView) this.findViewById(R.id.list_order_details);
        arrayAdapterOrderDetails = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                arrayListOrderDetails);
        this.listOrderDetails.setAdapter(arrayAdapterOrderDetails);

        (new LoadOrderDetailsTask(arrayListOrderDetails, arrayAdapterOrderDetails))
                .execute(clickedOrder.getId());
    }

    public void sendProcessingRequest(View view) {
        Toast.makeText(this, "process", Toast.LENGTH_LONG).show();
    }

    public void sendPaymentRequest(View view) {
        Toast.makeText(this, "payment", Toast.LENGTH_LONG).show();
    }
}
