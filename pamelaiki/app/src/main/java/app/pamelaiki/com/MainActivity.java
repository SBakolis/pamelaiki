package app.pamelaiki.com;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

        private ListView listView;
        private sMarketAdapter sMAdapter;
        private Calendar sCalendar;
        public String dayLongName;
        private TextView greetText;
        private FusedLocationProviderClient mFusedLocationClient;
        @Override

        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                buildAlertMessageNoGps();
            }

            private void buildAlertMessageNoGps() {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                dialog.cancel();
                            }
                        });
                final AlertDialog alert = builder.create();
                alert.show();
            }


                mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
                mFusedLocationClient.getLastLocation()
                        .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                // Got last known location. In some rare situations this can be null.
                                if (location != null) {
                                    // Logic to handle location object
                                    Intent  error=new Intent(this,MainActivity.class);
                                    startActivity(error);
                                }
                            }
                        });


            listView = (ListView) findViewById(R.id.listView0);
            ArrayList<sMarket> sMarketList = new ArrayList<>();
            sMarketList.add(new sMarket("Περιστερι", "23km", 0, 0));
            sMarketList.add(new sMarket("Αθηνα", "25km", 0, 0));
            sMarketList.add(new sMarket("Χαιδαρι", "33km", 0, 0));
            sMarketList.add(new sMarket("Νικαια", "40km", 0, 0));


            sMAdapter = new sMarketAdapter(this, sMarketList);
            listView.setAdapter(sMAdapter);

            sCalendar = Calendar.getInstance();
            dayLongName = sCalendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH);

            greetText = (TextView) findViewById(R.id.greetText);
            greetText.setText("Καλημέρα, σήμερα " + dayLongName + " οι κοντινότερες αγορές είναι:");
        }
    }



