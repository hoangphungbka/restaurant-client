package hust.hoangpt.restaurantclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import hust.hoangpt.restaurantclient.adapter.CustomListMenuAdapter;
import hust.hoangpt.restaurantclient.asynctask.LoadItemsAsyncTask;
import hust.hoangpt.restaurantclient.model.Categories;
import hust.hoangpt.restaurantclient.model.Employees;
import hust.hoangpt.restaurantclient.model.MenuItems;

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
                Toast.makeText(WaiterMainActivity.this, "items", Toast.LENGTH_LONG).show();
                break;
            case R.id.menu_orders:
                Toast.makeText(WaiterMainActivity.this, "orders", Toast.LENGTH_LONG).show();
                break;
            case R.id.menu_tables:
                Toast.makeText(WaiterMainActivity.this, "tables", Toast.LENGTH_LONG).show();
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

        listViewItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MenuItems menuItem = (MenuItems) listViewItems.getItemAtPosition(position);
                Toast.makeText(WaiterMainActivity.this, menuItem.getName(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
