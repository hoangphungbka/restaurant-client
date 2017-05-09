package hust.hoangpt.restaurantclient;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

import java.util.ArrayList;

import hust.hoangpt.restaurantclient.asynctask.LoadDetailsAsyncTask;
import hust.hoangpt.restaurantclient.asynctask.NotifyCompleteTask;
import hust.hoangpt.restaurantclient.model.OrderDetails;

public class ChefMainActivity extends AppCompatActivity {

    public Socket mSocket = SocketHelper.loadSocket();
    private NotificationCompat.Builder notificationBuilder;
    private static final int NOTIFICATION_ID = 1000;
    private static final int REQUEST_CODE = 100;

    public ListView listOrderDetails;
    public ArrayList<OrderDetails> arrayListOrderDetails = new ArrayList<>();
    public ArrayAdapter<OrderDetails> arrayAdapterOrderDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_chef_main);

        this.listOrderDetails = (ListView) this.findViewById(R.id.listOrderDetails);
        arrayAdapterOrderDetails = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_multiple_choice, arrayListOrderDetails);
        this.listOrderDetails.setAdapter(arrayAdapterOrderDetails);
        this.listOrderDetails.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

        this.notificationBuilder = new NotificationCompat.Builder(this);
        this.notificationBuilder.setAutoCancel(true);

        this.mSocket.on("receive-processing-request", onReceiveProcessingRequest);
        (new LoadDetailsAsyncTask(this)).execute();
    }

    private Emitter.Listener onReceiveProcessingRequest = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            setOnReceiveProcessingRequest();
        }
    };

    private void setOnReceiveProcessingRequest() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                (new LoadDetailsAsyncTask(ChefMainActivity.this)).execute();

                notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
                notificationBuilder.setTicker("This is a Ticker.");
                notificationBuilder.setWhen(System.currentTimeMillis());
                notificationBuilder.setContentTitle("This is a Title.");
                notificationBuilder.setContentText("This is a Content.");

                Intent intent = new Intent(ChefMainActivity.this, ChefMainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(ChefMainActivity.this,
                        REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                notificationBuilder.setContentIntent(pendingIntent);

                NotificationManager notificationManager = (NotificationManager)
                        getSystemService(Context.NOTIFICATION_SERVICE);
                Notification notification = notificationBuilder.build();
                notificationManager.notify(NOTIFICATION_ID, notification);
            }
        });
    }

    public void notifyProcessingComplete(View view) {
        SparseBooleanArray sparseBooleanArray = this.listOrderDetails.getCheckedItemPositions();
        for (int i = 0; i < sparseBooleanArray.size(); i++) {
            int position = sparseBooleanArray.keyAt(i);
            OrderDetails orderDetails = (OrderDetails) this.listOrderDetails.getItemAtPosition(position);
            (new NotifyCompleteTask(this)).execute(orderDetails.getId());
        }
    }
}
