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

import hust.hoangpt.restaurantclient.ChefMainActivity;
import hust.hoangpt.restaurantclient.R;

public class NotifyCompleteTask extends AsyncTask<Integer, Void, String> {

    private ChefMainActivity chefMainContext;

    public NotifyCompleteTask(ChefMainActivity chefMainContext) {
        this.chefMainContext = chefMainContext;
    }

    @Override
    protected String doInBackground(Integer... params) {
        String url = "http://192.168.1.119/restaurant/api/pages/notify-processing-complete/" + params[0] + ".json";
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
            if (JSONDocumentRoot.getInt("result") == 1) {
                chefMainContext.mSocket.emit("notify-processing-complete", "chef notify processing complete.");
                (new LoadDetailsAsyncTask(chefMainContext)).execute();
                Toast.makeText(chefMainContext, "Gui Yeu Cau Thanh Cong", Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        super.onPostExecute(resultString);
    }
}
