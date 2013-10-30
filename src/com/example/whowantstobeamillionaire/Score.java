package com.example.whowantstobeamillionaire;

public class Score {
	private long id;
	private String name;
	private Integer score;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getScore() {
		return score;
	}
	public void setScore(Integer score) {
		this.score = score;
	}
	
	// Will be used by the ArrayAdapter in the ListView
	@Override
	public String toString() {
		return String.valueOf(score);
	}
}
