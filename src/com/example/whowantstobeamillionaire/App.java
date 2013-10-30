package com.example.whowantstobeamillionaire;

import android.app.Application;

public class App extends Application{

	private static App _instance;
	private ScoresDataSource _localStorage;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		_instance = this;
	}
	
	public static App getInstance(){
		
		// Exposes a mechanism to get instance of the
		// custom application object
		return _instance;
	}
	
	public synchronized ScoresDataSource getDatabase(){
		if(_localStorage == null){
			_localStorage = new ScoresDataSource(this);
		}
		
		return _localStorage;
	}

}
