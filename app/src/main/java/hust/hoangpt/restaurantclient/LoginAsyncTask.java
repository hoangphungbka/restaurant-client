package hust.hoangpt.restaurantclient;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class LoginAsyncTask extends AsyncTask<String, Void, String> {

    private Context context;

    public LoginAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {
        String username = params[0], password = params[1], status = params[2];
        String url = "http://192.168.1.119/restaurant/api/pages/login.json";
        try {
            String data = URLEncoder.encode("username", "utf-8") + "=" + URLEncoder.encode(username, "utf-8");
            data += "&" + URLEncoder.encode("password", "utf-8") + "=" + URLEncoder.encode(password, "utf-8");
            data += "&" + URLEncoder.encode("status", "utf-8") + "=" + URLEncoder.encode(status, "utf-8");

            URLConnection urlConnection = (new URL(url)).openConnection();
            urlConnection.setDoOutput(true);

            OutputStreamWriter osWriter = new OutputStreamWriter(urlConnection.getOutputStream());
            osWriter.write(data); osWriter.flush();

            BufferedReader bReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder(); String line = "";
            while ((line = bReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String resultString) {
        try {
            JSONObject jsonRoot = new JSONObject(resultString);
            int result = jsonRoot.getInt("result");
            if (result == 1) { // login success
                JSONObject employeeObject = jsonRoot.getJSONObject("message");
                int status = employeeObject.getInt("status");
                if (status == 1) { // waiter role
                    Intent waiterMainIntent = new Intent(context, WaiterMainActivity.class);
                    context.startActivity(waiterMainIntent);
                } else if (status == 2) { // chef role
                    Intent chefMainIntent = new Intent(context, ChefMainActivity.class);
                    context.startActivity(chefMainIntent);
                }
            } else { // login failure
                String messageString = jsonRoot.getString("message");
                Toast.makeText(context, messageString, Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
