package app.pamelaiki.com;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



    }

    EditText Address=(EditText) findViewById(R.id.Address);
    public void  FindMarkets(View view) {  Intent nogps=new Intent(MainActivity.this,FleeMarket.class);
        nogps.putExtra("address",Address.getText().toString());
        startActivity(nogps);}
}
