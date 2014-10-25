package com.track.smarterping;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * Main user interface
 */

@SuppressWarnings("deprecation")
public class TraccarActivity extends PreferenceActivity {

	public static final String LOG_TAG = "traccar";

	public static final String KEY_ID = "id";
	public static final String KEY_ADDRESS = "address";
	public static final String KEY_PORT = "port";
	public static final String KEY_INTERVAL = "interval";
	public static final String KEY_PROVIDER = "provider";
	public static final String KEY_EXTENDED = "extended";
	public static final String KEY_STATUS = "status";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		initPreferences();
		SharedPreferences sharedPreferences = getPreferenceScreen()
				.getSharedPreferences();
		if (sharedPreferences.getBoolean(KEY_STATUS, false))
			startService(new Intent(this, TraccarService.class));
	
	}

	@Override
	protected void onResume() {
		super.onResume();
		getPreferenceScreen().getSharedPreferences()
				.registerOnSharedPreferenceChangeListener(
						preferenceChangeListener);
	}

	@Override
	protected void onPause() {
		getPreferenceScreen().getSharedPreferences()
				.unregisterOnSharedPreferenceChangeListener(
						preferenceChangeListener);
		super.onPause();
	}

	OnSharedPreferenceChangeListener preferenceChangeListener = new OnSharedPreferenceChangeListener() {
		@Override
		public void onSharedPreferenceChanged(
				SharedPreferences sharedPreferences, String key) {
			if (key.equals(KEY_STATUS)) {
				if (sharedPreferences.getBoolean(KEY_STATUS, false)) {
					startService(new Intent(TraccarActivity.this,
							TraccarService.class));
				} else {
					stopService(new Intent(TraccarActivity.this,
							TraccarService.class));
				}
			} else if (key.equals(KEY_ID)) {
				findPreference(KEY_ID).setSummary(
						sharedPreferences.getString(KEY_ID, null));
			}
		}
	};

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

		new AlertDialog.Builder(TraccarActivity.this)
				.setTitle("Exit")
				.setMessage("Are you sure?")
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								
								// continue with delete
								finish();
								
							}
						})
				.setNegativeButton(android.R.string.no,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// do nothing
							}
						}).setIcon(android.R.drawable.ic_dialog_alert).show();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);

	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.status) {
			startActivity(new Intent(this, StatusActivity.class));
			return true;
		} else if (item.getItemId() == R.id.about) {
			
			startActivity(new Intent(this, AboutActivity.class));
			return true;
			
		} else if (item.getItemId() == R.id.Update) {
			startActivity(new Intent(this, AndroidGPSTrackingActivity.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private boolean isServiceRunning() {
		ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if (TraccarService.class.getName().equals(
					service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}

	private void initPreferences() {
		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		String id = telephonyManager.getDeviceId();

		SharedPreferences sharedPreferences = getPreferenceScreen()
				.getSharedPreferences();

	
		if (!sharedPreferences.contains(KEY_ID)) {
			sharedPreferences.edit().putString(KEY_ID, id).commit();
		}
		findPreference(KEY_ID).setSummary(
				sharedPreferences.getString(KEY_ID, id));
	}

}
