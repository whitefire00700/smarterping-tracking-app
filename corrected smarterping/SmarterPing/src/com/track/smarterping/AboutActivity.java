package com.track.smarterping;



import android.app.Activity;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.widget.TextView;

public class AboutActivity extends Activity {
	


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);

        TextView title = (TextView) findViewById(R.id.title);
        try {
            title.setText(title.getText() + " " + getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName);
        } catch (NameNotFoundException e) {
        }
    }

}
