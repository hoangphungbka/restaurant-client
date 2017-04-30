package hust.hoangpt.restaurantclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import hust.hoangpt.restaurantclient.asynctask.CreateOrderAsyncTask;
import hust.hoangpt.restaurantclient.asynctask.LoadTablesAsyncTask;
import hust.hoangpt.restaurantclient.model.DiningTables;
import hust.hoangpt.restaurantclient.model.Employees;

public class WaiterTablesActivity extends AppCompatActivity {

    public Employees employee;
    public ListView listViewTables;
    public ArrayList<DiningTables> arrayListTables = new ArrayList<>();
    public ArrayAdapter<DiningTables> adapterListViewTables;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_waiter_tables);

        Intent callerIntent = this.getIntent();
        Bundle employeeBundle = callerIntent.getBundleExtra("employeeBundle");
        this.employee = (Employees) employeeBundle.getSerializable("employee");

        this.listViewTables = (ListView) this.findViewById(R.id.listViewTables);
        adapterListViewTables = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayListTables);
        this.listViewTables.setAdapter(adapterListViewTables);
        this.registerForContextMenu(listViewTables);

        (new LoadTablesAsyncTask(this)).execute();
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
                Toast.makeText(this, "items", Toast.LENGTH_LONG).show();
                break;
            case R.id.menu_orders:
                Toast.makeText(this, "orders", Toast.LENGTH_LONG).show();
                break;
            case R.id.menu_tables:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Select An Action");
        menu.add(Menu.NONE, 1, Menu.NONE, "Create Order");
        super.onCreateContextMenu(menu, view, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case 1:
                DiningTables diningTable = adapterListViewTables.getItem(info.position);
                if (diningTable.getStatus() == 0) {
                    (new CreateOrderAsyncTask(this)).execute(diningTable.getTableNumber(), employee.getId());
                } else {
                    Toast.makeText(this, "Table in use", Toast.LENGTH_LONG).show();
                }
                break;
        }
        return super.onContextItemSelected(item);
    }
}
