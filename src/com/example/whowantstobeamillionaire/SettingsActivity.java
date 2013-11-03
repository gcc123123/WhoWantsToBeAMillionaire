package com.example.whowantstobeamillionaire;



import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SettingsActivity extends Activity implements OnItemSelectedListener{

	public final static String GRANTS = "grants";
	public final static String USERNAME = "username";
	public final static String SETTINGS_FILE = "settings";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		
		Spinner spinner = (Spinner) findViewById(R.id.spinner1);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
				R.array.grant_options, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(this);
		
		final Button button_add_friend = (Button) findViewById(R.id.settings_button_addfriend);
		button_add_friend.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SharedPreferences preferences = 
				getSharedPreferences(SettingsActivity.SETTINGS_FILE, Context.MODE_PRIVATE);
				String name = preferences.getString(SettingsActivity.USERNAME, null);
				EditText editText = (EditText) findViewById(R.id.settings_edittext_addfriend);
				String friend = editText.getText().toString();
				if(name != null && !name.contentEquals("") && friend != null && !friend.contentEquals("")){
					AddFriendTask addFriendTask = new AddFriendTask(SettingsActivity.this);
					addFriendTask.execute(name, friend);
				}
			}
		});
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		saveData();
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		restoreData();
	}

	
	private void restoreData(){
		SharedPreferences preferences = 
				getSharedPreferences(SETTINGS_FILE, Context.MODE_PRIVATE);
		EditText text = (EditText) findViewById(R.id.editText1);
		text.setText(preferences.getString(USERNAME, "No se ha encontrado"));	
		
		Spinner spinner = (Spinner) findViewById(R.id.spinner1);
		spinner.setSelection(preferences.getInt(GRANTS, 0));
	}
	
	private void saveData(){
		SharedPreferences preferences =
				getSharedPreferences(SETTINGS_FILE, Context.MODE_PRIVATE);
		
		Editor editor = preferences.edit();
		
		EditText text = (EditText) findViewById(R.id.editText1);
		editor.putString(USERNAME, text.getText().toString());
		
		Spinner spinner = (Spinner) findViewById(R.id.spinner1);
		editor.putInt(GRANTS, spinner.getSelectedItemPosition());
		
		editor.commit();
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
}
