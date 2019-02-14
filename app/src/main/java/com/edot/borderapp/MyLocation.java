package com.edot.borderapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

/**
 * Created by Admin on 1/21/2017.
 */

public class MyLocation implements LocationListener {
    LocationManager manager;
    Context client;
    Location location;

    MyLocation(Context context) {
        client = context;
        manager = (LocationManager) client.getSystemService(client.LOCATION_SERVICE);
        //if (ActivityCompat.checkSelfPermission(client, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(client, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        manager.requestLocationUpdates(manager.GPS_PROVIDER, 0, 0, this);
        location=manager.getLastKnownLocation(manager.GPS_PROVIDER);
    }

    @Override
    public void onLocationChanged(Location l) {
        location = l;
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public Location getLocation() {
        return location;
    }

    public void remove() {
        manager.removeUpdates(this);
    }
}
