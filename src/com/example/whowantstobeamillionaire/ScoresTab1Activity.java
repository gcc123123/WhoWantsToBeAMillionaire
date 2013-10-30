package com.example.whowantstobeamillionaire;

import java.util.List;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.app.ListActivity;

public class ScoresTab1Activity extends ListActivity {

	private ScoresDataSource localStorage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		App appState = (App) this.getApplication();
		localStorage = appState.getDatabase();
		localStorage.open();
		List<Score> values = localStorage.getAllScores();
		
		setListAdapter(new ArrayAdapter<Score>(this, R.layout.scores_tab1, values));
	}
	
	public String[] generateScores(){
		return new String[] {"1000000", "500000", "250000", "250000", 
				"250000", "250000", "250000", "250000", "250000"
		};
	}
}
