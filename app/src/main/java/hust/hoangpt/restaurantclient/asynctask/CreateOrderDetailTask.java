package hust.hoangpt.restaurantclient.asynctask;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CreateOrderDetailTask extends AsyncTask<Integer, Void, String> {

    private Context context;

    public CreateOrderDetailTask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(Integer... params) {
        int itemId = params[0], orderId = params[1], quantity = params[2];
        String url = "http://192.168.1.119/restaurant/api/pages/create-order-detail/"
                + itemId + "/" + orderId + "/" + quantity + ".json";
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
                Toast.makeText(context, "Create Order Detail Failure.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "Create Order Detail Success.", Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        super.onPostExecute(resultString);
    }
}
