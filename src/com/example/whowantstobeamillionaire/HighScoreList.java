package com.example.whowantstobeamillionaire;

import java.util.List;

public class HighScoreList {

	private List<HighScore> scores;
	
	public HighScoreList(List<HighScore> scores){
		this.scores = scores;
	}

	public List<HighScore> getScores() {
		return scores;
	}

	public void setScores(List<HighScore> scores) {
		this.scores = scores;
	}
	
}
