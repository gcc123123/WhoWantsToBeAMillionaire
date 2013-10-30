package com.example.whowantstobeamillionaire;



import android.os.Bundle;
import android.app.Activity;
import android.app.LocalActivityManager;
import android.app.TabActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class ScoresActivity extends Activity {

	private TabHost host;
	private ScoresDataSource localStorage;
	private Bundle instance;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		
		instance = savedInstanceState;

		App appState = (App) this.getApplication();
		localStorage = appState.getDatabase();
		
		setTabHost();
		
	}
	
	

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		localStorage.close();
	}



	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		localStorage.open();
	}



	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		
		if(host != null && host.getCurrentTabTag() == getResources().getString(R.string.tab_global)){
			menu.findItem(R.id.action_delete_scores).setVisible(false);
		}
		else if(host != null 
				&& host.getCurrentTabTag() == getResources().getString(R.string.tab_local)
				&& !menu.hasVisibleItems()){
			menu.findItem(R.id.action_delete_scores).setVisible(true);
		}
		
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.scores, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_delete_scores:
			localStorage.deleteAll();
			setTabHost();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	public void setTabHost(){
		setContentView(R.layout.scores);
		
		host = (TabHost) findViewById(R.id.tabhost);
		LocalActivityManager mLocalActivityManager = new LocalActivityManager(this, false);
	    mLocalActivityManager.dispatchCreate(instance); // state will be bundle your activity state which you get in onCreate
		host.setup(mLocalActivityManager);
		
		TabSpec spec = host.newTabSpec(getResources().getString(R.string.tab_local));
		spec.setIndicator(getResources().getString(R.string.tab_local));
		
		Intent intent = new Intent().setClass(ScoresActivity.this, ScoresTab1Activity.class);
		spec.setContent(intent);
		host.addTab(spec);
		
		spec = host.newTabSpec(getResources().getString(R.string.tab_global));
		spec.setIndicator(getResources().getString(R.string.tab_global));
		
		intent = new Intent().setClass(ScoresActivity.this, ScoresTab2Activity.class);
		spec.setContent(intent);
		host.addTab(spec);

		
		host.setCurrentTabByTag(getResources().getString(R.string.tab_local));
	}
	
}
