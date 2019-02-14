package com.edot.borderapp;

import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    MyLocation myLocation;
    static Handler handler;
    TextView view;
    File file;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        file=new File(Environment.getExternalStorageDirectory(),getResources().getString(R.string.app_name));
        view=(TextView)findViewById(R.id.waiting_text);
        myLocation=new MyLocation(this);
        handler=new Handler(getMainLooper())
        {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what==10)
                {
                    Intent intent;
                    view.setText(getResources().getString(R.string.connected));
                    if(!file.exists()) {
                        file.mkdir();
                        intent=new Intent(MainActivity.this,L1.class);
                        startActivity(intent);
                    }
                    else {
                        intent=new Intent(MainActivity.this,Surveillance.class);
                        startService(intent);
                        Toast.makeText(MainActivity.this,getResources().getString(R.string.s_start),Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        WaitingForGPS gps=new WaitingForGPS(myLocation,handler);
        gps.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        myLocation.remove();
    }
}
