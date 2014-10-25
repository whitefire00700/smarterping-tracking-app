package com.track.smarterping;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


public class AndroidDetectInternetConnectionActivity extends Activity {

	// flag for Internet connection status
	Boolean isInternetPresent = false;
	
	// Connection detector class
	ConnectionDetector cd;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.checkconn);

		
		// creating connection detector class instance
		cd = new ConnectionDetector(getApplicationContext());

		/**
		 * Check Internet status button click event
		 * */
				
				// get Internet status
				isInternetPresent = cd.isConnectingToInternet();

				// check for Internet status
				if (isInternetPresent) {
					// Internet Connection is Present
					// make HTTP requests
					
					//showAlertDialog(AndroidDetectInternetConnectionActivity.this, "Internet Connection","You have internet connection", true);
					
					Intent myIntent = new Intent(AndroidDetectInternetConnectionActivity.this, TraccarActivity.class);
					AndroidDetectInternetConnectionActivity.this.startActivity(myIntent);
					this.finish();
					
					
				} else {
					// Internet connection is not present
					// Ask user to connect to Internet
					
					showAlertDialog(AndroidDetectInternetConnectionActivity.this, "No Internet Connection",
							"You don't have internet connection.", false);
					
					
				}
			}

		
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);

	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.Back) {
			startActivity(new Intent(this,TraccarActivity.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	

	/**
	 * Function to display simple Alert Dialog
	 * @param context - application context
	 * @param title - alert dialog title
	 * @param message - alert message
	 * @param status - success/failure (used to set icon)
	 * */
	
	
	public void showAlertDialog(Context context, String title, String message, Boolean status) {
		
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
				
				AndroidDetectInternetConnectionActivity.this.finish();
			}
		});

		// Showing Alert Message
		alertDialog.show();
		
	}
}