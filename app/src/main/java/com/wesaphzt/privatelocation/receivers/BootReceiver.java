package com.wesaphzt.privatelocation.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

import com.wesaphzt.privatelocation.service.LocationService;

import static com.wesaphzt.privatelocation.MainActivity.PREFS_NAME;
import static com.wesaphzt.privatelocation.MainActivity.USER_LAT_NAME;
import static com.wesaphzt.privatelocation.MainActivity.USER_LNG_NAME;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);;
            if(prefs.getBoolean("START_ON_BOOT", false)) {
                Intent i = new Intent(context, LocationService.class);

                //init
                double lat = 0;
                double lng = 0;

                //shared prefs
                SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                try {
                    lat = Double.parseDouble(sharedPreferences.getString(USER_LAT_NAME, "null"));
                    lng = Double.parseDouble(sharedPreferences.getString(USER_LNG_NAME, "null"));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //add location data to the intent
                i.putExtra("lat", lat);
                i.putExtra("lng", lng);

                //check android api
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(i);
                } else {
                    context.startService(i);
                }
            }
        }
    }
}
