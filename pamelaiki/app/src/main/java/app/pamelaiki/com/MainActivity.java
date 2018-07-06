package app.pamelaiki.com;



import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
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


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
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
    public double deviceLatt;
    public double deviceLong;
    public Location marketLoc;
    public Location lastPlace;
    public int n;
    public TextView TextDistance;
    public float[] results = new float[3];
    public AlertDialog.Builder builder;

    public float distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView0);
        final ArrayList<sMarket> sMarketList = new ArrayList<>();

        builder = new AlertDialog.Builder(MainActivity.this);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent gpsOptionsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(gpsOptionsIntent);
            }
        });

        builder.setMessage("Για τη σωστή λειτουργεία της εφαρμογής θα πρέπει να ενεργοποιήσετε το GPS, πατήστε ΟΚ για ενεργοποίηση.")
                .setTitle("Ενεργοποιήστε το GPS.");

        builder.setNegativeButton("Έξοδος", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                System.exit(0);
            }
        });

        final ArrayList<sMarket> BestMarketList = new ArrayList<>();//h lista p tha emfanizetai me tis kaluteres 4,to allaksa kai sto adapter

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
        MondayList.add(new sMarket("Αγ.Βαρβάρα", 00.0, 37.991370, 23.654593));
        MondayList.add(new sMarket("Καματερό", 00.0, 38.049685, 23.698548));//υπο κατασκευη
        MondayList.add(new sMarket("Αγ.Δημήτριος(Λιδορίκι)", 00.0, 37.930078, 23.732230));//*
        MondayList.add(new sMarket("Αθήνα(Γούβα)", 00.0, 37.957375, 23.739294));//*
        MondayList.add(new sMarket("Αθήνα(Κολοκυνθού)", 00.0, 37.997452, 23.712129));//*
        MondayList.add(new sMarket("Αθήνα(Λεύκα)", 00.0, 37.960400, 23.731468));//*
        MondayList.add(new sMarket("Αθήνα(Πατήσια)", 00.0, 38.015401, 23.732333));//* 15/9
        MondayList.add(new sMarket("Αιγάλεω(Αναγέννηση)", 00.0, 38.001239, 23.677747));//* 15/9
        MondayList.add(new sMarket("Αιγάλεω(Άνω Λιούμη)", 00.0, 38.002731, 23.669712));//* 30/11
        MondayList.add(new sMarket("Αχαρνές(Αγ.Πέτρος)", 00.0, 38.095823, 23.731755));// υπό κατασκευή
        MondayList.add(new sMarket("Ελευσίνα", 00.0, 38.047294, 23.538724));// υπό κατασκευη
        MondayList.add(new sMarket("Ζωγράφου", 00.0, 37.978389, 23.770182));//* 20/11
        MondayList.add(new sMarket("Νέα Λιόσια", 00.0, 38.030709, 23.705678));//*
        MondayList.add(new sMarket("Ίλιον(Παλατιανή)", 00.0, 38.031008, 23.688716));//*
        MondayList.add(new sMarket("Καλλιθέα", 00.0, 37.950294, 23.705063));//* 30/9
        MondayList.add(new sMarket("Αμφιάλη", 00.0, 37.968445, 23.620031));
        MondayList.add(new sMarket("Νέα Ερυθραία", 00.0, 38.093088, 23.814281));//* 31/8
        MondayList.add(new sMarket("Κορυδαλλός(Άγ.Στυλιανός)", 00.0, 37.986196, 23.649571));//*
        MondayList.add(new sMarket("Νέα Ιωνία(Καλογρέζα)", 00.0, 38.038641, 23.768391));//*
        MondayList.add(new sMarket("Γέρακας", 00.0, 38.009195, 23.858673));// υπο κατασκευή
        MondayList.add(new sMarket("Παλλήνη(Γλυκά Νερά)", 00.0, 37.983799, 23.846571));// υπο κατασκευη
        MondayList.add(new sMarket("Νέο Φάληρο", 00.0, 37.947950, 23.669672));
        MondayList.add(new sMarket("Πέραμα", 00.0, 37.964363, 23.580154));//*
        MondayList.add(new sMarket("Περιστέρι(Αγ.Τριάδα)", 00.0, 38.004217, 23.693244));//*
        MondayList.add(new sMarket("Περιστέρι(Άσπρα Χώματα)", 00.0, 38.008309, 23.683775));//*
        MondayList.add(new sMarket("Ραφήνα", 00.0, 38.023041, 24.002521));// υπό κατασκευη
        MondayList.add(new sMarket("Σαρωνίδα", 00.0, 37.750236, 23.912476));// υπο κατασκευη
        MondayList.add(new sMarket("Νέο Ψυχικό", 00.0, 38.004539, 23.778239));//*
        MondayList.add(new sMarket("Χολαργός", 00.0, 38.001255, 23.800281));//* 31/8
        MondayList.add(new sMarket("Θεσσαλονίκη(Αλλατίνη)", 00.0, 40.608813, 22.960485));
        MondayList.add(new sMarket("Θεσσαλονίκη(Κυβέλια)", 00.0, 40.614213, 22.956000));
        MondayList.add(new sMarket("Νίκαια(Οσία Ξένη)", 00.0, 37.967145, 23.64410));//* 31/12

        TuesdayList.add(new sMarket("Αγ.Παρασκευή(Κοντόπευκο)",0,38.019121, 23.829085));//*
        TuesdayList.add(new sMarket("Άγ.Ανάργυροι(Ανάκασα)",0,38.041178, 23.732618));
        TuesdayList.add(new sMarket("Καματερό",0,38.059309, 23.710294));//* υπο κατασκευη
        TuesdayList.add(new sMarket("Αμπελοκηποι",0,37.988084, 23.749523));
        TuesdayList.add(new sMarket("Βοτανικός",0,37.979074, 23.708255));
        TuesdayList.add(new sMarket("Κεραμεικός",0,37.981711, 23.718912));
        TuesdayList.add(new sMarket("Νέα Φιλοθέη",0,37.996669, 23.758757));
        TuesdayList.add(new sMarket("Νέα Φιλοθέη 2η",0,38.001572, 23.763876));
        TuesdayList.add(new sMarket("Παγκράτι",0,37.966016, 23.754264));
        TuesdayList.add(new sMarket("Αιγάλεω(Νταμαράκια)",0,37.989303, 23.671086));//*
        TuesdayList.add(new sMarket("Άλιμος(Σούλι)",0,37.925637, 23.741934));
        TuesdayList.add(new sMarket("Μαρούσι",0,38.047041, 23.824806));
        TuesdayList.add(new sMarket("Βύρωνας(Καρέα)",0,37.948148, 23.771464));//* 31/8
        TuesdayList.add(new sMarket("Γαλάτσι",0,38.016212, 23.751579));//*
        TuesdayList.add(new sMarket("Άνω Γλυφάδα",0,37.897884, 23.769116));//*
        TuesdayList.add(new sMarket("Γλυφάδα(Τερψιθέα)",0,37.890722, 23.769431));
        TuesdayList.add(new sMarket("Διόνυσος",0,38.116940, 23.872412));
        TuesdayList.add(new sMarket("Ηλιούπολη",0,37.927539, 23.746849));//*
        TuesdayList.add(new sMarket("Καλλιθέα(Αγ.Ελεούσα)",0,37.956389, 23.696169));//* 30/9
        TuesdayList.add(new sMarket("Καλλιθέα(Τζιτζιφιές)",0,37.947057, 23.694468));//* 30/9
        TuesdayList.add(new sMarket("Νέα Κηφισιά",0,38.097430, 23.806317));
        TuesdayList.add(new sMarket("Πόρτο Ράφτη",0,37.902623, 24.022352));
        TuesdayList.add(new sMarket("Μοσχάτο",0,37.952148, 23.682502));//*
        TuesdayList.add(new sMarket("Νέα Φιλαδέλφεια",0,38.033682, 23.738934));//* 31/10
        TuesdayList.add(new sMarket("Πειραιάς(Καστέλα)",0,37.937406, 23.653073));//*
        TuesdayList.add(new sMarket("Πειραιάς(Πηγάδα)",0,37.954215, 23.669185));
        TuesdayList.add(new sMarket("Περιστέρι(Χρυσούπολη)",0,38.024468, 23.675361));
        TuesdayList.add(new sMarket("Ταύρος",0,37.967850, 23.692983));
        TuesdayList.add(new sMarket("Φυλής(Ζεφύρι)",0,38.064828, 23.718147));//* υπο κατασκευη
        TuesdayList.add(new sMarket("Κυψέλη",0,38.005584, 23.736929));//*
        TuesdayList.add(new sMarket("Χαλάνδρι",0,38.026105, 23.794671));//* 31/12
        //Wednesday
        WednesdayList.add(new sMarket("Άγ.Ανάργυροι",0,38.027045, 23.721258));
        WednesdayList.add(new sMarket("Μπραχάμι",0,37.936384, 23.735316));//*
        WednesdayList.add(new sMarket("Αθήνα(Άγ.Στυλιανός)",0,37.995430, 23.751373));
        WednesdayList.add(new sMarket("Αθήνα(Άγ.Παύλος)",0,37.988093, 23.722614));
        WednesdayList.add(new sMarket("Άνω Πατήσια",0,38.021628, 23.744366));//* 31/8
        WednesdayList.add(new sMarket("Γκύζη",0,37.990603, 23.745687));//*
        WednesdayList.add(new sMarket("Κάτω Πετράλωνα",0,37.968964, 23.705144));
        WednesdayList.add(new sMarket("Κολωνός",0,37.988975, 23.712936));//* 30/9
        WednesdayList.add(new sMarket("Αθήνα(Πλατεία Αγάμων)",0,37.998499, 23.729100));
        WednesdayList.add(new sMarket("Αθήνα(Πλατεία Κολιάτσου)",0,38.009504, 23.732452));//* 31/8
        WednesdayList.add(new sMarket("Δάφνη",0,37.951878, 23.734787));//*
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
        WednesdayList.add(new sMarket("Πειραιάς(Παλιά Κοκκινιά)",0,37.961848, 23.653745));//*
        WednesdayList.add(new sMarket("Περιστέρι(Κηπούπολη)",0,38.031469, 23.678976));//* 30/12
        WednesdayList.add(new sMarket("Περιστέρι",0,38.014081, 23.700111));
        WednesdayList.add(new sMarket("Πεύκης",0,38.057999, 23.790532));//*
        WednesdayList.add(new sMarket("Χαϊδάρι(Δαφνί)",0,38.014491, 23.634602));
        WednesdayList.add(new sMarket("Αργυρούπολη(Αλεξιούπολη)",0,37.908378, 23.755837));//*  31/10
        //Thursday
        ThursdayList.add(new sMarket("Άνω Αγία Βαρβάρα", 00.0, 37.986196, 23.649571));
        ThursdayList.add(new sMarket("Αθήνα(Άγιος Ελευθέριος)", 00.0, 38.017871, 23.726214));
        ThursdayList.add(new sMarket("Αθήνα(Αχαρνών)", 00.0, 38.008428, 23.726712)); // 15/9
        ThursdayList.add(new sMarket("Αθήνα(Ελληνορώσσων)", 00.0, 37.994854, 23.772483));
        ThursdayList.add(new sMarket("Αθήνα(Κυψέλη)", 00.0, 37.997976, 23.737023)); // 4/7
        ThursdayList.add(new sMarket("Αθήνα(Προμπονά)", 00.0, 38.026038, 23.735157));
        ThursdayList.add(new sMarket("Αθήνα(Σεπόλια)", 00.0, 38.000818, 23.717312)); //25/7
        ThursdayList.add(new sMarket("Βούλας", 00.0, 37.851104, 23.760300));
        ThursdayList.add(new sMarket("Νεραϊδα", 00.0, 37.960531, 23.748191)); // 9/7
        ThursdayList.add(new sMarket("Γαλάτσι", 00.0, 38.016511, 23.756531));
        ThursdayList.add(new sMarket("Γλυφάδα", 00.0, 37.867178, 23.749200));
        ThursdayList.add(new sMarket("Ελληνικό", 00.0, 37.890954, 23.755731)); //31/8
        ThursdayList.add(new sMarket("Άνω Ιλίσια", 00.0, 37.974105, 23.769688));
        ThursdayList.add(new sMarket("Άγιος Κωνσταντίνος", 00.0, 37.942010, 23.743228));
        ThursdayList.add(new sMarket("Άγιος Νικόλαος", 00.0, 38.046904, 23.693807));
        ThursdayList.add(new sMarket("Γαλήνη", 00.0, 37.975401, 23.628721));
        ThursdayList.add(new sMarket("Κορυδαλλός", 00.0, 37.981935, 23.658251));
        ThursdayList.add(new sMarket("Αλσούπολη", 00.0, 38.032984, 23.774138));
        ThursdayList.add(new sMarket("Νέα Ιωνία", 00.0, 38.043485, 23.751038));
        ThursdayList.add(new sMarket("Νεάπολη (Περισσός)", 00.0, 38.032812, 23.757396));
        ThursdayList.add(new sMarket("Αγία Σοφία", 00.0, 37.956864, 23.639664));
        ThursdayList.add(new sMarket("Δεληγιάννη", 00.0, 37.943414, 23.650869));
        ThursdayList.add(new sMarket("Ανθούπολη", 00.0, 38.021692, 23.689229)); //30/9
        ThursdayList.add(new sMarket("Υμηττός", 00.0, 37.952852, 23.749444)); //4/7
        ThursdayList.add(new sMarket("Δάσος", 00.0, 38.013365, 23.650722));
        //Friday
        FridayList.add(new sMarket("Αγία Παρασκευή", 00.0, 38.009760, 23.820662));
        FridayList.add(new sMarket("Άγιος Δημήτριος(Ανθέων)", 00.0, 37.933661, 23.741072));
        FridayList.add(new sMarket("Άγιος Δημήτριος(Ασύρματος)", 00.0, 37.926264, 23.717694));
        FridayList.add(new sMarket("Αθήνα(Άγιος Σώστης)", 00.0, 37.953272, 23.718645)); // mon
        FridayList.add(new sMarket("Αθήνα(Άνω Κυψέλη)", 00.0, 38.004211, 23.745506)); // 31/8
        FridayList.add(new sMarket("Αθήνα(Βεϊκού)", 00.0, 37.963435, 23.718848));
        FridayList.add(new sMarket("Αθήνα(Ιλίσια (Χίλτον))", 00.0, 37.973909, 23.752768));
        FridayList.add(new sMarket("Αθήνα(Κολωνάκι)", 00.0, 37.979279, 23.745334));
        FridayList.add(new sMarket("Αθήνα(Παγκράτι)", 00.0, 37.965703, 23.748055));
        FridayList.add(new sMarket("Αθήνα(Πετράλωνα)", 00.0, 37.968815, 23.713482));
        FridayList.add(new sMarket("Αιγάλεω(Αιγάλεω Α)", 00.0, 37.995149, 23.685060)); //30/11
        FridayList.add(new sMarket("Αιγάλεω(Αιγάλεω Β)", 00.0, 37.986679, 23.679289));
        FridayList.add(new sMarket("Αιγάλεω(Λιούμη)", 00.0, 37.994319, 23.670464)); // 30/11
        FridayList.add(new sMarket("Βριλλήσια", 00.0, 38.033054, 23.831937));
        FridayList.add(new sMarket("Νέα Ελβετία", 00.0, 37.955535, 23.761151)); // 9/7
        FridayList.add(new sMarket("Αγία Παρασκευή", 00.0, 37.908305, 23.742212)); // 30/9
        FridayList.add(new sMarket("Νέο Ηράκλειο", 00.0, 38.048724, 23.762752));
        FridayList.add(new sMarket("Καλλιθέα(Καλλιθέα)", 00.0, 37.958084, 23.711647)); // 30/9
        FridayList.add(new sMarket("Καλλιθέα(Σφαγεία Καλλιθέας)", 00.0, 37.960677, 23.703664));// 30/9
        FridayList.add(new sMarket("Ειρήνη", 00.0, 38.037803, 23.746338));
        FridayList.add(new sMarket("Παλαιό Φάληρο(Αγίας Βαρβάρας)", 00.0, 37.920720, 23.708837));
        FridayList.add(new sMarket("Παλαιό Φάληρο(Αμφιθέα)", 00.0, 37.938793, 23.703539));
        FridayList.add(new sMarket("Παλαιό Φάληρο(Παλαιό Φάληρο)", 00.0, 37.928415, 23.697849));
        FridayList.add(new sMarket("Πειραιάς(Καμίνια)", 00.0, 37.953784, 23.660188));
        FridayList.add(new sMarket("Πειραιάς(Ταμπούρια)", 00.0, 37.960782, 23.629861));
        FridayList.add(new sMarket("Περιστέρι", 00.0, 38.014214, 23.684524));
        FridayList.add(new sMarket("Πετρούπολη(Αγία Τριάδα)", 00.0, 38.041077, 23.669220)); // mon
        FridayList.add(new sMarket("Πετρούπολη(Άνω Πετρούπολη)", 00.0, 38.044389, 23.679667)); // 30/8
        //Saturday
        SaturdayList.add(new sMarket("Άγιοι Ανάργυροι", 00.0, 38.047698, 23.732443));
        SaturdayList.add(new sMarket("Αθήνα(Άγιος Γεώργιος)", 00.0, 37.952425, 23.722328));
        SaturdayList.add(new sMarket("Αθήνα(Αμπελοκήποι)", 00.0, 37.991532, 23.762097));
        SaturdayList.add(new sMarket("Αθήνα(Εξάρχεια)", 00.0, 37.987296, 23.737615));
        SaturdayList.add(new sMarket("Αθήνα(Πλατεία Αττικής)", 00.0, 37.998895, 23.724868)); // 15/9
        SaturdayList.add(new sMarket("Αμαρούσι", 00.0, 38.053557, 23.797641));
        SaturdayList.add(new sMarket("Πολυδρόσο", 00.0, 38.034002, 23.810491));
        SaturdayList.add(new sMarket("Νέα Ευρυάλη", 00.0, 37.881742, 23.748783)); //20/7
        SaturdayList.add(new sMarket("Ζωοδόχου Πηγής", 00.0, 37.951112, 23.730229));
        SaturdayList.add(new sMarket("Αργυρούπολη(Αργυρούπολη)", 00.0, 37.902726, 23.748494));
        SaturdayList.add(new sMarket("Αργυρούπολη(Αργυρούπολη Π.Π.)", 00.0, 37.918409, 23.749308));
        SaturdayList.add(new sMarket("Ηλιούπολη", 00.0, 37.943420, 23.755290));
        SaturdayList.add(new sMarket("Μιχελή", 00.0, 38.037497, 23.696708));
        SaturdayList.add(new sMarket("Νέα Ευγενεία", 00.0, 37.955698, 23.619153));
        SaturdayList.add(new sMarket("Κορυδαλλός", 00.0, 37.978836, 23.650401));
        SaturdayList.add(new sMarket("Μεταμόρφωση", 00.0, 38.062461, 23.764212));
        SaturdayList.add(new sMarket("Περισσός", 00.0, 38.034394, 23.748218)); // 13/7
        SaturdayList.add(new sMarket("Κουτσικάρι", 00.0, 37.974331, 23.659206)); // 31/7
        SaturdayList.add(new sMarket("Καλλίπολη", 00.0, 37.931713, 23.631889));
        SaturdayList.add(new sMarket("Μπουρνάζι", 00.0, 38.013657, 23.708943));
        SaturdayList.add(new sMarket("Χαϊδάρι(Αγία Γρηγορούσα)", 00.0, 38.015774, 23.671780));
        SaturdayList.add(new sMarket("Χαϊδάρι(Κουνέλια)", 00.0, 38.030577, 23.794105));
        SaturdayList.add(new sMarket("Χαϊδάρι(Χαιδάρι)", 00.0, 38.002877, 23.661962)); // 31/8
        SaturdayList.add(new sMarket("Κάτω Χαλάνδρι", 00.0, 38.011289, 23.802235));
        SaturdayList.add(new sMarket("Νέα Χαλκηδόνα", 00.0, 38.026400, 23.729543));
        //thn kyriakh kleistes



        SundayList.add(new sMarket("Αθηνα", 00.0, 37.943454, 23.618762));
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
        dayLongName = sCalendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH);


        switch (dayLongName) {
            case "Monday":
                dayLongNameGreek = "Δευτέρα";
                sMarketList.addAll(MondayList);
                break;

            case "Tuesday":
                dayLongNameGreek = "Τρίτη";
                sMarketList.addAll(TuesdayList);
                break;

            case "Wednesday":
                dayLongNameGreek = "Τετάρτη";
                sMarketList.addAll(WednesdayList);
                break;

            case "Thursday":
                dayLongNameGreek = "Πέμπτη";
                sMarketList.addAll(ThursdayList);
                break;

            case "Friday":
                dayLongNameGreek = "Παρασκευή";
                sMarketList.addAll(FridayList);
                break;

            case "Saturday":
                dayLongNameGreek = "Σάββατο";
                sMarketList.addAll(SaturdayList);
                break;

            case "Sunday":
                dayLongNameGreek = "Κυριακή";
                sMarketList.addAll(SundayList);
                break;

            default:
                break;
        }

        greetText = (TextView) findViewById(R.id.greetText);
        greetText.setText("Καλημέρα, σήμερα " + dayLongNameGreek + " οι κοντινότερες αγορές είναι:");

        locationtest = findViewById(R.id.locationtest);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
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
                            TextDistance.setText(String.format("%.2f",sMarketList.get(0).getsMarketDistance()) + " χλμ");

                        }else{

                            AlertDialog dialog = builder.create();
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
}


