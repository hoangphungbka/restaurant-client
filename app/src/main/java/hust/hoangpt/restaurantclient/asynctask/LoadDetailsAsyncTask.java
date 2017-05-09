package hust.hoangpt.restaurantclient.asynctask;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import hust.hoangpt.restaurantclient.ChefMainActivity;
import hust.hoangpt.restaurantclient.R;
import hust.hoangpt.restaurantclient.model.OrderDetails;

public class LoadDetailsAsyncTask extends AsyncTask<Void, Void, String> {

    private ChefMainActivity chefMainContext;

    public LoadDetailsAsyncTask(ChefMainActivity chefMainContext) {
        this.chefMainContext = chefMainContext;
    }

    @Override
    protected String doInBackground(Void... params) {
        String url = "http://192.168.1.119/restaurant/api/pages/load-processing-order-details.json";
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
            JSONArray JSONOrderDetails = JSONDocumentRoot.getJSONArray("orderDetails");
            chefMainContext.arrayListOrderDetails.clear();
            for (int i = 0; i < JSONOrderDetails.length(); i++) {
                JSONObject JSONOrderDetail = JSONOrderDetails.getJSONObject(i);
                OrderDetails orderDetail = new OrderDetails(JSONOrderDetail.getInt("id"),
                        JSONOrderDetail.getInt("quantity"), JSONOrderDetail.getInt("status"),
                        JSONOrderDetail.getInt("amount"), JSONOrderDetail.getInt("item_price"),
                        JSONOrderDetail.getString("item_name"));
                chefMainContext.arrayListOrderDetails.add(orderDetail);
            }
            chefMainContext.arrayAdapterOrderDetails.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        super.onPostExecute(resultString);
    }
}
