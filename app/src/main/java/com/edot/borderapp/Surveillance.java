package com.edot.borderapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static java.lang.Math.abs;

/**
 * Created by Admin on 1/22/2017.
 */

public class Surveillance extends Service implements LocationListener {

    private static final String CHANNEL_ID = "Pdjfhkj#jfkjdabf";

    private static final String AT_BORDER = "At Border";
    private static final String WITH_IN_BORDER = "With in Border";
    private static final String OUT_OF_BORDER = "Out of Border";
    private static final String TURN_ON_LOCATION = "Turn On GPS";
    private static final String CONNECTING = "Connecting";

    private static final int NOTIFICATION_ID = 1;

    private NotificationManager notificationManager;

    private NotificationCompat.Builder builder;

    private double max_lat=181,min_lat=181,max_lon=181,min_lon=181;

    private boolean equals(Location myLocation) {
        if(abs(myLocation.getLatitude()-max_lat)<=0.005)
            return true;
        if(abs(myLocation.getLatitude()-min_lat)<=0.005)
            return true;
        if(abs(myLocation.getLongitude()-max_lon)<=0.005)
            return true;
        if(abs(myLocation.getLongitude()-min_lon)<=0.005)
            return true;
        return false;
    }

    private boolean out_of_border(Location myLocation) {
        if(myLocation.getLatitude()>max_lat)
            return true;
        if(myLocation.getLatitude()<min_lat)
            return true;
        if(myLocation.getLongitude()>max_lon)
            return true;
        if(myLocation.getLongitude()<min_lon)
            return true;
        return false;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "NotiChannel";
            String description = "NotiDesc";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            notificationManager.createNotificationChannel(channel);
        }

        builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.stop)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(false);

        File file=new File(Environment.getExternalStorageDirectory(),getResources().getString(R.string.app_name));
        file = new File(file,getResources().getString(R.string.b));
        File temp=new File(file,getResources().getString(R.string.max_lat));
        int trig;
        FileInputStream stream;
        try {
            stream=new FileInputStream(temp);
            ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
            while((trig=stream.read())>0)
                byteArrayOutputStream.write(trig);
            max_lat= Double.parseDouble(new String(byteArrayOutputStream.toByteArray()));
            byteArrayOutputStream.close();
            stream.close();
            temp=new File(file,getResources().getString(R.string.min_lat));
            stream=new FileInputStream(temp);
            byteArrayOutputStream=new ByteArrayOutputStream();
            while((trig=stream.read())>0)
                byteArrayOutputStream.write(trig);
            min_lat= Double.parseDouble(new String(byteArrayOutputStream.toByteArray()));
            byteArrayOutputStream.close();
            stream.close();
            temp=new File(file,getResources().getString(R.string.max_lon));
            stream=new FileInputStream(temp);
            byteArrayOutputStream=new ByteArrayOutputStream();
            while((trig=stream.read())>0)
                byteArrayOutputStream.write(trig);
            max_lon= Double.parseDouble(new String(byteArrayOutputStream.toByteArray()));
            byteArrayOutputStream.close();
            stream.close();
            temp=new File(file,getResources().getString(R.string.min_lon));
            stream=new FileInputStream(temp);
            byteArrayOutputStream=new ByteArrayOutputStream();
            while((trig=stream.read())>0)
                byteArrayOutputStream.write(trig);
            min_lon= Double.parseDouble(new String(byteArrayOutputStream.toByteArray()));
            byteArrayOutputStream.close();
            stream.close();
        }catch (IOException ignored) {
        }
        Toast.makeText(this,String.valueOf(max_lat),Toast.LENGTH_SHORT).show();
        Toast.makeText(this,String.valueOf(min_lat),Toast.LENGTH_SHORT).show();
        Toast.makeText(this,String.valueOf(max_lon),Toast.LENGTH_SHORT).show();
        Toast.makeText(this,String.valueOf(min_lon),Toast.LENGTH_SHORT).show();

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (location != null) {
            if (equals(location)) {
                builder.setContentText(AT_BORDER);
            } else if (out_of_border(location)) {
                builder.setContentText(OUT_OF_BORDER);
            }
            else {
                builder.setContentText(WITH_IN_BORDER);
            }
        }
        else {
            builder.setContentText(CONNECTING);
        }
        startForeground(NOTIFICATION_ID,builder.build());
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,
                0,this);
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationManager.removeUpdates(this);
        notificationManager.cancel(NOTIFICATION_ID);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (equals(location)) {
            builder.setContentText(AT_BORDER);
        } else if (out_of_border(location)) {
            builder.setContentText(OUT_OF_BORDER);
        }
        else {
            builder.setContentText(WITH_IN_BORDER);
        }
        notificationManager.notify(NOTIFICATION_ID,builder.build());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (location != null) {
            if (equals(location)) {
                builder.setContentText(AT_BORDER);
            } else if (out_of_border(location)) {
                builder.setContentText(OUT_OF_BORDER);
            }
            else {
                builder.setContentText(WITH_IN_BORDER);
            }
        }
        else
        {
            builder.setContentText(CONNECTING);
        }

        notificationManager.notify(NOTIFICATION_ID,builder.build());
    }

    @Override
    public void onProviderDisabled(String provider) {
        builder.setContentText(TURN_ON_LOCATION);
        notificationManager.notify(NOTIFICATION_ID,builder.build());
    }
}
