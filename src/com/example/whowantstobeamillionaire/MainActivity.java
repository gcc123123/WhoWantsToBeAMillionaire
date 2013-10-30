package com.example.whowantstobeamillionaire;



import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		final Button button_settings = (Button) findViewById(R.id.main_button_settings);
		button_settings.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(
						new Intent(MainActivity.this,
								SettingsActivity.class));
			}
		});
		
		final Button button_scores = (Button) findViewById(R.id.main_button_scores);
		button_scores.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(
						new Intent(MainActivity.this,
								ScoresActivity.class));
			}
		});
		
		final Button button_play = (Button) findViewById(R.id.main_button_play);
		button_play.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(
						new Intent(MainActivity.this,
								PlayActivity.class));
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_credits:
			Intent creditIntent = new Intent(MainActivity.this, 
					CreditsActivity.class);
			startActivity(creditIntent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
		
	}

}
