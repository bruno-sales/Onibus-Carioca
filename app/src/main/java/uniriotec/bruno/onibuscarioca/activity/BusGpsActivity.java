package uniriotec.bruno.onibuscarioca.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import uniriotec.bruno.onibuscarioca.R;
import uniriotec.bruno.onibuscarioca.controller.Controller;
import uniriotec.bruno.onibuscarioca.helper.PermissionHelper;
import uniriotec.bruno.onibuscarioca.model.BusNearby;
import uniriotec.bruno.onibuscarioca.model.LocationInformation;

public class BusGpsActivity extends AppCompatActivity {

    private String[] permissionsNeeded = new String[]{
            android.Manifest.permission.INTERNET,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_NETWORK_STATE
    };

    private DrawerLayout myDrawer;
    private ActionBarDrawerToggle myToggle;
    private Toolbar myToolbar;
    private SwipeRefreshLayout swipeRefresh;

    private Controller controller = new Controller();
    private LocationInformation location;

    private TextView lineTxt;
    private TextView localTxt;
    private ListView gpsListView;
    private Button btSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(uniriotec.bruno.onibuscarioca.R.layout.busgps_act);
        PermissionHelper.validatePermissions(1,this,permissionsNeeded);

        myDrawer = findViewById(R.id.drawerLayout);
        myToggle = new ActionBarDrawerToggle(this, myDrawer, R.string.open, R.string.close);
        myToolbar = findViewById(R.id.nav_action);
        setSupportActionBar(myToolbar);
        myDrawer.addDrawerListener(myToggle);
        myToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lineTxt = findViewById(R.id.linhaTxt);
        localTxt = findViewById(R.id.localTxt);
        gpsListView = findViewById(R.id.gpsListview);
        btSearch = findViewById(R.id.btSearch);
        swipeRefresh = findViewById(R.id.pullToRefresh);

        loadListView();

        gpsListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (gpsListView.getChildAt(0) != null) {
                    swipeRefresh.setEnabled(gpsListView.getFirstVisiblePosition() == 0 && gpsListView.getChildAt(0).getTop() == 0);
                }
            }
        });

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        loadListView();
                        swipeRefresh.setRefreshing(false);
                    }
                }, 1000);
            }
        });



        menuNavigation();
        buttonActions();
    }

    private void loadListView()
    {
        ArrayList<String> items = new ArrayList<>();
        ArrayList<BusNearby> buses = new ArrayList<>();

        Bundle extraData = getIntent().getExtras();
        location = getGpsInformation();

        if(location != null)
        {
            Geocoder geocoder;
            List<Address> addresses = new ArrayList<>();
            geocoder = new Geocoder(this, Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(addresses.isEmpty() == false)
            {
                String address = addresses.get(0).getAddressLine(0);
                localTxt.setText("Local atual: " + address);
            }

            if (isNetworkAvailable()) {
                if (extraData != null) {
                    String lineId = extraData.getString("lineId");
                    lineTxt.setText(lineId);
                    buses = controller.GetNearbyBuses(location.latitude, location.longitude, lineId, this.getApplicationContext());
                } else {
                    buses = controller.GetNearbyBuses(location.latitude, location.longitude, this.getApplicationContext());
                }
            }
        }

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

        gpsListView.setAdapter(itemsAdaptor);


        //Adding Itens
        for (BusNearby bus : buses) {

            String information = "Linha: "+bus.line + "\n" + "Ônibus: " + bus.bus + "\n"
                    +"Distância aproximada: " + bus.distance + " metros\n" ;

            items.add(information);
        }

        if (buses.isEmpty()) {
            items.add("Não foi possível obter as informações de GPS dos ônibus deste local. \nTente novamente");
        }

    }

    private void buttonActions()
    {
        btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!lineTxt.getText().toString().isEmpty()) {
                    Intent intentBusGps = new Intent(BusGpsActivity.this, BusGpsActivity.class);

                    intentBusGps.putExtra("lineId",lineTxt.getText().toString() );

                    startActivity(intentBusGps);
                    finish();
                }
            }
        });
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
                        Intent intentLocalLines = new Intent(BusGpsActivity.this, LocalLineActivity.class);
                        startActivity(intentLocalLines);
                        finish();
                        break;
                    case (R.id.nearby_buses):
                        Intent intentNearbyBuses = new Intent(BusGpsActivity.this, BusGpsActivity.class);
                        startActivity(intentNearbyBuses);
                        finish();
                        break;
                    case (R.id.config):
                        Intent intentConfig = new Intent(BusGpsActivity.this, ConfigActivity.class);
                        startActivity(intentConfig);
                        finish();
                        break;
                    case (R.id.infos):
                        Intent intentInfo = new Intent(BusGpsActivity.this, InfoActivity.class);
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
