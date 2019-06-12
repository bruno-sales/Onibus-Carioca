package uniriotec.bruno.onibuscarioca.repository;


import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import uniriotec.bruno.onibuscarioca.helper.SharedPreferencesHelper;
import uniriotec.bruno.onibuscarioca.model.BusNearby;
import uniriotec.bruno.onibuscarioca.model.LineDetails;
import uniriotec.bruno.onibuscarioca.model.LineIndex;
import uniriotec.bruno.onibuscarioca.repository.httputillity.Networking;

public class IkuzoRepository {

    private Context context;
    private String endpoint;
    private int distance;

    public IkuzoRepository(Context con)
    {
        context = con;
        SharedPreferencesHelper sp = new SharedPreferencesHelper(context);

        endpoint = sp.RepositoryUrl;
        distance = sp.Variance * 5;
    }

    public LineDetails GetLineDetails(String lineId) throws ExecutionException, InterruptedException{

        String myUrl = endpoint + "lines/" + lineId;

        Networking network = new Networking();
        String result = network.execute(myUrl).get();

        if (result == null)
            return new LineDetails();

        Gson gson = new Gson();

        LineDetails line = gson.fromJson(result, LineDetails.class);

        return line;
    }

    public ArrayList<LineIndex> GetLocalLines(double lat, double lon) throws ExecutionException, InterruptedException {

        String myUrl = endpoint + "lines/local?lat=" + lat + "&lon=" + lon+"&distance="+distance;

        Networking network = new Networking();
        String result = network.execute(myUrl).get();

        if (result == null)
            return new ArrayList<>();

        Gson gson = new Gson();

        Type listType = new TypeToken<ArrayList<LineIndex>>() {
        }.getType();
        ArrayList<LineIndex> lineIndexes = gson.fromJson(result, listType);

        return lineIndexes;
    }

    public ArrayList<BusNearby> GetNearbyBuses(double lat, double lon) throws ExecutionException, InterruptedException {

        String myUrl = endpoint + "modals/nearby?lat=" + lat + "&lon=" + lon+"&distance="+distance;

        Networking network = new Networking();
        String result = network.execute(myUrl).get();

        if (result == null)
            return new ArrayList<>();

        Gson gson = new Gson();

        Type listType = new TypeToken<ArrayList<BusNearby>>() {
        }.getType();
        ArrayList<BusNearby> buses = gson.fromJson(result, listType);

        return buses;
    }

    public ArrayList<BusNearby> GetNearbyBuses(double lat, double lon, String line) throws ExecutionException, InterruptedException {

        String myUrl = endpoint + "modals/nearby?lat=" + lat + "&lon=" + lon + "&line=" + line+"&distance="+distance;

        Networking network = new Networking();
        String result = network.execute(myUrl).get();

        if (result == null)
            return new ArrayList<>();

        Gson gson = new Gson();

        Type listType = new TypeToken<ArrayList<BusNearby>>(){}.getType();
        ArrayList<BusNearby> buses = gson.fromJson(result, listType);

        return buses;
    }
}
