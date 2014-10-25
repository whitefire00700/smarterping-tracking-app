package com.track.smarterping;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

public class StatusActivity extends ListActivity {

	private static final int LIMIT = 20;
	public int a = 1;
	Boolean isInternetPresent = false;
	ConnectionDetector cd;

	private static final LinkedList<String> messages = new LinkedList<String>();
	private static final Set<ArrayAdapter<String>> adapters = new HashSet<ArrayAdapter<String>>();

	private static void notifyAdapters() {
		for (ArrayAdapter<String> adapter : adapters) {
			adapter.notifyDataSetChanged();
		}
	}

	public static void addMessage(String message) {
		Log.i(TraccarActivity.LOG_TAG, message);
		DateFormat format = DateFormat.getTimeInstance(DateFormat.SHORT);
		message = format.format(new Date()) + " - " + message;
		messages.add(message);
		while (messages.size() > LIMIT) {
			messages.removeFirst();
		}
		notifyAdapters();
	}

	public static void clearMessages() {
		messages.clear();
		notifyAdapters();
	}

	private ArrayAdapter<String> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.status);

		chkk();
		
		
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1,
				messages);
		setListAdapter(adapter);
		adapters.add(adapter);
	}

	@Override
	protected void onDestroy() {
		adapters.remove(adapter);
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.status, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.clear) {
			clearMessages();
			return true;
		}
		
		return super.onOptionsItemSelected(item);
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

				StatusActivity.this.finish();
			}
		});

		// Showing Alert Message
		alertDialog.show();

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


}
