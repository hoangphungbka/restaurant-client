package hust.hoangpt.restaurantclient.asynctask;

import android.os.AsyncTask;
import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import hust.hoangpt.restaurantclient.R;
import hust.hoangpt.restaurantclient.WaiterOrdersActivity;
import hust.hoangpt.restaurantclient.model.Orders;

public class LoadOrdersAsyncTask extends AsyncTask<Void, Void, String> {

    private ArrayList<Orders> arrayListOrders;
    private ArrayAdapter<Orders> arrayAdapterOrders;
    private WaiterOrdersActivity waiterOrdersContext;

    public LoadOrdersAsyncTask(ArrayList<Orders> arrayListOrders,
                               ArrayAdapter<Orders> arrayAdapterOrders,
                               WaiterOrdersActivity waiterOrdersContext) {
        this.arrayListOrders = arrayListOrders;
        this.arrayAdapterOrders = arrayAdapterOrders;
        this.waiterOrdersContext = waiterOrdersContext;
    }

    @Override
    protected String doInBackground(Void... params) {
        String url = "http://192.168.1.119/restaurant/api/pages/load-serving-orders.json";
        try {
            HttpURLConnection httpConnection = (HttpURLConnection) (new URL(url)).openConnection();
            httpConnection.setAllowUserInteraction(false);
            httpConnection.setInstanceFollowRedirects(true);
            httpConnection.setRequestMethod("GET");
            httpConnection.connect();

            int resCode = httpConnection.getResponseCode();
            if (resCode == HttpURLConnection.HTTP_OK) {
                BufferedReader bReader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder(); String line;
                while ((line = bReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                return stringBuilder.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String resultString) {
        try {
            JSONObject JSONDocumentRoot = new JSONObject(resultString);
            JSONArray JSONOrders = JSONDocumentRoot.getJSONArray("servingOrders");
            if (waiterOrdersContext == null) arrayListOrders.clear();
            for (int i = 0; i < JSONOrders.length(); i++) {
                JSONObject JSONOrder = JSONOrders.getJSONObject(i);
                Orders order = new Orders(JSONOrder.getInt("id"), JSONOrder.getInt("total_amount"),
                        JSONOrder.getInt("table_number"), JSONOrder.getInt("employee_id"),
                        JSONOrder.getInt("status"));
                if (waiterOrdersContext == null) arrayListOrders.add(order);
                else waiterOrdersContext.arrayListOrders.add(order);
            }
            if (waiterOrdersContext == null) arrayAdapterOrders.notifyDataSetChanged();
            else waiterOrdersContext.adapterListViewOrders.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        super.onPostExecute(resultString);
    }
}
