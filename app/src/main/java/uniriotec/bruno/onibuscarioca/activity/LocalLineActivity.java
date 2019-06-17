package uniriotec.bruno.onibuscarioca.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import uniriotec.bruno.onibuscarioca.R;
import uniriotec.bruno.onibuscarioca.controller.Controller;
import uniriotec.bruno.onibuscarioca.helper.PermissionHelper;
import uniriotec.bruno.onibuscarioca.model.LineIndex;
import uniriotec.bruno.onibuscarioca.model.LocationInformation;

public class LocalLineActivity extends AppCompatActivity {

    private String[] permissionsNeeded = new String[]{
            android.Manifest.permission.INTERNET,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_NETWORK_STATE
    };

    private DrawerLayout myDrawer;
    private ActionBarDrawerToggle myToggle;
    private Toolbar myToolbar;

    private Controller controller = new Controller();
    private LocationInformation location;

    private ListView linesListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.localline_act);
        PermissionHelper.validatePermissions(1,this,permissionsNeeded);


        myDrawer = findViewById(R.id.drawerLayout);
        myToolbar = findViewById(R.id.nav_action);
        myToggle = new ActionBarDrawerToggle(this, myDrawer, R.string.open, R.string.close);
        setSupportActionBar(myToolbar);
        myDrawer.addDrawerListener(myToggle);
        myToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ArrayList<String> items = new ArrayList<>();
        ArrayList<LineIndex> lines = new ArrayList<>();

        location = getGpsInformation();

        if (location != null && isNetworkAvailable())
            lines = controller.GetLocalLines(location.latitude, location.longitude, this.getApplicationContext());

        linesListView = findViewById(R.id.listViewId);

        ArrayAdapter<String> itemsAdaptor = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_2,
                android.R.id.text2,
                items) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View view = super.getView(position, convertView, parent);
                TextView text = view.findViewById(android.R.id.text2);
                text.setTextColor(Color.BLACK);
                return view;
            }
        };

        linesListView.setAdapter(itemsAdaptor);

        //Adding Itens
        for (LineIndex line : lines) {
            items.add(line.line + " - " + line.name);
        }


        final ArrayList<LineIndex> finalLines = lines;

        if (!finalLines.isEmpty()) {
            linesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {

                    Intent intentBusGps = new Intent(LocalLineActivity.this, BusGpsActivity.class);

                    if (finalLines.size() >= index + 1) {
                        intentBusGps.putExtra("lineId", finalLines.get(index).line);
                    }

                    startActivity(intentBusGps);
                    finish();

                }
            });
        } else {
            items.add("Não foi possível obter as linhas deste local. \nTente novamente");
        }

        menuNavigation();

    }

    private LocationInformation getGpsInformation() {

        LocationInformation loc = new LocationInformation();

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }

        try {

            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            criteria.setPowerRequirement(Criteria.POWER_LOW);
            criteria.setCostAllowed(false);
            String bestProvider =lm.getBestProvider(criteria,true);

            lm.requestLocationUpdates(bestProvider, 2000, 10, new LocationListener() {
                public void onLocationChanged(Location location) {
                    location.setLongitude(location.getLongitude());
                    location.setLatitude(location.getLatitude());
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {
                }

                @Override
                public void onProviderEnabled(String s) {
                }

                @Override
                public void onProviderDisabled(String s) {
                }
            });

            Location location = lm.getLastKnownLocation(bestProvider);

            loc.longitude = location.getLongitude();
            loc.latitude = location.getLatitude();

        } catch (Exception e) {
            return null;
        }

        return loc;
    }

    private void menuNavigation() {

        NavigationView nv = findViewById(R.id.nv1);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case (R.id.local_lines):
                        Intent intentLocalLines = new Intent(LocalLineActivity.this, LocalLineActivity.class);
                        startActivity(intentLocalLines);
                        finish();
                        break;
                    case (R.id.nearby_buses):
                        Intent intentNearbyBuses = new Intent(LocalLineActivity.this, BusGpsActivity.class);
                        startActivity(intentNearbyBuses);
                        finish();
                        break;
                    case (R.id.config):
                        Intent intentConfig = new Intent(LocalLineActivity.this, ConfigActivity.class);
                        startActivity(intentConfig);
                        finish();
                        break;
                    case (R.id.infos):
                        Intent intentInfo = new Intent(LocalLineActivity.this, InfoActivity.class);
                        startActivity(intentInfo);
                        finish();
                        break;
                }

                return true;
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (myToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}