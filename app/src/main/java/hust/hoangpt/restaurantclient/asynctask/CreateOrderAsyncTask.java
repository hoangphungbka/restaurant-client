package hust.hoangpt.restaurantclient.asynctask;

import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import hust.hoangpt.restaurantclient.WaiterTablesActivity;

public class CreateOrderAsyncTask extends AsyncTask<Integer, Void, String> {

    private WaiterTablesActivity waiterTablesContext;

    public CreateOrderAsyncTask(WaiterTablesActivity waiterTablesContext) {
        this.waiterTablesContext = waiterTablesContext;
    }

    @Override
    protected String doInBackground(Integer... params) {
        int tableNumber = params[0]; int employeeId = params[1];
        String url = "http://192.168.1.119/restaurant/api/pages/create-order-for-table/"
                + tableNumber + "/" + employeeId + ".json";
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
            int result = JSONDocumentRoot.getInt("result");

            if (result == 0) {
                Toast.makeText(waiterTablesContext, "Create Order Failure.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(waiterTablesContext, "Create Order Success.", Toast.LENGTH_LONG).show();
                (new LoadTablesAsyncTask(waiterTablesContext)).execute();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        super.onPostExecute(resultString);
    }
}
