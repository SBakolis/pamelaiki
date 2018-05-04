package app.pamelaiki.com;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import android.widget.ListView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

        private ListView listView;
        private sMarketAdapter sMAdapter;
        private Calendar sCalendar;
        public String dayLongName;
        private TextView greetText;
         private  TextView gpsText;
    private FusedLocationProviderClient mFusedLocationClient;
    @Override

        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
           /* mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
             Location lastKnownLocation=manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);


            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                buildAlertMessageNoGps();
            }
*/




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
   /* private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }*/

}



