package uniriotec.bruno.onibuscarioca.activity;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import uniriotec.bruno.onibuscarioca.R;
import uniriotec.bruno.onibuscarioca.controller.Controller;

public class InfoActivity extends AppCompatActivity {

    private DrawerLayout myDrawer;
    private ActionBarDrawerToggle myToggle;
    private Toolbar myToolbar;

    private Controller controller = new Controller();
    private ListView listaTarefas;
    private ArrayAdapter<String> itensAdaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_act);
        myDrawer = findViewById(R.id.drawerLayout);
        myToggle = new ActionBarDrawerToggle(this, myDrawer, R.string.open, R.string.close);
        myToolbar = findViewById(R.id.nav_action);

        setSupportActionBar(myToolbar);

        myDrawer.addDrawerListener(myToggle);
        myToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        menuNavigation();
    }

    private void menuNavigation() {

        NavigationView nv = findViewById(R.id.nv1);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case (R.id.local_lines):
                        Intent intentLocalLines = new Intent(InfoActivity.this, LocalLineActivity.class);
                        startActivity(intentLocalLines);
                        finish();
                        break;
                    case (R.id.nearby_buses):
                        Intent intentNearbyBuses = new Intent(InfoActivity.this, BusGpsActivity.class);
                        startActivity(intentNearbyBuses);
                        finish();
                        break;
                    case (R.id.config):
                        Intent intentConfig = new Intent(InfoActivity.this, ConfigActivity.class);
                        startActivity(intentConfig);
                        finish();
                        break;
                    case (R.id.infos):
                        Intent intentInfo = new Intent(InfoActivity.this, InfoActivity.class);
                        startActivity(intentInfo);
                        finish();
                        break;
                }

                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (myToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
