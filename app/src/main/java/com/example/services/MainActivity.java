package com.example.services;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button start, stop, bind, unbind, getrandom;
    Intent serviceintent;
    TextView randomtext;

    Myservices myservices;
    boolean isServiceBound;
    ServiceConnection serviceConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start = findViewById(R.id.start);
        stop = findViewById(R.id.stop);
        bind = findViewById(R.id.Bind);
        unbind = findViewById(R.id.unbind);
        getrandom = findViewById(R.id.getrandom);
        randomtext=findViewById(R.id.randomtext);

        Log.i("ServiceDemo", "Main Activity Thread id:" + Thread.currentThread().getId());

        serviceintent = new Intent(getApplicationContext(), Myservices.class);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startService(serviceintent);
                Toast.makeText(MainActivity.this, "Service Started", Toast.LENGTH_SHORT).show();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopService(serviceintent);
                Toast.makeText(MainActivity.this, "Service stopped", Toast.LENGTH_SHORT).show();
            }
        });

        bind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BindService();
            }
        });
        unbind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UnBindService();
            }
        });
        getrandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetRandomNumber();
            }
        });


    }

    private void SetRandomNumber() {
        if (isServiceBound){
            randomtext.setText("Random Number:"+myservices.getRandomNumber());
        }
        else {
            randomtext.setText("Service Not Bound");
        }
    }

    private void UnBindService() {
        if (isServiceBound){
            unbindService(serviceConnection);
            isServiceBound=false;
        }
    }

    private void BindService() {
        if (serviceConnection == null) {
            serviceConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                 Myservices.MyServiceBinder myServiceBinder=(Myservices.MyServiceBinder)iBinder;
                 myservices=myServiceBinder.getService();
                    isServiceBound = true;
                }

                @Override
                public void onServiceDisconnected(ComponentName componentName) {
                    isServiceBound = false;
                }
            };
        }
        bindService(serviceintent,serviceConnection, Context.BIND_AUTO_CREATE);
    }
}