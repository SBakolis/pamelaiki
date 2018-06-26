package app.pamelaiki.com;



import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;



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
            final ArrayList<sMarket> sMarketList = new ArrayList<>();
            sMarketList.add(new sMarket("Περιστερι", "23km", 0, 0));
            sMarketList.add(new sMarket("Αθηνα", "25km", 0, 0));
            sMarketList.add(new sMarket("Χαιδαρι", "33km", 0, 0));
            sMarketList.add(new sMarket("Νικαια", "40km", 0, 0));


            sMAdapter = new sMarketAdapter(this, sMarketList);
            listView.setAdapter(sMAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    sMarket selectedsMarket = sMarketList.get(i);
                    float selectedLatt = selectedsMarket.getlatt();
                    float selectedLong = selectedsMarket.getlongt();
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


        }
    }



