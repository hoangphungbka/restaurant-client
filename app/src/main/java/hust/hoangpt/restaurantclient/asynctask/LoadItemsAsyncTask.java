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

import hust.hoangpt.restaurantclient.WaiterMainActivity;
import hust.hoangpt.restaurantclient.model.Categories;
import hust.hoangpt.restaurantclient.model.MenuItems;

public class LoadItemsAsyncTask extends AsyncTask<Void, Void, String> {

    private WaiterMainActivity waiterMainContext;

    public LoadItemsAsyncTask(WaiterMainActivity waiterMainContext) {
        this.waiterMainContext = waiterMainContext;
    }

    @Override
    protected String doInBackground(Void... params) {
        String url = "http://192.168.1.119/restaurant/api/pages/get-menu-items-by-category.json";
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
            JSONArray JSONCategories = JSONDocumentRoot.getJSONArray("categories");
            for (int i = 0; i < JSONCategories.length(); i++) {
                JSONObject JSONCategory = JSONCategories.getJSONObject(i);
                Categories category = new Categories(JSONCategory.getInt("id"), JSONCategory.getString("name"));

                JSONArray JSONMenuItems = JSONCategory.getJSONArray("menu_items");
                for (int j = 0; j < JSONMenuItems.length(); j++) {
                    JSONObject JSONMenuItem = JSONMenuItems.getJSONObject(j);
                    MenuItems menuItem = new MenuItems(JSONMenuItem.getInt("id"),JSONMenuItem.getInt("category_id"),
                            JSONMenuItem.getString("image"), (float)JSONMenuItem.getDouble("price"), JSONMenuItem.getString("name"));
                    category.addMenuItems(menuItem);
                }
                waiterMainContext.listCategories.add(category);
            }
            waiterMainContext.adapterSpinner.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
