package com.example.whowantstobeamillionaire;

import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import android.app.ListActivity;
import android.content.Context;
import android.content.SharedPreferences;


public class ScoresTab2Activity extends ListActivity {

	private List<HighScore> highScores; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
				
		highScores = new ArrayList<HighScore>();
		
		setListAdapter(new ArrayAdapter<HighScore>(this, R.layout.scores_tab2, highScores));
		
		SharedPreferences preferences = 
				getSharedPreferences("settings", Context.MODE_PRIVATE);
		String name = preferences.getString("username", null);
		
		GetScoresTask getScoresTask = new GetScoresTask(this, this, getParent());
		getScoresTask.execute(name);
	}

	public void addScores(List<HighScore> list){
		 highScores.addAll(list);
		 @SuppressWarnings("unchecked")
		 final ArrayAdapter<HighScore> adapter = (ArrayAdapter<HighScore>) getListAdapter();
		 adapter.notifyDataSetChanged();
	}


	public String[] generateScores(){
		return new String[] {"1000000", "1000000", "1000000",
				"1000000", "1000000", "1000000", "1000000",
				"1000000", "1000000", "1000000", "500000", "250000", "250000", 
				"250000", "250000", "250000", "250000", "250000"
		};
	}
}
