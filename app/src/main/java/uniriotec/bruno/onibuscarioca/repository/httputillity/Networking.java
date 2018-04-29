package uniriotec.bruno.onibuscarioca.repository.httputillity;

import android.os.AsyncTask;
import android.util.Log;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Networking extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {

        String stringUrl = params[0];
        String result = null;

        try {
            URL myUrl = new URL(stringUrl);

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(4, TimeUnit.SECONDS)
                    .readTimeout(4, TimeUnit.SECONDS)
                    .build();

            Request request = new Request.Builder()
                    .url(myUrl)
                    .build();

            Response response = client.newCall(request).execute();

            if (response.code() == 200) {
                result = response.body().string();
            } else {
                Log.e("HTTPError", "Code: " + response.code() + " Message:" + response.message());
            }

        } catch (Exception e) {
            Log.e("HTTPException", "Exception: " + e.getMessage() + " " + e.getLocalizedMessage());
        }

        return result;
    }

    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }

}
