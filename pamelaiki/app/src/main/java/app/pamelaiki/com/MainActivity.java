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
import java.util.Collections;
import java.util.Comparator;
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

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            listView = (ListView) findViewById(R.id.listView0);
            final ArrayList<sMarket> sMarketList = new ArrayList<>();

            sMarketList.add(new sMarket("Περιστερι", 00.0, 39.03872, 84.53979));
            sMarketList.add(new sMarket("Αθηνα", 00.0, 7.75277, -104.71680));
            sMarketList.add(new sMarket("Χαιδαρι", 00.0, 67.08224, -127.61173));
            sMarketList.add(new sMarket("Νικαια", 00.0, -20.48317, 32.82617));
            sMarketList.add(new sMarket("Νίκαια 2η",00.0,37.967856,23.635297));
             final ArrayList<sMarket>  BestMarketList= new ArrayList<>();//h lista p tha emfanizetai me tis kaluteres 4,to allaksa kai sto adapter
            sMAdapter = new sMarketAdapter(this, BestMarketList);
            listView.setAdapter(sMAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    sMarket selectedsMarket = BestMarketList.get(i);
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
                                deviceLatt=location.getLatitude();
                                deviceLong=location.getLongitude();
                                listView.setAdapter(sMAdapter);

            locationtest.setText("Recent Location " + deviceLong +","+ deviceLatt );
                      n=sMarketList.size();
                  //vriskei to distance kai to vazei sto antistoixo tou sMarket
                  for(int counter=0;counter<n;counter ++){



                      Location.distanceBetween(deviceLatt,deviceLong,sMarketList.get(counter).getlatt(),sMarketList.get(counter).getlongt(),results);

                           sMarketList.get(counter).setsMarketDistance(results[0]);

                  }
                      //sortarei ta sMarket
                  for(int counter=0;counter<n;counter ++) {
                      for (int j = counter + 1; j < n; j++) {
                          if (sMarketList.get(counter).getsMarketDistance() > sMarketList.get(j).getsMarketDistance()) {

                              sMarket num = sMarketList.get(counter);
                             sMarketList.set(counter,sMarketList.get(j));
                              sMarketList.set(j,num);
                          }
                      }
                  }
                  //gemizei thn BestMarket me ta kontinotera
                   for(int counter=0;counter<4;counter++){

                        sMarket temp=sMarketList.get(counter);
                         BestMarketList.add(temp);
                   }
            //TextDistance=(TextView)findViewById(R.id.distance);
            //TextDistance.setText(String.valueOf(marketLoc.getLatitude()));
                            }

                        }

                    });
        }
    }



