package hust.hoangpt.restaurantclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import hust.hoangpt.restaurantclient.model.Employees;
import hust.hoangpt.restaurantclient.model.Orders;

public class WaiterDetailsActivity extends AppCompatActivity {

    public Employees employee;
    public Orders clickedOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_waiter_details);

        Intent callerIntent = this.getIntent();
        Bundle dataBundle = callerIntent.getBundleExtra("dataBundle");
        this.employee = (Employees) dataBundle.getSerializable("employee");
        this.clickedOrder = (Orders) dataBundle.getSerializable("clickedOrder");


    }
}
