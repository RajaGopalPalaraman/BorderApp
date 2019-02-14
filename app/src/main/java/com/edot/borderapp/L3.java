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

public class L3 extends AppCompatActivity {
    File file,temp;
    MyLocation myLocation;
    FileOutputStream stream;
    static double latitude,longitude;
    Location location;
    Button add,refresh;
    TextView lat,lon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_l3);
        add=(Button)findViewById(R.id.l3_add);
        refresh=(Button)findViewById(R.id.r3);
        lat=(TextView)findViewById(R.id.l3_lat);
        lon=(TextView)findViewById(R.id.l3_lon);
        myLocation=new MyLocation(this);
        location=myLocation.getLocation();
        latitude=location.getLatitude();
        longitude=location.getLongitude();
        lat.setText(String.valueOf(latitude));
        lon.setText(String.valueOf(longitude));
        file=new File(Environment.getExternalStorageDirectory(),getResources().getString(R.string.app_name));
        file=new File(file,getResources().getString(R.string.l3));
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
                    startActivity(new Intent(L3.this,L4.class));
                }catch (IOException e) {
                    Toast.makeText(getApplicationContext(),R.string.failed,Toast.LENGTH_SHORT).show();
                    file.delete();
                    recreate();
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
}
