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


