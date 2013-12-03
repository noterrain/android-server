package com.example.androidside;
import java.sql.Date;
import java.text.SimpleDateFormat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.format.Time;
import android.util.Log;

public class receiverScreen extends BroadcastReceiver {
 public String screenOnTime="";
 private Time screenOnTime2;
 private Time screenOnDate;
 private Date myDateScreenOn;
 
 public void onReceive(Context context, Intent intent) {
	 Time today = new Time(Time.getCurrentTimezone());;
     if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)){
    	 today.setToNow();
    	 screenOnTime2=today;
    	 screenOnTime=today.format("%k:%M:%S");
    	 //myDateScreenOn
    	 //screenOnDate= today;
    	 Log.i("mamahow", "screen on="+screenOnTime);
    	 //Log.d("mamahow", "screen on="+screenOnTime);
     }else{
    	 today.setToNow();
    	// SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd_HH:mm:ss");
    	 //String currentDateandTime = sdf.format(new Date(0));
    	 String now=today.format("%k:%M:%S");
    	 Time nowTime=today;
    	 if (screenOnTime!=""){ 
    		 //Time sub=nowTime-screenOnTime2;
    		 
    		 //long convertedLong = date.getTimeInMillis();
    		 //long currentTimeLong = System.currentTimeMillis(); // will get you current time in milli
    		 //long difference = currentTimeLong - convertedLong;
    		  //String strDiff = String.valueOf(difference);
    	 }
    	 screenOnTime="";
    	 Log.i("mamahow", "screen off now="+now);
    	 Log.i("mamahow", "screen off="+nowTime);
     }
 }
}

