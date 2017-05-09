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

import hust.hoangpt.restaurantclient.R;
import hust.hoangpt.restaurantclient.WaiterTablesActivity;
import hust.hoangpt.restaurantclient.model.DiningTables;

public class LoadTablesAsyncTask extends AsyncTask<Void, Void, String> {

    private WaiterTablesActivity waiterTablesContext;

    public LoadTablesAsyncTask(WaiterTablesActivity waiterTablesContext) {
        this.waiterTablesContext = waiterTablesContext;
    }

    @Override
    protected String doInBackground(Void... params) {
        String url = "http://192.168.1.119/restaurant/api/pages/get-dining-tables-status.json";
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
            JSONArray JSONDiningTables = JSONDocumentRoot.getJSONArray("diningTables");
            waiterTablesContext.arrayListTables.clear();
            for (int i = 0; i < JSONDiningTables.length(); i++) {
                JSONObject JSONDiningTable = JSONDiningTables.getJSONObject(i);
                DiningTables diningTable = new DiningTables(JSONDiningTable.getInt("table_number"),
                        JSONDiningTable.getInt("chairs_count"), JSONDiningTable.getInt("status"));
                waiterTablesContext.arrayListTables.add(diningTable);
            }
            waiterTablesContext.adapterListViewTables.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
