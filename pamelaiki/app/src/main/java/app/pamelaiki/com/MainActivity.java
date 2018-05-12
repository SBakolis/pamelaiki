package app.pamelaiki.com;

import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import android.widget.ListView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static com.google.android.gms.location.LocationServices.*;

public class MainActivity extends AppCompatActivity {

        private ListView listView;
        private sMarketAdapter sMAdapter;
        private Calendar sCalendar;
        public String dayLongName;
        private TextView greetText;
        private TextView locationtest;
        private FusedLocationProviderClient mFusedLocationClient;
        public double deviceLatt;
        public double deviceLong;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

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

            locationtest = findViewById(R.id.locationtest);

            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {

                ActivityCompat.requestPermissions( this, new String[] {  android.Manifest.permission.ACCESS_COARSE_LOCATION  },
                        1);
            }
            
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                deviceLong = location.getLatitude();
                                deviceLatt = location.getLongitude();
                                locationtest.setText("Recent Location " + deviceLong +","+ deviceLatt );
                            }
                        }

                    });


        }
    }



