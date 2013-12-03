package com.example.androidside;

import java.text.SimpleDateFormat;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.text.format.Time;
import android.util.Log;

public class serviceScreen extends Service {

    private long screenOnTimestamp;
    private long screenOffTimestamp;
    private long duration;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        try{
            //	        IntentFilter filter = new IntentFilter();
            //	        filter.addAction(Intent.ACTION_SCREEN_ON);
            //	        filter.addAction(Intent.ACTION_SCREEN_OFF);
            //	        BroadcastReceiver mReceiver = new receiverScreen();
            //	        registerReceiver(mReceiver, filter);
            screenOnTimestamp = System.currentTimeMillis();
            screenOffTimestamp = System.currentTimeMillis();
            duration = 0;

            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_SCREEN_ON);
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            filter.addAction(Keyword.BROADCAST_FILTER_SERVICE_SCREEN);
            BroadcastReceiver receiver = new BroadcastReceiver() {

                @Override
                public void onReceive(Context context, Intent intent) {
                    if(intent.getAction().equals(Intent.ACTION_SCREEN_ON)){
                        screenOnTimestamp = System.currentTimeMillis();
                        Log.i("mamahow", "screen on=" + screenOnTimestamp);

                    }else if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){
                        screenOffTimestamp = System.currentTimeMillis();
                        Log.i("mamahow", "screen off=" + screenOffTimestamp);
                        if (screenOffTimestamp > screenOnTimestamp) {
                            duration = screenOffTimestamp - screenOnTimestamp;
                        }

                    }else if(intent.getAction().equals(Keyword.BROADCAST_FILTER_SERVICE_SCREEN)){
                        if(intent.hasExtra(Keyword.KEYWORD_COMMAND)){
                            String command = intent.getStringExtra(Keyword.KEYWORD_COMMAND);

                            // Received Intent from MainActivity, and command is "GET_DURATION". 
                            if(command.equals(Keyword.KEYWORD_COMMAND_GET_DURATION)){
                                // Send Intent to MainActivity.
                                Intent send_to_intent = new Intent();
                                send_to_intent.setAction(Keyword.BROADCAST_FILTER_MAIN_ACTIVITY);
                                Bundle bundle = new Bundle();
                                bundle.putString(Keyword.KEYWORD_COMMAND, Keyword.KEYWORD_COMMAND_REPORT_DURATION);
                                bundle.putLong(Keyword.KEYWORD_LAST_SCREENON_TIME, screenOnTimestamp);
                                bundle.putLong(Keyword.KEYWORD_LAST_SCREENOFF_TIME, screenOffTimestamp);
                                bundle.putLong(Keyword.KEYWORD_LAST_DURATION, duration);
                                duration=0;
                                send_to_intent.putExtras(bundle);
                                sendBroadcast(send_to_intent);

                            }else{
                                Log.e("mamahow", "Unsupport command: " + command);
                            }
                        }
                    }
                }
            };
            registerReceiver(receiver, filter);

        }catch(Exception e){
            Log.d("mamahow", e.toString());
        }
    }
}
