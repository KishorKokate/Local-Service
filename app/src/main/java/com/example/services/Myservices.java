package com.example.services;

import android.app.Service;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.Random;

public class Myservices extends Service {
    int mRandomNumber;
    boolean mIsRandomGeneratorOn;

    final int MIN=0;
    final int MAX=100;

    class MyServiceBinder extends Binder{
        public Myservices getService(){
            return Myservices.this;
        }
    }

    private IBinder mBinder=new MyServiceBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i("ServiceDemo","In OnBind");
        return mBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        Log.i("ServiceDemo","In OnReBind");
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(getString(R.string.app_name),"In onStartCommand,id:"+Thread.currentThread().getId());

        mIsRandomGeneratorOn=true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                Myservices.this.startRandomNumberGenerator();
            }
        }).start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRandomNumberGenerator();
        Log.i(getString(R.string.app_name),"Service Destroyed");
    }

    private void startRandomNumberGenerator(){
        while (mIsRandomGeneratorOn){
            try {
                Thread.sleep(1000);
                if (mIsRandomGeneratorOn){
                    mRandomNumber=new Random().nextInt(MAX)+MIN;
                    Log.i("ServiceDemo","Thread id:"+Thread.currentThread().getId()+"Random Number:"+mRandomNumber);

                }
            }catch (InterruptedException e){
                Log.i("ServiceDemo","Thread Interrupted");

            }
        }

    }

    private void stopRandomNumberGenerator(){
        mIsRandomGeneratorOn=false;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i("ServiceDemo","In onUnBind");
        return super.onUnbind(intent);

    }

    public int getRandomNumber(){
        return mRandomNumber;
    }
}
