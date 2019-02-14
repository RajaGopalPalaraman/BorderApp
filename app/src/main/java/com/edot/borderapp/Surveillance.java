package com.edot.borderapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
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

public class Surveillance extends Service {
    MyLocation myLocation;
    Location location;
    double max_lat=181,min_lat=181,max_lon=181,min_lon=181;
    File file,temp;
    FileInputStream stream;
    ByteArrayOutputStream byteArrayOutputStream;
    int trig;
    boolean first=true;
    NotificationManager manager;
    Notification notification,notification1,notification2;
    Intent intent; int flags; int startId;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.intent=intent;
        this.flags=flags;
        this.startId=startId;
        manager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        notification1=new Notification.Builder(this).setContentTitle(getResources().getString(R.string.app_name)).setContentText("Within the border").setSmallIcon(R.drawable.stop).build();
        notification=new Notification.Builder(this).setContentTitle(getResources().getString(R.string.app_name)).setContentText("Out of the border").setSmallIcon(R.drawable.stop).build();
        notification2=new Notification.Builder(this).setContentTitle(getResources().getString(R.string.app_name)).setContentText("At the border").setSmallIcon(R.drawable.stop).build();
        manager.notify(0,notification1);
        myLocation=new MyLocation(this);
        file=new File(Environment.getExternalStorageDirectory(),getResources().getString(R.string.app_name));
        file=new File(file,getResources().getString(R.string.b));
        temp=new File(file,getResources().getString(R.string.max_lat));
        try {
            stream=new FileInputStream(temp);
            byteArrayOutputStream=new ByteArrayOutputStream();
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
        }catch (IOException e) {

        }
        Toast.makeText(this,String.valueOf(max_lat),Toast.LENGTH_SHORT).show();
        Toast.makeText(this,String.valueOf(min_lat),Toast.LENGTH_SHORT).show();
        Toast.makeText(this,String.valueOf(max_lon),Toast.LENGTH_SHORT).show();
        Toast.makeText(this,String.valueOf(min_lon),Toast.LENGTH_SHORT).show();
        while(true) {
            if (equals()) {
                if(first) {
                    manager.cancelAll();
                    manager.notify(5, notification2);
                    first=false;
                }
            }
            else if (out_of_border()) {
                manager.cancelAll();
                manager.notify(10, notification);
                break;
            }
            else {
                manager.cancelAll();
                manager.notify(0, notification1);
            }
        }
        return START_STICKY;
    }

    private boolean equals() {
        if(abs(myLocation.getLocation().getLatitude()-max_lat)<=0.005)
            return true;
        if(abs(myLocation.getLocation().getLatitude()-min_lat)<=0.005)
            return true;
        if(abs(myLocation.getLocation().getLongitude()-max_lon)<=0.005)
            return true;
        if(abs(myLocation.getLocation().getLongitude()-min_lon)<=0.005)
            return true;
        return false;
    }

    private boolean out_of_border() {
        if(myLocation.getLocation().getLatitude()>max_lat)
            return true;
        if(myLocation.getLocation().getLatitude()<min_lat)
            return true;
        if(myLocation.getLocation().getLongitude()>max_lon)
            return true;
        if(myLocation.getLocation().getLongitude()<min_lon)
            return true;
        return false;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //onStartCommand(intent,flags,startId);
    }
}
