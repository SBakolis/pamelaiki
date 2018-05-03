package app.pamelaiki.com;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by zapatistas on 3/30/2018.
 */

public class FleeMarket extends MainActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



    }

    EditText Address=(EditText) findViewById(R.id.Address);
    public void  FindMarkets(View view) {  Intent nogps=new Intent(MainActivity.this,FleeMarket.class);
        nogps.putExtra("address",Address.getText().toString());
        startActivity(nogps);}
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fleemarket);
        Intent nogps2=getIntent();
           Bundle bundle=nogps2.getExtras();
        String  address1=bundle.getString("address");
        TextView addresstext=(TextView) findViewById(R.id.addtext);
        addresstext.setText(address1.toString());
    }
}
