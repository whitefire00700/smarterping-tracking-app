package com.track.smarterping;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AutostartReceiver extends BroadcastReceiver {
	
	
	
	public static final String LOG_TAG = "Traccar.AutostartReceiver";
 
    @Override
    public void onReceive(Context context, Intent intent) {
    	SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    	if (sharedPreferences.getBoolean(TraccarActivity.KEY_STATUS, false)) {
    		context.startService(new Intent(context, TraccarService.class));
    	}
    }

}
