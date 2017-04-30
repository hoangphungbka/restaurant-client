package hust.hoangpt.restaurantclient;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import hust.hoangpt.restaurantclient.adapter.CustomListMenuAdapter;
import hust.hoangpt.restaurantclient.asynctask.CreateOrderDetailTask;
import hust.hoangpt.restaurantclient.asynctask.LoadItemsAsyncTask;
import hust.hoangpt.restaurantclient.asynctask.LoadOrdersAsyncTask;
import hust.hoangpt.restaurantclient.model.Categories;
import hust.hoangpt.restaurantclient.model.Employees;
import hust.hoangpt.restaurantclient.model.MenuItems;
import hust.hoangpt.restaurantclient.model.Orders;

public class WaiterMainActivity extends AppCompatActivity {

    public Employees employee;

    public ListView listViewItems;
    public Spinner spinnerCategories;

    public ArrayList<Categories> listCategories = new ArrayList<>();
    public ArrayAdapter<Categories> adapterSpinner = null;

    public ArrayList<MenuItems> listMenuItems = new ArrayList<>();
    public CustomListMenuAdapter adapterListView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_waiter_main);

        this.listViewItems = (ListView) this.findViewById(R.id.listViewItems);
        this.spinnerCategories = (Spinner) this.findViewById(R.id.spinnerCategories);

        Intent callerIntent = this.getIntent();
        Bundle employeeBundle = callerIntent.getBundleExtra("employeeBundle");
        this.employee = (Employees) employeeBundle.getSerializable("employee");

        adapterSpinner = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listCategories);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        this.spinnerCategories.setAdapter(adapterSpinner);

        adapterListView = new CustomListMenuAdapter(this, listMenuItems);
        this.listViewItems.setAdapter(adapterListView);

        (new LoadItemsAsyncTask(WaiterMainActivity.this)).execute();
        this.addEventForFormWidgets();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.menu_waiter_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_items:
                break;
            case R.id.menu_orders:
                Toast.makeText(this, "orders", Toast.LENGTH_LONG).show();
                break;
            case R.id.menu_tables:
                Bundle employeeBundle = new Bundle();
                employeeBundle.putSerializable("employee", employee);
                Intent waiterTablesIntent = new Intent(this, WaiterTablesActivity.class);
                waiterTablesIntent.putExtra("employeeBundle", employeeBundle);
                this.startActivity(waiterTablesIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addEventForFormWidgets() {
        spinnerCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                listMenuItems.clear();
                listMenuItems.addAll(listCategories.get(position).getListMenuItems());
                adapterListView.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        listViewItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                MenuItems menuItem = (MenuItems) listViewItems.getItemAtPosition(position);
                processItemLongClickOnListView(menuItem);
                return false;
            }
        });
    }

    private void processItemLongClickOnListView(final MenuItems menuItem)
    {
        LayoutInflater inflater = this.getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.layout_custom_dialog, null);
        final Spinner spinnerOrders = (Spinner) alertLayout.findViewById(R.id.spinner_orders);
        final EditText editQuantity = (EditText) alertLayout.findViewById(R.id.edt_quantity);

        final ArrayList<Orders> arrayListOrders = new ArrayList<>();
        final ArrayAdapter<Orders> arrayAdapterOrders = new ArrayAdapter<>(getBaseContext(),
                android.R.layout.simple_spinner_item, arrayListOrders);
        arrayAdapterOrders.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinnerOrders.setAdapter(arrayAdapterOrders);

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Create Order Detail."); alert.setView(alertLayout);

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getBaseContext(), "Cancel Create.", Toast.LENGTH_LONG).show();
            }
        });

        alert.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Orders selectedOrder = (Orders) spinnerOrders.getSelectedItem();
                int quantity = Integer.parseInt(editQuantity.getText().toString());
                (new CreateOrderDetailTask(getBaseContext())).execute(menuItem.getId(),
                        selectedOrder.getId(), quantity);
            }
        });

        AlertDialog dialog = alert.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                (new LoadOrdersAsyncTask(arrayListOrders, arrayAdapterOrders)).execute();
            }
        });
        dialog.show();
    }
}
