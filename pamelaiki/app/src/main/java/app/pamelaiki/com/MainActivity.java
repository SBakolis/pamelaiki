package app.pamelaiki.com;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import android.widget.ListView;

import java.util.ArrayList;

    public class MainActivity extends AppCompatActivity {

        private ListView listView;
        private sMarketAdapter sMAdapter;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            listView = (ListView)findViewById(R.id.listView0);
            ArrayList<sMarket> sMarketList = new ArrayList<>();
            sMarketList.add(new sMarket("Περιστερι", "23km", 0 ,0));
            sMarketList.add(new sMarket("Αθηνα", "25km", 0 ,0));
            sMarketList.add(new sMarket("Χαιδαρι", "33km", 0 ,0));
            sMarketList.add(new sMarket("Νικαια", "40km", 0 ,0));

<<<<<<< HEAD
    private ListView listView;
    private sMarketAdapter sMAdapter;
    private  TextView greetText;
=======
            sMAdapter = new sMarketAdapter(this,sMarketList);
            listView.setAdapter(sMAdapter);
        }
>>>>>>> 9636a2a2736769c0ab85878d223e8bd3e070bc0d


<<<<<<< HEAD


        listView = (ListView)findViewById(R.id.listView0);
        ArrayList<sMarket> sMarketList = new ArrayList<>();
        sMarketList.add(new sMarket("Περιστερι", "23km", 0 ,0));
        sMarketList.add(new sMarket("Αθηνα", "25km", 0 ,0));
        sMarketList.add(new sMarket("Χαιδαρι", "33km", 0 ,0));
        sMarketList.add(new sMarket("Νικαια", "40km", 0 ,0));
=======
>>>>>>> 9636a2a2736769c0ab85878d223e8bd3e070bc0d



