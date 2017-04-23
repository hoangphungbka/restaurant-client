package hust.hoangpt.restaurantclient.asynctask;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoadImageAsyncTask extends AsyncTask<String, Void, Bitmap> {

    private ImageView imageViewAvatar;

    public LoadImageAsyncTask(ImageView imageViewAvatar) {
        this.imageViewAvatar = imageViewAvatar;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        String imageUrl = params[0];
        InputStream inputStream = null;
        try {
            HttpURLConnection httpConnection = (HttpURLConnection) (new URL(imageUrl)).openConnection();
            httpConnection.setAllowUserInteraction(false);
            httpConnection.setInstanceFollowRedirects(true);
            httpConnection.setRequestMethod("GET");
            httpConnection.connect();

            int resCode = httpConnection.getResponseCode();
            if (resCode == HttpURLConnection.HTTP_OK) {
                inputStream = httpConnection.getInputStream();
            }
            return BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (bitmap != null) this.imageViewAvatar.setImageBitmap(bitmap);
    }
}
