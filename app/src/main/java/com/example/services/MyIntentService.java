package com.example.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.Random;


public class MyIntentService extends IntentService {

    int mRandomNumber;
    boolean mIsRandomGeneratorOn;

    final int MIN=0;
    final int MAX=100;

   class MyIntentServiceBinder extends Binder {
        public MyIntentService getService(){
            return MyIntentService.this;
        }
    }

    private IBinder mBinder= new MyIntentServiceBinder();

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
    public MyIntentService() {
        super("MyIntentService");
    }



    @Override
    protected void onHandleIntent(Intent intent) {
        mIsRandomGeneratorOn=true;
        MyIntentService.this.startRandomNumberGenerator();
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