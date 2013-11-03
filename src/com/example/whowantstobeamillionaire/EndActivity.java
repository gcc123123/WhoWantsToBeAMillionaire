package com.example.whowantstobeamillionaire;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EndActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.end);
		
		Bundle extras = getIntent().getExtras();
		int idStringResult = extras.getInt("result");
		String stringPrize = extras.getString("prize");
		
		TextView textViewResult = (TextView) findViewById(R.id.textViewResult);
		textViewResult.setText(getResources().getString(idStringResult));
		
		TextView textViewPrize = (TextView) findViewById(R.id.textViewPrize);
		textViewPrize.setText(stringPrize);
		
		
		final Button button_play = (Button) findViewById(R.id.buttonPlay);
		button_play.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(
						new Intent(EndActivity.this, PlayActivity.class));
			}
		});
		
		final Button button_main = (Button) findViewById(R.id.buttonMain);
		button_main.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(
						new Intent(EndActivity.this, MainActivity.class));
			}
		});
		
	}
}
