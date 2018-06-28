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
        public String dayLongNameGreek;
        private TextView greetText;
        private TextView locationtest;
        private FusedLocationProviderClient mFusedLocationClient;
        public double  deviceLatt;
        public double deviceLong;
        public Location marketLoc;
        public Location lastPlace;
        public int n;
        public TextView TextDistance;
        public float[] results=new  float[3];
        final ArrayList<sMarket> sMarketList = new ArrayList<>();
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            listView = (ListView) findViewById(R.id.listView0);


            sMarketList.add(new sMarket("Περιστερι", 00.0, 39.03872, 84.53979));
            sMarketList.add(new sMarket("Αθηνα", 00.0, 37.943454, 23.618762));
            sMarketList.add(new sMarket("Χαιδαρι", 00.0, 38.001470, 23.663558));
            sMarketList.add(new sMarket("Νικαια", 00.0, 37.986537, 23.629464));
            sMarketList.add(new sMarket("Νίκαια 2η",00.0,37.999976,23.641376));
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


            switch(dayLongName)
            {
                case "Monday" : dayLongNameGreek = "Δευτέρα";
                break;

                case "Tuesday" : dayLongNameGreek = "Τρίτη";
                break;

                case "Wednesday" : dayLongNameGreek = "Τετάρτη";
                break;

                case "Thursday" : dayLongNameGreek = "Πέμπτη";
                break;

                case "Friday" : dayLongNameGreek = "Παρασκευή";
                break;

                case "Saturday" : dayLongNameGreek = "Σάββατο";
                break;

                case "Sunday" : dayLongNameGreek = "Κυριακή";
                break;

                default : break;
            }

            greetText = (TextView) findViewById(R.id.greetText);
            greetText.setText("Καλημέρα, σήμερα " + dayLongNameGreek + " οι κοντινότερες αγορές είναι:");

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
                                deviceLong = location.getLongitude();
                                deviceLatt = location.getLatitude();
                                locationtest.setText("Recent Location " + deviceLong + "," + deviceLatt);

                                n=sMarketList.size();
                                //vriskei to distance kai to vazei sto antistoixo tou sMarket
                                for(int counter=0;counter<n;counter ++){
                                    Location marketLoc=new Location("");


                                    Location.distanceBetween(deviceLatt,deviceLong,sMarketList.get(counter).getlatt(),sMarketList.get(counter).getlongt(),results);

                                    sMarketList.get(counter).setsMarketDistance(results[0]/1000);

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
                            }
                        }
                    });



            TextDistance=(TextView)findViewById(R.id.distance);
                  TextDistance.setText(String.valueOf(sMarketList.get(4).getsMarketDistance()));


        }
    }



