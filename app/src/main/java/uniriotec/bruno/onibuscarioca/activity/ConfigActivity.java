package uniriotec.bruno.onibuscarioca.activity;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import uniriotec.bruno.onibuscarioca.R;
import uniriotec.bruno.onibuscarioca.helper.SharedPreferencesHelper;

public class ConfigActivity extends AppCompatActivity {

    private DrawerLayout myDrawer;
    private ActionBarDrawerToggle myToggle;
    private Toolbar myToolbar;
    private Button saveButton;
    private Button restoreButton;

    private TextView txtRepository;
    private SeekBar seekPrecision;

    private SharedPreferencesHelper sp;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.config_act);

        myDrawer = findViewById(R.id.drawerLayout);
        myToggle = new ActionBarDrawerToggle(this, myDrawer, R.string.open, R.string.close);
        myToolbar = findViewById(R.id.nav_action);
        setSupportActionBar(myToolbar);
        myDrawer.addDrawerListener(myToggle);
        myToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        menuNavigation();

        saveButton = findViewById(R.id.btSave);
        restoreButton = findViewById(R.id.btrestore);
        txtRepository = findViewById(R.id.txtRepository);
        seekPrecision = findViewById(R.id.sbPrecision);

        sp = new SharedPreferencesHelper(this.getApplicationContext());

        buttonsActions();
        loadValues();
    }

    private void loadValues()
    {
        txtRepository.setText(sp.RepositoryUrl);
        seekPrecision.setProgress(sp.Variance);
    }

    private void buttonsActions()
    {
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               sp.save(txtRepository.getText().toString(),seekPrecision.getProgress());

                Toast.makeText(ConfigActivity.this,"Configurações salvas com sucesso", Toast.LENGTH_SHORT).show();
            }
        });

        restoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sp.save("http://ikuzo.eastus.cloudapp.azure.com/v1/api/",100);

                txtRepository.setText(sp.RepositoryUrl);
                seekPrecision.setProgress(sp.Variance);
                Toast.makeText(ConfigActivity.this,"Configurações restauradas", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void menuNavigation() {

        NavigationView nv = findViewById(R.id.nv1);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case (R.id.local_lines):
                        Intent intentLocalLines = new Intent(ConfigActivity.this, LocalLineActivity.class);
                        startActivity(intentLocalLines);
                        finish();
                        break;
                    case (R.id.nearby_buses):
                        Intent intentNearbyBuses = new Intent(ConfigActivity.this, BusGpsActivity.class);
                        startActivity(intentNearbyBuses);
                        finish();
                        break;
                    case (R.id.config):
                        Intent intentConfig = new Intent(ConfigActivity.this, ConfigActivity.class);
                        startActivity(intentConfig);
                        finish();
                        break;
                    case (R.id.infos):
                        Intent intentInfo = new Intent(ConfigActivity.this, InfoActivity.class);
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