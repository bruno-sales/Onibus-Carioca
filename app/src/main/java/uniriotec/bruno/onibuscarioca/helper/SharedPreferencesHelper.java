package uniriotec.bruno.onibuscarioca.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesHelper {

    public String RepositoryUrl;
    public int Variance;
    private Context context;

    private static final String PREFERENCES_FILE = "preferences";
    private static final String DEFAULT_REPOSITORY_URL = "http://10.0.2.2:57408/v1/api/";
    private static final int DEFAULT_VARIANCE = 100;

    public SharedPreferencesHelper(Context con) {
        context = con;

        if (context != null) {
            SharedPreferences sp = context.getSharedPreferences(PREFERENCES_FILE, 0);

            if (sp.contains("url")) {
                String repo = sp.getString("url", DEFAULT_REPOSITORY_URL);
                RepositoryUrl = repo;
            } else {
                RepositoryUrl = DEFAULT_REPOSITORY_URL;
            }

            if (sp.contains("variance")) {
                int variance = sp.getInt("variance", DEFAULT_VARIANCE);
                Variance = variance;
            } else {
                Variance = DEFAULT_VARIANCE;
            }
        } else {
            RepositoryUrl = DEFAULT_REPOSITORY_URL;
            Variance = DEFAULT_VARIANCE;
        }
    }

    public void save(String url, int variance) {
        if (context != null) {
            SharedPreferences sp = context.getSharedPreferences(PREFERENCES_FILE, 0);
            SharedPreferences.Editor editor = sp.edit();

            editor.putString("url", url);
            editor.putInt("variance", variance);

            editor.apply();

            RepositoryUrl = url;
            Variance = variance;
        }
    }
}
