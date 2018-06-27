package app.pamelaiki.com;



import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


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
        public Location marketLoc;
        public int n;
        public TextView TextDistance;
        public float[] results=new  float[3];
        public float[] Bestdistance=new float[4];
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            listView = (ListView) findViewById(R.id.listView0);
            final ArrayList<sMarket> sMarketList = new ArrayList<>();
            sMarketList.add(new sMarket("Περιστερι", "23km", 39.03872, 84.53979));
            sMarketList.add(new sMarket("Αθηνα", "25km", 7.75277, -104.71680));
            sMarketList.add(new sMarket("Χαιδαρι", "33km", 67.08224, -127.61173));
            sMarketList.add(new sMarket("Νικαια", "40km", -20.48317, 32.82617));


            sMAdapter = new sMarketAdapter(this, sMarketList);
            listView.setAdapter(sMAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    sMarket selectedsMarket = sMarketList.get(i);
                    double selectedLatt = selectedsMarket.getlatt();
                    double selectedLong = selectedsMarket.getlongt();
                    String locationInfo = selectedLatt + "," + selectedLong;
                    Uri gmmIntentUri = Uri.parse("google.navigation:q="+locationInfo+"&mode=d" );
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                }
            });

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
                  final ArrayList<Double> distance=new ArrayList<>();
                          n=sMarketList.size();

                  for(int counter=0;counter<n;counter ++){
                        Location marketLoc=new Location("");
                      sMarket temp =sMarketList.get(counter);
                        marketLoc.setLatitude(temp.getlatt());
                      marketLoc.setLongitude(temp.getlongt());
                      Location.distanceBetween(deviceLatt,deviceLong,sMarketList.get(counter).getlatt(),sMarketList.get(counter).getlongt(),results);

                           Bestdistance[counter]=results[0];

                  }
                  for(int counter=0;counter<4;counter ++){
                            if(Bestdistance[counter]<Bestdistance[0]){
                                 float num;
                                 num=Bestdistance[0];
                                 Bestdistance[0]=Bestdistance[counter];
                            }
                  }
            TextDistance=(TextView)findViewById(R.id.distance);
                  TextDistance.setText(String.valueOf(Bestdistance[0]));
        }
    }



