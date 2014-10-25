package com.track.smarterping;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class AndroidGPSTrackingActivity extends Activity {

	// GPSTracker class
	GPSTracker gps;
	Boolean isInternetPresent = false;
	ConnectionDetector cd;
	public int a = 0;
	Calendar c = Calendar.getInstance();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.display);

		chkk();

		// create class object
		gps = new GPSTracker(AndroidGPSTrackingActivity.this);

		// check if GPS enabled
		if (gps.canGetLocation()) {

			double latitude = gps.getLatitude();
			double longitude = gps.getLongitude();

			// \n is for new line
			
			if(isInternetPresent){
			Toast.makeText(
					getApplicationContext(),
					"Your Location is - \nLat: " + latitude + "\nLong: "
							+ longitude, Toast.LENGTH_LONG).show();
			}
			TextView elatitude = (TextView) findViewById(R.id.elatitude);
			TextView elongitude = (TextView) findViewById(R.id.elongitude);
			TextView ealtitude = (TextView) findViewById(R.id.ealtitude);
			TextView espeed = (TextView) findViewById(R.id.espeed);
			TextView edate = (TextView) findViewById(R.id.edate);
			TextView egsm = (TextView) findViewById(R.id.egsm);
			TextView egps = (TextView) findViewById(R.id.egps);

			elatitude.setText(" " + gps.getLatitude());
			elongitude.setText(" " + gps.getLongitude());
			ealtitude.setText(" " + gps.getAltitude());
			espeed.setText(" " + gps.getSpeed());
			edate.setText(" " + c.getTime());
			egsm.setText("Available ");
			egps.setText("Available ");

		} // else {
			// can't get location
			// GPS or Network is not enabled
			// Ask user to enable GPS/network in settings
			// gps.showSettingsAlert();
		// }

        ServerUrl serverUrl = new ServerUrl();
        serverUrl.sendResultsToServ();
	}

	public void chkk()
	{

		cd = new ConnectionDetector(getApplicationContext());
		// get Internet status
		isInternetPresent = cd.isConnectingToInternet();

		if (isInternetPresent) {
			a++;

		} else {
			// Internet connection is not present
			// Ask user to connect to Internet
			
			new AlertDialog.Builder(this)
			.setTitle("No Internet Connection")
			.setMessage("You don't have internet connection.")
			.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int which) {
							// continue with delete
							finish();
							int pid=android.os.Process.myPid();
							android.os.Process.killProcess(pid);
						}
					}).setIcon(android.R.drawable.ic_dialog_alert).show();

		}
		
	}

	

	public void showAlertDialog(Context context, String title, String message,
			Boolean status) {

		AlertDialog alertDialog = new AlertDialog.Builder(context).create();

		// Setting Dialog Title
		alertDialog.setTitle(title);

		// Setting Dialog Message
		alertDialog.setMessage(message);

		// can't cancel button
		alertDialog.setCancelable(false);

		// Setting alert dialog icon
		alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);

		// Setting OK Button
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

				AndroidGPSTrackingActivity.this.finish();
			}
		});

		// Showing Alert Message
		alertDialog.show();

	}
}
