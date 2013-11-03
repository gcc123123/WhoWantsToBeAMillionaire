package com.example.whowantstobeamillionaire;

import java.util.ArrayList;
import android.os.Bundle;
import android.app.ListActivity;

public class ScoresTab1Activity extends ListActivity {

	private ScoresDataSource localStorage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		App appState = (App) this.getApplication();
		localStorage = appState.getDatabase();
		localStorage.open();
		ArrayList<Score> values = localStorage.getAllScores();
		
		ScoreAdapter adapter = new ScoreAdapter(ScoresTab1Activity.this, values);
		
		setListAdapter(adapter);
	}
	
	public String[] generateScores(){
		return new String[] {"1000000", "500000", "250000", "250000", 
				"250000", "250000", "250000", "250000", "250000"
		};
	}
}
