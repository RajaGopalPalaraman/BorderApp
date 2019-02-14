package com.edot.borderapp;


import android.location.Location;
import android.os.Handler;

/**
 * Created by Admin on 1/22/2017.
 */

public class WaitingForGPS extends Thread {
    MyLocation myLocation;
    Handler handler;
    String string=null;
    WaitingForGPS(MyLocation myLocation, Handler handler)
    {
        this.myLocation=myLocation;
        this.handler=handler;
    }
    @Override
    public void run() {
        super.run();
        while(myLocation.getLocation()==null);
        handler.sendEmptyMessage(10);
    }
}
