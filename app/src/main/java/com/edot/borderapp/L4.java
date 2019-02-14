package com.edot.borderapp;

import android.content.Intent;
import android.location.Location;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class L4 extends AppCompatActivity {
    File file,temp,border;
    MyLocation myLocation;
    FileOutputStream stream;
    double latitude,longitude,max_lat,min_lat,max_lon,min_lon;
    Location location;
    Button add,refresh;
    TextView lat,lon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_l4);
        add=(Button)findViewById(R.id.l4_add);
        refresh=(Button)findViewById(R.id.r4);
        lat=(TextView)findViewById(R.id.l4_lat);
        lon=(TextView)findViewById(R.id.l4_lon);
        myLocation=new MyLocation(this);
        location=myLocation.getLocation();
        latitude=location.getLatitude();
        longitude=location.getLongitude();
        lat.setText(String.valueOf(latitude));
        lon.setText(String.valueOf(longitude));
        file=new File(Environment.getExternalStorageDirectory(),getResources().getString(R.string.app_name));
        file=new File(file,getResources().getString(R.string.l4));
        if(file.mkdir())
            Toast.makeText(this,R.string.success,Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this,R.string.failed,Toast.LENGTH_SHORT).show();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                temp=new File(file,getResources().getString(R.string.lat)+".txt");
                try {
                    temp.createNewFile();
                    stream=new FileOutputStream(temp);
                    stream.write(String.valueOf(latitude).getBytes());
                    stream.close();
                    temp=new File(file,getResources().getString(R.string.lon)+".txt");
                    temp.createNewFile();
                    stream=new FileOutputStream(temp);
                    stream.write(String.valueOf(longitude).getBytes());
                    stream.close();
                    max_lat=getMax_lat();
                    min_lat=getMin_lat();
                    max_lon=getMax_lon();
                    min_lon=getMin_lon();
                    border=file.getParentFile();
                    border=new File(border,getResources().getString(R.string.b));
                    if(border.mkdir())
                        Toast.makeText(L4.this,R.string.success,Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(L4.this,R.string.failed,Toast.LENGTH_SHORT).show();
                    temp=new File(border,getResources().getString(R.string.max_lat));
                    temp.createNewFile();
                    stream=new FileOutputStream(temp);
                    stream.write(String.valueOf(max_lat).getBytes());
                    stream.close();
                    temp=new File(border,getResources().getString(R.string.min_lat));
                    temp.createNewFile();
                    stream=new FileOutputStream(temp);
                    stream.write(String.valueOf(min_lat).getBytes());
                    stream.close();
                    temp=new File(border,getResources().getString(R.string.max_lon));
                    temp.createNewFile();
                    stream=new FileOutputStream(temp);
                    stream.write(String.valueOf(max_lon).getBytes());
                    stream.close();
                    temp=new File(border,getResources().getString(R.string.min_lon));
                    temp.createNewFile();
                    stream=new FileOutputStream(temp);
                    stream.write(String.valueOf(min_lon).getBytes());
                    stream.close();
                    startActivity(new Intent(L4.this,MainActivity.class));
                }catch (Exception e) {

                }
            }
        });
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                location=myLocation.getLocation();
                latitude=location.getLatitude();
                longitude=location.getLongitude();
                lat.setText(String.valueOf(latitude));
                lon.setText(String.valueOf(longitude));
            }
        });
    }
    @Override
    protected void onStop() {
        super.onStop();
        myLocation.remove();
    }

    public double getMax_lat() {
        max_lat=L1.latitude;
        if(L2.latitude>max_lat)
            max_lat=L2.latitude;
        if(L3.latitude>max_lat)
            max_lat=L3.latitude;
        if(latitude>max_lat)
            max_lat=latitude;
        return max_lat;
    }

    public double getMax_lon() {
        max_lon=L1.longitude;
        if(L2.longitude>max_lon)
            max_lon=L2.longitude;
        if(L3.longitude>max_lon)
            max_lon=L3.longitude;
        if(longitude>max_lon)
            max_lon=longitude;
        return max_lon;
    }

    public double getMin_lat() {
        min_lat=L1.latitude;
        if(L2.latitude<min_lat)
            min_lat=L2.latitude;
        if(L3.latitude<min_lat)
            min_lat=L3.latitude;
        if(latitude<min_lat)
            min_lat=latitude;
        return min_lat;
    }

    public double getMin_lon() {
        min_lon=L1.longitude;
        if(L2.longitude<min_lon)
            min_lon=L2.longitude;
        if(L3.longitude<min_lon)
            min_lon=L3.longitude;
        if(longitude<min_lon)
            min_lon=longitude;
        return min_lon;
    }
}
