package uniriotec.bruno.onibuscarioca.controller;

import android.content.Context;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import uniriotec.bruno.onibuscarioca.model.BusNearby;
import uniriotec.bruno.onibuscarioca.model.LineDetails;
import uniriotec.bruno.onibuscarioca.model.LineIndex;
import uniriotec.bruno.onibuscarioca.repository.IkuzoRepository;

public class Controller {

    private IkuzoRepository repository;

    public LineDetails GetLineDetails(String lineId, Context context)  {

        repository = new IkuzoRepository(context);

        LineDetails line = new LineDetails();

        try {
             line = repository.GetLineDetails(lineId);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return line;
    }

    public ArrayList<LineIndex> GetLocalLines(double lat, double lon, Context context)  {

        repository = new IkuzoRepository(context);
        ArrayList lines = new ArrayList();

        try {
            lines = repository.GetLocalLines(lat, lon);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lines;

    }

    public ArrayList<BusNearby> GetNearbyBuses(double lat, double lon, Context context ) {

        repository = new IkuzoRepository(context);
        ArrayList buses = new ArrayList();

        try {
            buses = repository.GetNearbyBuses(lat, lon);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return buses;

    }

    public ArrayList<BusNearby> GetNearbyBuses(double lat, double lon, String line, Context context ) {

        repository = new IkuzoRepository(context);
        ArrayList buses = new ArrayList();

        try {
            buses = repository.GetNearbyBuses(lat, lon, line);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return buses;

    }

}
