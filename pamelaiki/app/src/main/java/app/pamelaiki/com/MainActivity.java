package app.pamelaiki.com;



import android.Manifest;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;


import android.widget.ListView;

import android.widget.TextView;


import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.ConnectionResult;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;



import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import java.util.Locale;

import static android.location.LocationManager.GPS_PROVIDER;

import static android.location.LocationManager.NETWORK_PROVIDER;


import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;



public class    MainActivity extends AppCompatActivity {

    private  View main;
    private ListView listView;
    private sMarketAdapter sMAdapter;
    private Calendar sCalendar;
    public String dayLongName;
    public String dayLongNameGreek;
    private TextView greetText;
    private TextView Nomarket;
    private FusedLocationProviderClient mFusedLocationClient;
    public double deviceLatt;
    public double deviceLong;
    public TextView TextDistance;
    public TextView locationtest;
    public final ArrayList<sMarket> sMarketList = new ArrayList<>();
    public final ArrayList<sMarket> BestMarketList = new ArrayList<>();//h lista p tha emfanizetai me tis kaluteres 4,to allaksa kai sto adapter
    public int n;
    public LocationManager manager;
    private LocationCallback mLocationCallback;
    public LocationRequest mLocationRequest;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    public boolean hasFailed = false;
    public String Greet;
    public float[] results = new float[3];
    private AlertDialog.Builder builder;
    public AlertDialog dialog;
    public int time;
    private AlertDialog.Builder buildermaps;
    public AlertDialog dialogmaps;

    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, "ca-app-pub-6028798031014902~4137361226");

        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-6028798031014902/2891303134");

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
        }

        listView = (ListView) findViewById(R.id.listView0);

        
        builder = new AlertDialog.Builder(MainActivity.this);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent gpsOptionsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(gpsOptionsIntent);
                //finish();
                //startActivity(getIntent());
            }
        });

        builder.setMessage("Για τη σωστή λειτουργία της εφαρμογής θα πρέπει να ενεργοποιήσετε το GPS, πατήστε ΟΚ για ενεργοποίηση.")
                .setTitle("Ενεργοποιήστε το GPS.");

        builder.setNegativeButton("Έξοδος", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                System.exit(0);
            }
        });
        dialog = builder.create();

        buildermaps = new AlertDialog.Builder(MainActivity.this);

        buildermaps.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Uri gmmIntentUri = Uri.parse("geo:0,0?q= Πλατεία Συντάγματος , Αθήνα");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        buildermaps.setMessage("Η τοποθεσία της συσκευής σας δεν έχει ενημερωθεί, πατήστε ΟΚ για να ενημερώσετε την τοποθεσία σας μέσω του Google Maps ή Έξοδος για έξοδο από την εφαρμογή.")
                .setTitle("Δεν υπάρχει πρόσφατη τοποθεσία.");

        buildermaps.setNegativeButton("Έξοδος", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                System.exit(0);
            }
        });
        dialogmaps = buildermaps.create();

        LocationManager manager=(LocationManager) getSystemService(LOCATION_SERVICE);
        if(manager.isProviderEnabled(NETWORK_PROVIDER) || manager.isProviderEnabled(GPS_PROVIDER)){
            createLocationRequest();
            locateAndSort();
        }
        else {
            hasFailed=true;
            dialog.show();
        }

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // All location settings are satisfied. The client can initialize
                // location requests here.
                // ...
            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(MainActivity.this,
                                REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    double alt = location.getLatitude();
                    double alt2 = location.getLongitude();
                    Log.d("test","a:"+ alt + alt2);

                }
            }

        };

        final ArrayList<sMarket> MondayList=new ArrayList<>();
        final ArrayList<sMarket> TuesdayList=new ArrayList<>();
        final ArrayList<sMarket> WednesdayList=new ArrayList<>();
        final ArrayList<sMarket> ThursdayList=new ArrayList<>();
        final ArrayList<sMarket> FridayList=new ArrayList<>();
        final ArrayList<sMarket> SaturdayList=new ArrayList<>();
        final ArrayList<sMarket> SundayList=new ArrayList<>();

       // sta sxolia opou exw * shmainei oti arxise na isxuei apo kapoia hmeromhnia kai meta,ara tha prepei na prosexoume mhpws xreiastei na kanoume update an allaksei pali topothesia,epishs opou exw hmeromhnia shmainei oti mexri tote tha einai sthn topothesia auth kai meta tha allaksei
        MondayList.add(new sMarket("Γλυφάδα(Πυρνάρι)", 00.0, 37.873114, 23.762878));
        MondayList.add(new sMarket("Ίλιον(Ζωοδόχου Πηγής)", 00.0, 38.021728, 23.698081));
        MondayList.add(new sMarket("Ζωγράφου(Γουδί)", 00.0, 37.982131, 23.768774));
        MondayList.add(new sMarket("Κηφισιά", 00.0, 38.072513, 23.796675));
        MondayList.add(new sMarket("Αγ.Βαρβάρα", 00.0, 37.990603, 23.662530)); //*
        MondayList.add(new sMarket("Καματερό", 00.0, 38.049685, 23.698548));//υπο κατασκευη
        MondayList.add(new sMarket("Αγ.Δημήτριος(Λιδορίκι)", 00.0, 37.930078, 23.732230));//*
        MondayList.add(new sMarket("Αθήνα(Γούβα)", 00.0, 37.957375, 23.739294));//*
        MondayList.add(new sMarket("Αθήνα(Κολοκυνθού)", 00.0, 37.997452, 23.712129));//*
        MondayList.add(new sMarket("Αθήνα(Λεύκα)", 00.0, 37.960830, 23.732212));//*
        //MondayList.add(new sMarket("Αθήνα(Πατήσια)", 00.0, 38.015401, 23.732333));//* 15/9
        //MondayList.add(new sMarket("Αιγάλεω(Αναγέννηση)", 00.0, 38.001239, 23.677747));//* 15/9
        MondayList.add(new sMarket("Αιγάλεω(Άνω Λιούμη)", 00.0, 38.002731, 23.669712));//* 30/11
        MondayList.add(new sMarket("Αχαρνές(Αγ.Πέτρος)", 00.0, 38.095823, 23.731755));// υπό κατασκευή
        MondayList.add(new sMarket("Ελευσίνα", 00.0, 38.047294, 23.538724));// υπό κατασκευη
        MondayList.add(new sMarket("Ζωγράφου", 00.0, 37.978389, 23.770182));//* 20/11
        MondayList.add(new sMarket("Νέα Λιόσια", 00.0, 38.030709, 23.705678));//*
        MondayList.add(new sMarket("Ίλιον(Παλατιανή)", 00.0, 38.031008, 23.688716));//*
        MondayList.add(new sMarket("Καλλιθέα", 00.0, 37.950775, 23.704129));//* 31/3
        MondayList.add(new sMarket("Αμφιάλη", 00.0, 37.968445, 23.620031));
        MondayList.add(new sMarket("Κηφισιά", 00.0, 38.072484, 23.796670));//*
        MondayList.add(new sMarket("Κορυδαλλός(Άγ.Στυλιανός)", 00.0, 37.986196, 23.649571));//*
        MondayList.add(new sMarket("Νέα Ιωνία(Καλογρέζα)", 00.0, 38.038642, 23.768379));// 18/11
        MondayList.add(new sMarket("Γέρακας", 00.0, 38.009195, 23.858673));// υπο κατασκευή
        MondayList.add(new sMarket("Παλλήνη(Γλυκά Νερά)", 00.0, 37.983799, 23.846571));// υπο κατασκευη
        MondayList.add(new sMarket("Νέο Φάληρο", 00.0, 37.947950, 23.669672));
        MondayList.add(new sMarket("Πέραμα", 00.0, 37.965039, 23.576049));//*
        MondayList.add(new sMarket("Περιστέρι(Αγ.Τριάδα)", 00.0, 38.006214, 23.689531));//*
        MondayList.add(new sMarket("Περιστέρι(Άσπρα Χώματα)", 00.0, 38.011583, 23.683403));//*
        MondayList.add(new sMarket("Ραφήνα", 00.0, 38.023041, 24.002521));// υπό κατασκευη
        MondayList.add(new sMarket("Σαρωνίδα", 00.0, 37.750236, 23.912476));// υπο κατασκευη
        MondayList.add(new sMarket("Νέο Ψυχικό", 00.0, 38.006568, 23.782838));//*
        MondayList.add(new sMarket("Χολαργός", 00.0, 37.999893, 23.796311));//* 30/11
        MondayList.add(new sMarket("Θεσσαλονίκη(Αλλατίνη)", 00.0, 40.608813, 22.960485));
        MondayList.add(new sMarket("Θεσσαλονίκη(Κυβέλια)", 00.0, 40.614213, 22.956000));
        MondayList.add(new sMarket("Νίκαια(Οσία Ξένη)", 00.0, 37.967145, 23.64410));//* 31/12
        MondayList.add(new sMarket("Βόλος(Ν.Ιωνία)",0,39.373284, 22.940850));// 31/12
        MondayList.add(new sMarket("Λαμία(Παγκράτι)",0,38.892718, 22.425502));
        MondayList.add(new sMarket("Ηράκλειο Κρήτης",0,35.336861, 25.116861));
        MondayList.add(new sMarket("Λάρισα(Φιλιπούπολη)",0,39.631575, 22.390323));
        MondayList.add(new sMarket("Λάρισα(40 Μαρτύρων)",0,39.639857, 22.427890));
        MondayList.add(new sMarket("Λάρισα(Ανθούπολης)",0,39.625975, 22.420692)) ;
       MondayList.add(new sMarket("Χανιά",0,35.508895, 24.026672));// 31/3/19

        //Tuesday
        TuesdayList.add(new sMarket("Αγ.Παρασκευή(Κοντόπευκο)",0,38.019121, 23.829085));//*
        TuesdayList.add(new sMarket("Άγ.Ανάργυροι(Ανάκασα)",0,38.041178, 23.732618));
        TuesdayList.add(new sMarket("Καματερό",0,38.059309, 23.710294));//* υπο κατασκευη
        TuesdayList.add(new sMarket("Αμπελοκηποι",0,37.988084, 23.749523));
        TuesdayList.add(new sMarket("Βοτανικός",0,37.979074, 23.708255));
        TuesdayList.add(new sMarket("Κεραμεικός",0,37.981711, 23.718912));
        TuesdayList.add(new sMarket("Νέα Φιλοθέη",0,37.996669, 23.758757));
        TuesdayList.add(new sMarket("Νέα Φιλοθέη 2η",0,38.001572, 23.763876));
        TuesdayList.add(new sMarket("Παγκράτι",0,37.966016, 23.754264));
        TuesdayList.add(new sMarket("Κυψέλη",0,38.005387, 23.736926)); //*
        TuesdayList.add(new sMarket("Αιγάλεω(Νταμαράκια)",0,37.989303, 23.671086));//*
        TuesdayList.add(new sMarket("Άλιμος(Σούλι)",0,37.925637, 23.741934));
        TuesdayList.add(new sMarket("Άλιμος",0,37.916133, 23.719075));// 30/4
        TuesdayList.add(new sMarket("Μαρούσι",0,38.047041, 23.824806));
        TuesdayList.add(new sMarket("Βύρωνας(Καρέα)",0,37.945072, 23.768821));// 28/2
        TuesdayList.add(new sMarket("Γαλάτσι",0,38.013753, 23.756986));//*
        TuesdayList.add(new sMarket("Άνω Γλυφάδα",0,37.898355, 23.771190));//*
        TuesdayList.add(new sMarket("Γλυφάδα(Τερψιθέα)",0,37.890722, 23.769431));
        TuesdayList.add(new sMarket("Διόνυσος",0,38.116940, 23.872412));
        TuesdayList.add(new sMarket("Κάτω Ηλιούπολη",0,37.927159, 23.747019));//*
        TuesdayList.add(new sMarket("Καισαριανή",0,37.967627, 23.758865));// 31/12
        //TuesdayList.add(new sMarket("Καλλιθέα(Αγ.Ελεούσα)",0,37.956389, 23.696169));//*
        TuesdayList.add(new sMarket("Καλλιθέα(Τζιτζιφιές)",0,37.947624, 23.693982));//* 31/3
        TuesdayList.add(new sMarket("Νέα Κηφισιά",0,38.097430, 23.806317));
        TuesdayList.add(new sMarket("Πόρτο Ράφτη",0,37.902623, 24.022352));
        TuesdayList.add(new sMarket("Μοσχάτο",0,37.952148, 23.682502));//*
        TuesdayList.add(new sMarket("Νέα Φιλαδέλφεια",0,38.033682, 23.738934));//* 31/10
        TuesdayList.add(new sMarket("Νέα Φιλαδέλφεια 2η",0,38.052272, 23.743650)); // 31/12
        TuesdayList.add(new sMarket("Πειραιάς(Καστέλα)",0,37.937406, 23.653073));//*
        TuesdayList.add(new sMarket("Πειραιάς(Πηγάδα)",0,37.954215, 23.669185));
        TuesdayList.add(new sMarket("Περιστέρι(Χρυσούπολη)",0,38.019044, 23.676979)); // 15/6
        TuesdayList.add(new sMarket("Ταύρος",0,37.967850, 23.692983));
        TuesdayList.add(new sMarket("Φυλής(Ζεφύρι)",0,38.064828, 23.718147));//* υπο κατασκευη
        TuesdayList.add(new sMarket("Κυψέλη",0,38.005584, 23.736929));//*
        TuesdayList.add(new sMarket("Χαλάνδρι",0,38.026105, 23.794671));//* 31/12
        TuesdayList.add(new sMarket("Βόλος(Ν.Δημητριάδα)",0,39.358978, 22.965531));//* 31/10
        TuesdayList.add(new sMarket("Βόλος",0,39.376298, 22.963728));//* 31/10
        TuesdayList.add(new sMarket("Ν.Αγχίαλος",0,39.279521, 22.819958));
       TuesdayList.add(new sMarket("Λαμία(Κέντρο)",0,38.895814, 22.436427));
       TuesdayList.add(new sMarket("Ηράκλειο Κρήτης",0,35.337688, 25.159855));
       TuesdayList.add(new sMarket("Ηράκλειο Κρήτης",0,35.307927, 25.146718));
       TuesdayList.add(new sMarket("Λάρισα(Σιδ.Σταθμού)",0,39.630125, 22.422853));

        TuesdayList.add(new sMarket("Θεσσαλονίκη(Μαλακόπη)",0,40.610490, 22.980438));
        TuesdayList.add(new sMarket("Θεσσαλονίκη(Ν.Κρήνη)",0,40.568087, 22.961409));
        TuesdayList.add(new sMarket("Θεσσαλονίκη(Κιμ.Βογά)",0,40.598446, 22.953524));
        TuesdayList.add(new sMarket("Θεσσαλονίκη(40 Εκκλησίες)",0,40.632831, 22.964834));
        TuesdayList.add(new sMarket("Θεσσαλονίκη(Κάτω Ηλιούπολη)",0,40.664865, 22.924956));
        TuesdayList.add(new sMarket("Θεσσαλονίκη(Ζωοδόχου Πηγής)",0,40.653854, 22.917772));
        TuesdayList.add(new sMarket("Θεσσαλονίκη(Αριστοτέλους)",0,40.666301, 22.911870));
        TuesdayList.add(new sMarket("Εύοσμος(Δημαρχείο)",0,40.670116, 22.910156));
        TuesdayList.add(new sMarket("Θεσσαλονίκη(Πολίχνης)",0,40.659445, 22.943013));
        TuesdayList.add(new sMarket("Θεσσαλονίκη(Παπαδάκη)",0,40.593029, 22.962282));


        TuesdayList.add(new sMarket("Χανιά",0,35.498761, 24.025359));//31/3
        //Wednesday
        WednesdayList.add(new sMarket("Άγ.Ανάργυροι",0,38.027045, 23.721258));
        WednesdayList.add(new sMarket("Μπραχάμι",0,37.938721, 23.733146));//*
        WednesdayList.add(new sMarket("Αθήνα(Άγ.Στυλιανός)",0,37.995430, 23.751373));
        WednesdayList.add(new sMarket("Αθήνα(Άγ.Παύλος)",0,37.988925, 23.721807)); //*
        WednesdayList.add(new sMarket("Άνω Πατήσια",0,38.026298, 23.746234));//* 31/12
        WednesdayList.add(new sMarket("Γκύζη",0,37.992140, 23.746347));//*
        WednesdayList.add(new sMarket("Κάτω Πετράλωνα",0,37.968964, 23.705144));
        WednesdayList.add(new sMarket("Κολωνός",0,37.990898, 23.713382));//* 31/3
        WednesdayList.add(new sMarket("Αθήνα(Πλατεία Αγάμων)",0,37.998499, 23.729100));
        WednesdayList.add(new sMarket("Αθήνα(Πλατεία Κολιάτσου)",0,38.010239, 23.733771));//* 15/6
        WednesdayList.add(new sMarket("Δάφνη",0,37.951878, 23.734787));//*
        WednesdayList.add(new sMarket("Αργυρούπολη",0,37.908342, 23.755822));// 31/10
        WednesdayList.add(new sMarket("Ηλιούπολη(Αστυνομικά)",0,37.924201, 23.758086));//*
        WednesdayList.add(new sMarket("Ίλιον(Ραδιοφωνίας)",0,38.036528, 23.709855));//*
        WednesdayList.add(new sMarket("Κερατσίνι(Άγ.Γεώργιος)",0,37.963169, 23.620174));
        WednesdayList.add(new sMarket("Δραπετσώνα",0,37.946937, 23.628535));
        WednesdayList.add(new sMarket("Κηφισιά",0,38.066561, 23.824154));
        WednesdayList.add(new sMarket("Λυκόβρυση",0,38.067658, 23.782411));
        WednesdayList.add(new sMarket("Μεταμόρφωση(Κανπίτσα)",0,38.056954, 23.755243));
        WednesdayList.add(new sMarket("Άνω Νέα Σμύρνη",0,37.941649, 23.721383));
        WednesdayList.add(new sMarket("Νέα Σμύρνη",0,37.945171, 23.712114));
        WednesdayList.add(new sMarket("Παλαιό Φάληρο",0,37.927521, 23.714189));
        WednesdayList.add(new sMarket("Πειραιάς(Αγ.Μαρίνα)",0,37.958614, 23.648216));
        WednesdayList.add(new sMarket("Πειραιάς(Μυρτιδιώτισσα)",0,37.943282, 23.660609));//*
        WednesdayList.add(new sMarket("Πειραιάς(Παλιά Κοκκινιά)",0,37.963085, 23.652840));//*
        WednesdayList.add(new sMarket("Περιστέρι(Κηπούπολη)",0,38.031469, 23.678976));//* 30/12
        WednesdayList.add(new sMarket("Περιστέρι",0,38.014081, 23.700111));
        WednesdayList.add(new sMarket("Πεύκης",0,38.061804, 23.796243));//*
        WednesdayList.add(new sMarket("Χαϊδάρι(Δαφνί)",0,38.014491, 23.634602));
        WednesdayList.add(new sMarket("Αργυρούπολη(Αλεξιούπολη)",0,37.908378, 23.755837));//*  31/10
        WednesdayList.add(new sMarket("Βόλος",0,39.369294, 22.947448));//31/10
        WednesdayList.add(new sMarket("Λαμία(Κέντρο)",0,38.906183, 22.430849));
        WednesdayList.add(new sMarket("Ηράκλειο Κρήτης",0,35.32502, 25.130602));
        WednesdayList.add(new sMarket("Ηράκλειο Κρήτης",0,  35.3260147, 25.114094 ));
        WednesdayList.add(new sMarket("Λάρισα(Αγ.Αθανάσιου)",0,39.640380, 22.409214));
        WednesdayList.add(new sMarket("Λάρισα(Νεράιδας)",0,39.624528, 22.413740));

        WednesdayList.add(new sMarket("Θεσσαλονίκη(Άνω Τούμπα)",0,40.614083, 22.969768));
        WednesdayList.add(new sMarket("Θεσσαλονίκη(Σόλωνος-Κρήτης)",0,40.599478, 22.958124));
        WednesdayList.add(new sMarket("Θεσσαλονίκη(Λιτόχωρου)",0,40.621260, 22.961048));
        WednesdayList.add(new sMarket("Θεσσαλονίκη(Π.Μελά)",0,40.657189, 22.932225));
        WednesdayList.add(new sMarket("Θεσσαλονίκη(Συκιές)",0,40.650494, 22.948042));
        WednesdayList.add(new sMarket("Θεσσαλονίκη(Δαβάκη)",0,40.645480, 22.951704));
        WednesdayList.add(new sMarket("Θεσσαλονίκη(Φλέμινγκ)",0,40.667863, 22.932788));

        WednesdayList.add(new sMarket("Χανιά",0,35.509406, 24.016335));//31/3

        //Thursday
        ThursdayList.add(new sMarket("Άνω Αγία Βαρβάρα", 00.0, 37.990149, 23.652338)); //*
        ThursdayList.add(new sMarket("Αθήνα(Άγιος Ελευθέριος)", 00.0, 38.017871, 23.726214));
        //ThursdayList.add(new sMarket("Αθήνα(Αχαρνών)", 00.0, 38.008428, 23.726712)); // 15/9
        ThursdayList.add(new sMarket("Αθήνα(Ελληνορώσσων)", 00.0, 37.997068, 23.775511));//*
        ThursdayList.add(new sMarket("Αθήνα(Κυψέλη)", 00.0, 37.995609, 23.738007)); //*
        ThursdayList.add(new sMarket("Αθήνα(Προμπονά)", 00.0, 38.026026, 23.735141));//*
        ThursdayList.add(new sMarket("Αθήνα(Σεπόλια)", 00.0, 38.000474, 23.715432)); // *
        ThursdayList.add(new sMarket("Βούλας", 00.0, 37.852092, 23.757043));//*
        //ThursdayList.add(new sMarket("Νεραϊδα", 00.0, 37.960531, 23.748191)); // 9/7
        ThursdayList.add(new sMarket("Γαλάτσι", 00.0, 38.018228, 23.754258)); //*
        ThursdayList.add(new sMarket("Γλυφάδα", 00.0, 37.867178, 23.749200));
        ThursdayList.add(new sMarket("Ελληνικό", 00.0, 37.893135, 23.757171)); // 17/1
        ThursdayList.add(new sMarket("Άνω Ιλίσια", 00.0, 37.974105, 23.769688));
        ThursdayList.add(new sMarket("Άγιος Κωνσταντίνος", 00.0, 37.942010, 23.743228));
        ThursdayList.add(new sMarket("Άγιος Νικόλαος", 00.0, 38.046904, 23.693807));
        ThursdayList.add(new sMarket("Γαλήνη", 00.0, 37.975401, 23.628721));
        ThursdayList.add(new sMarket("Κορυδαλλός", 00.0, 37.981716, 23.658982)); //*
        ThursdayList.add(new sMarket("Αλσούπολη", 00.0, 38.032984, 23.774138));
        ThursdayList.add(new sMarket("Νέα Ιωνία", 00.0, 38.043485, 23.751038));
        ThursdayList.add(new sMarket("Νεάπολη (Περισσός)", 00.0, 38.034012, 23.760744));//*
        ThursdayList.add(new sMarket("Αγία Σοφία", 00.0, 37.956864, 23.639664));
        ThursdayList.add(new sMarket("Δεληγιάννη", 00.0, 37.943414, 23.650869));
        ThursdayList.add(new sMarket("Ανθούπολη", 00.0, 38.022954, 23.688504)); // 31/1
        ThursdayList.add(new sMarket("Υμηττός", 00.0, 37.949780, 23.743043)); // *
        ThursdayList.add(new sMarket("Δάσος Χαϊδαρίου", 00.0, 38.013365, 23.650722));
        ThursdayList.add(new sMarket("Χαλάνδρι", 00.0, 38.028228, 23.821222)); //30/11
        ThursdayList.add(new sMarket("Βόλος(Ν.Ιωνία)",0,39.375819, 22.926578));//31/12
       ThursdayList.add(new sMarket("Λαμία(Κέντρο)",0,38.898678, 22.440555));
        ThursdayList.add(new sMarket("Λαμία(Ν.Μαγνησία)",0,38.900061, 22.458182));
        ThursdayList.add(new sMarket("Ηράκλειο Κρήτης",0,35.3307325, 25.1412011));
        ThursdayList.add(new sMarket("Λάρισα(Ν.Σμύρνης)",0,39.648203, 22.434603));
        ThursdayList.add(new sMarket("Λάρισα(Αγ.Γεωργίου)",0,39.631118, 22.441745));
        ThursdayList.add(new sMarket("Λάρισα(Αβέρωφ)",0,39.613946, 22.426832));

        ThursdayList.add(new sMarket("Θεσσαλονίκη(Καλαμαριά)",0,40.585087, 22.952060));
        ThursdayList.add(new sMarket("Θεσσαλονίκη(Κανάρη)",0,40.607029, 22.968563));
        ThursdayList.add(new sMarket("Θεσσαλονίκη(Πυλαία)",0,40.598889, 22.990984));
        ThursdayList.add(new sMarket("Θεσσαλονίκη(Αγ.Παύλος)",0,40.640547, 22.963316));
        ThursdayList.add(new sMarket("Θεσσαλονίκη(Φοίνικας)",0,40.579310, 22.967851));
        ThursdayList.add(new sMarket("Θεσσαλονίκη(Γκράτσιου)",0,40.641231, 22.946566));
        ThursdayList.add(new sMarket("Θεσσαλονίκη(Καλλιθέα)",0,40.645523, 22.939863));
        ThursdayList.add(new sMarket("Θεσσαλονίκη(Πολίχνη)",0,40.660845, 22.951423));
        ThursdayList.add(new sMarket("Θεσσαλονίκη(Δενδροπόταμος)",0,40.657141, 22.899520));
        ThursdayList.add(new sMarket("Θεσσαλονίκη(Νικόπολη)",0,40.682893, 22.933908));

        ThursdayList.add(new sMarket("Χανιά",0,35.515131, 24.013227));// 31/3

        //Friday
        FridayList.add(new sMarket("Αγία Παρασκευή", 00.0, 38.001955, 23.816524)); //*
        FridayList.add(new sMarket("Άγιος Δημήτριος(Ανθέων)", 00.0, 37.933661, 23.741072));
        FridayList.add(new sMarket("Άγιος Δημήτριος(Ασύρματος)", 00.0, 37.926264, 23.717694));
        FridayList.add(new sMarket("Αθήνα(Άγιος Σώστης)", 00.0, 37.953272, 23.718645));
        FridayList.add(new sMarket("Αθήνα(Άνω Κυψέλη)", 00.0, 38.001572, 23.745673)); // 15/6
        FridayList.add(new sMarket("Αθήνα(Βεϊκού)", 00.0, 37.963435, 23.718848));//*
        FridayList.add(new sMarket("Αθήνα(Χίλτον)", 00.0, 37.973909, 23.752768));
        FridayList.add(new sMarket("Αθήνα(Κολωνάκι)", 00.0, 37.979279, 23.745334));
        FridayList.add(new sMarket("Αθήνα(Παγκράτι)", 00.0, 37.965703, 23.748055));
        FridayList.add(new sMarket("Αθήνα(Πετράλωνα)", 00.0, 37.968815, 23.713482));
        FridayList.add(new sMarket("Αιγάλεω(Αιγάλεω Α)", 00.0, 37.995149, 23.685060)); //30/11
        FridayList.add(new sMarket("Αιγάλεω(Αιγάλεω Β)", 00.0, 37.986679, 23.679289));
        FridayList.add(new sMarket("Αιγάλεω(Λιούμη)", 00.0, 37.994319, 23.670464)); // 30/11
        FridayList.add(new sMarket("Βριλλήσια", 00.0, 38.033054, 23.831937));
        FridayList.add(new sMarket("Νέα Ελβετία", 00.0, 37.956559, 23.756720)); // 9/1
        FridayList.add(new sMarket("Αγία Παρασκευή(Αργυρούπολη)", 00.0, 37.905328, 23.743096)); // 31/5
        FridayList.add(new sMarket("Νέο Ηράκλειο", 00.0, 38.048724, 23.762752));//*
       // FridayList.add(new sMarket("Καλλιθέα(Καλλιθέα)", 00.0, 37.958084, 23.711647)); //
       // FridayList.add(new sMarket("Καλλιθέα(Σφαγεία Καλλιθέας)", 00.0, 37.960677, 23.703664));//
        FridayList.add(new sMarket("Ειρήνη", 00.0, 38.037803, 23.746338));
        FridayList.add(new sMarket("Χαλκηδόνα", 00.0, 37.966097, 23.635231)); // 14/1
        FridayList.add(new sMarket("Παλαιό Φάληρο(Αγίας Βαρβάρας)", 00.0, 37.920720, 23.708837));
        FridayList.add(new sMarket("Παλαιό Φάληρο(Αμφιθέα)", 00.0, 37.938793, 23.703539));
        FridayList.add(new sMarket("Παλαιό Φάληρο", 00.0, 37.928415, 23.697849)); //*
        FridayList.add(new sMarket("Πειραιάς(Καμίνια)", 00.0, 37.953784, 23.660188));//*
        FridayList.add(new sMarket("Πειραιάς(Ταμπούρια)", 00.0, 37.960782, 23.629861));//*
        FridayList.add(new sMarket("Περιστέρι", 00.0, 38.014214, 23.684524));
        FridayList.add(new sMarket("Πετρούπολη(Αγία Τριάδα)", 00.0, 38.041077, 23.669220));
        FridayList.add(new sMarket("Άνω Πετρούπολη", 00.0, 38.044097, 23.677984)); // 30/11
        FridayList.add(new sMarket("Πετρούπολη", 00.0, 38.036074, 23.688170)); //31/10
        FridayList.add(new sMarket("Ρέντη", 00.0, 37.958399, 23.672224)); // 31/12
        FridayList.add(new sMarket("Βόλος(Μαγνητών)",0,39.361364, 22.957268)); // 31/10
        FridayList.add(new sMarket("Λαμία(Γαλανέϊκα)",0,38.916046, 22.428907));
        FridayList.add(new sMarket("Ηράκλειο Κρήτης",0,35.32679, 25.120651));
        FridayList.add(new sMarket("Λάρισα(Βιολογικά Προϊόντα)",0,39.619240, 22.402302));

        FridayList.add(new sMarket("Θεσσαλονίκη(Τριανδρία)",0,40.620958, 22.972036));
        FridayList.add(new sMarket("Θεσσαλονίκη(Χατζηλαζάρου)",0,40.606926, 22.956114));
        FridayList.add(new sMarket("Θεσσαλονίκη(Κηφισιά)",0,40.585749, 22.967882));
        FridayList.add(new sMarket("Θεσσαλονίκη(Σταυρούπολη)",0,40.664179, 22.935701));
        FridayList.add(new sMarket("Θεσσαλονίκη(Εύοσμος)",0,40.663769, 22.911763));
        FridayList.add(new sMarket("Θεσσαλονίκη(Καυτατζόγλου)",0,40.623469, 22.968907));
        FridayList.add(new sMarket("Θεσσαλονίκη(Μετέωρα)",0,40.656126, 22.956639));


        //Saturday
        SaturdayList.add(new sMarket("Άγιοι Ανάργυροι", 00.0, 38.047698, 23.732443));
        SaturdayList.add(new sMarket("Αθήνα(Άγιος Γεώργιος)", 00.0, 37.952425, 23.722328));
        SaturdayList.add(new sMarket("Αθήνα(Αμπελοκήποι)", 00.0, 37.991532, 23.762097));//*
        SaturdayList.add(new sMarket("Αθήνα(Εξάρχεια)", 00.0, 37.987296, 23.737615));
        SaturdayList.add(new sMarket("Αθήνα(Πλατεία Αττικής)", 00.0, 38.000041, 23.726112)); // 15/12
        SaturdayList.add(new sMarket("Αμαρούσι", 00.0, 38.052414, 23.804425)); //*
        SaturdayList.add(new sMarket("Πολυδρόσο", 00.0, 38.034002, 23.810491));//*
        SaturdayList.add(new sMarket("Νέα Ευρυάλη", 00.0, 37.874086, 23.750802)); //*
        SaturdayList.add(new sMarket("Ζωοδόχου Πηγής", 00.0, 37.951112, 23.730229));//*
        SaturdayList.add(new sMarket("Αργυρούπολη", 00.0, 37.905722, 23.752037));//*
        SaturdayList.add(new sMarket("Αργυρούπολη Π.Π.", 00.0, 37.918294, 23.752028)); //*
        SaturdayList.add(new sMarket("Ηλιούπολη", 00.0, 37.943408, 23.759324));//*
        SaturdayList.add(new sMarket("Μιχελή", 00.0, 38.035895, 23.6982638));//*
        SaturdayList.add(new sMarket("Νέα Ευγενεία", 00.0, 37.955698, 23.619153));
        SaturdayList.add(new sMarket("Κορυδαλλός", 00.0, 37.978836, 23.650401));
        SaturdayList.add(new sMarket("Μεταμόρφωση", 00.0, 38.062461, 23.764212));
        SaturdayList.add(new sMarket("Περισσός", 00.0, 38.033969, 23.750188)); // *
        SaturdayList.add(new sMarket("Νέα Πεντέλη", 00.0, 38.060253, 23.860504)); // 31/10
        //SaturdayList.add(new sMarket("Κουτσικάρι", 00.0, 37.974331, 23.659206)); // 31/7
        SaturdayList.add(new sMarket("Καλλίπολη", 00.0, 37.931713, 23.631889));
        SaturdayList.add(new sMarket("Μπουρνάζι", 00.0, 38.013657, 23.708943));
        SaturdayList.add(new sMarket("Χαϊδάρι(Αγία Γρηγορούσα)", 00.0, 38.015774, 23.671780));
        SaturdayList.add(new sMarket("Χαϊδάρι(Κουνέλια)", 00.0, 38.030577, 23.794105));
        //SaturdayList.add(new sMarket("Χαϊδάρι", 00.0, 38.002877, 23.661962)); // 31/8
        SaturdayList.add(new sMarket("Κάτω Χαλάνδρι", 00.0, 38.013101, 23.800752)); //*
        SaturdayList.add(new sMarket("Νέα Χαλκηδόνα", 00.0, 38.026400, 23.729543));
        SaturdayList.add(new sMarket("Βόλος(Γ.Δήμου)",0,39.368665, 22.957522));//31/10
        SaturdayList.add(new sMarket("Βόλος(Αγ.Αναργύρων)",0,39.367184, 22.929372));// 31/10
        SaturdayList.add(new sMarket("Λαμία(Κέντρο)",0,38.899810, 22.434471));
        SaturdayList.add(new sMarket("Ηράκλειο Κρήτης",0,35.3302599, 25.1415444));
        SaturdayList.add(new sMarket("Λάρισα(Νεάπολης)",0,39.624152, 22.395816));

        SaturdayList.add(new sMarket("Χανιά",0,35.516744, 24.023637));
        SaturdayList.add(new sMarket("Θεσσαλονίκη(Μαρτίου)",0,40.599554, 22.963281));
        SaturdayList.add(new sMarket("Θεσσαλονίκη(Ξηροκρήνη)",0,40.648513, 22.931185));
        SaturdayList.add(new sMarket("Θεσσαλονίκη(Νεάπολη)",0,40.653185, 22.936572));
        SaturdayList.add(new sMarket("Θεσσαλονίκη(Παυσανία)",0,40.613623, 22.982869));
        SaturdayList.add(new sMarket("Θεσσαλονίκη(Κορδελιό)",0,40.671763, 22.892359));
        SaturdayList.add(new sMarket("Θεσσαλονίκη(Αγ.Νικόλαος)",0,40.576300, 22.951419));
        SaturdayList.add(new sMarket("Θεσσαλονίκη(Εύοσμος)",0,40.676101, 22.923314));
        SaturdayList.add(new sMarket("Θεσσαλονίκη(Κανάρη)",0,40.659417, 22.943204));

        SaturdayList.add(new sMarket("Χανιά",0,35.516744, 24.023637));//31/3

        //thn kyriakh kleistes
        SundayList.add(new sMarket("Αθηνα", 00.0, 37.943454, 23.618762));
        SundayList.add(new sMarket("Αθηνα", 00.0, 35.943454, 23.618762));
        SundayList.add(new sMarket("Αθηνα", 00.0, 36.943454, 23.618762));
        SundayList.add(new sMarket("Αθηνα", 00.0, 38.943454, 23.618762));
        SaturdayList.add(new sMarket("Νίκαια 2η", 00.0, 37.999976, 23.641376));


        sMAdapter = new sMarketAdapter(this,BestMarketList);
        listView.setAdapter(sMAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                sMarket selectedsMarket = BestMarketList.get(i);
                double selectedLatt = selectedsMarket.getlatt();
                double selectedLong = selectedsMarket.getlongt();
                String locationInfo = selectedLatt + "," + selectedLong;
                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + locationInfo + "&mode=d");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });
        sCalendar = Calendar.getInstance();
         time=sCalendar.get(Calendar.HOUR_OF_DAY);
        dayLongName = sCalendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH);
         Nomarket=(TextView) findViewById(R.id.nomarket);
           main=(View) findViewById(R.id.main);
        switch (dayLongName) {
            case "Monday":
                dayLongNameGreek = "Δευτέρα";
                sMarketList.addAll(MondayList);
                Nomarket.setVisibility(View.GONE);
                break;

            case "Tuesday":
                dayLongNameGreek = "Τρίτη";
                sMarketList.addAll(TuesdayList);
                Nomarket.setVisibility(View.GONE);
                break;

            case "Wednesday":
                dayLongNameGreek = "Τετάρτη";
                sMarketList.addAll(WednesdayList);
                Nomarket.setVisibility(View.GONE);
                break;

            case "Thursday":
                dayLongNameGreek = "Πέμπτη";
                sMarketList.addAll(ThursdayList);
                Nomarket.setVisibility(View.GONE);
                break;

            case "Friday":
                dayLongNameGreek = "Παρασκευή";
                sMarketList.addAll(FridayList);
                Nomarket.setVisibility(View.GONE);
                break;

            case "Saturday":
                dayLongNameGreek = "Σάββατο";
                sMarketList.addAll(SaturdayList);
                Nomarket.setVisibility(View.GONE);
                break;

            case "Sunday":
                dayLongNameGreek = "Κυριακή";
               main.setVisibility(View.GONE);
                Nomarket.setVisibility(View.VISIBLE);
                sMarketList.addAll(SundayList);

                break;

            default:
                break;
        }

         if(time>3 && time<12){
                Greet="Καλημέρα";
        }
        else {
                Greet="Καλησπέρα";
            }
        greetText = (TextView) findViewById(R.id.greetText);
        greetText.setText(""+Greet+", σήμερα " + dayLongNameGreek + " οι κοντινότερες αγορές είναι:");
        locationtest = findViewById(R.id.locationtest);


  }

    protected void createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setNumUpdates(1);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }


    @Override
    public void onResume()
    {
        super.onResume();
        createLocationRequest();
        startLocationUpdates();

        if(hasFailed == true) {
          locateAndSort();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    private void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    private void startLocationUpdates() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
        }
        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                mLocationCallback,
                null /* Looper */);


    }





    public void locateAndSort()
    {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
        }

            createLocationRequest();

             mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.

                        if (location != null ){


                            deviceLong = location.getLongitude();
                            deviceLatt = location.getLatitude();

                            locationtest.setText("Recent Location " + deviceLong + "," + deviceLatt);
                            n = sMarketList.size();
                            //vriskei to distance kai to vazei sto antistoixo tou sMarket
                            for (int counter = 0; counter < n; counter++) {


                                Location.distanceBetween(deviceLatt, deviceLong, sMarketList.get(counter).getlatt(), sMarketList.get(counter).getlongt(), results);

                                sMarketList.get(counter).setsMarketDistance(results[0] / 1000);

                            }
                            //sortarei ta sMarket
                            for (int counter = 0; counter < n; counter++) {
                                for (int j = counter + 1; j < n; j++) {
                                    if (sMarketList.get(counter).getsMarketDistance() > sMarketList.get(j).getsMarketDistance()) {

                                        sMarket num = sMarketList.get(counter);
                                        sMarketList.set(counter, sMarketList.get(j));
                                        sMarketList.set(j, num);
                                    }
                                }
                            }
                            //gemizei thn BestMarket me ta kontinotera
                            for (int counter = 0; counter < 4; counter++) {

                                sMarket temp = sMarketList.get(counter);
                                BestMarketList.add(temp);
                            }
                            TextDistance = (TextView) findViewById(R.id.distance);
                            TextDistance.setText(String.format("%.2f", sMarketList.get(0).getsMarketDistance()) + " χλμ");
                            hasFailed = false;

                        } else {
                            hasFailed = true;

                            dialogmaps.show();
                            dialog.dismiss();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("MapDemoActivity", "Error trying to get last GPS location");
                        e.printStackTrace();
                    }


                });
    }
   
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode)
        {
            case 1 : {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    locateAndSort();
                } else {

                }
                return;
            }
        }
    }

}


