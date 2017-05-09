package hust.hoangpt.restaurantclient;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

import java.util.ArrayList;

import hust.hoangpt.restaurantclient.asynctask.LoadOrdersAsyncTask;
import hust.hoangpt.restaurantclient.model.Employees;
import hust.hoangpt.restaurantclient.model.Orders;

public class WaiterOrdersActivity extends AppCompatActivity {

    public Employees employee;
    public ListView listViewOrders;
    public ArrayList<Orders> arrayListOrders = new ArrayList<>();
    public ArrayAdapter<Orders> adapterListViewOrders;

    public Socket mSocket = SocketHelper.loadSocket();
    private NotificationCompat.Builder notificationBuilder;
    private static final int NOTIFICATION_ID = 1000;
    private static final int REQUEST_CODE = 100;

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

        this.notificationBuilder = new NotificationCompat.Builder(this);
        this.notificationBuilder.setAutoCancel(true);

        (new LoadOrdersAsyncTask(null, null, this)).execute();
        this.mSocket.on("receive-processing-complete", onReceiveProcessingComplete);
        this.listViewOrders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                processClickedItemOnListView(position);
            }
        });
    }

    private void processClickedItemOnListView(int position) {
        Orders clickedOrder = (Orders) listViewOrders.getItemAtPosition(position);

        Bundle dataBundle = new Bundle();
        dataBundle.putSerializable("employee", employee);
        dataBundle.putSerializable("clickedOrder", clickedOrder);

        Intent waiterDetailsIntent = new Intent(this, WaiterDetailsActivity.class);
        waiterDetailsIntent.putExtra("dataBundle", dataBundle);
        this.startActivity(waiterDetailsIntent);
    }

    private Emitter.Listener onReceiveProcessingComplete = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            setOnReceiveProcessingComplete();
        }
    };

    private void setOnReceiveProcessingComplete() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                (new LoadOrdersAsyncTask(null, null, WaiterOrdersActivity.this)).execute();

                notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
                notificationBuilder.setTicker("This is a Ticker.");
                notificationBuilder.setWhen(System.currentTimeMillis());
                notificationBuilder.setContentTitle("This is a Title.");
                notificationBuilder.setContentText("This is a Content.");

                Intent intent = new Intent(WaiterOrdersActivity.this, WaiterOrdersActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(WaiterOrdersActivity.this,
                        REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                notificationBuilder.setContentIntent(pendingIntent);

                NotificationManager notificationManager = (NotificationManager)
                        getSystemService(Context.NOTIFICATION_SERVICE);
                Notification notification = notificationBuilder.build();
                notificationManager.notify(NOTIFICATION_ID, notification);
            }
        });
    }
}
