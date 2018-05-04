package app.pamelaiki.com;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class Gps {

    private FusedLocationProviderClient mFusedLocationClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // ...

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            Intent  error=new Intent(this,MainActivity.class);
                             startActivity(error);
                        }
                    }
                });
    }

}
