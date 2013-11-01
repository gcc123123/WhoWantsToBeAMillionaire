package com.example.whowantstobeamillionaire;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ScoresDataSource {
	
	// Database fields
	private SQLiteDatabase database;
	private ScoresSQLiteHelper dbHelper;
	private String[] allColumns = { 
			
			ScoresSQLiteHelper.COLUMN_ID,
			ScoresSQLiteHelper.COLUMN_NAME,
			ScoresSQLiteHelper.COLUMN_SCORE 
	};
	
	public ScoresDataSource(Context context){
		dbHelper = new ScoresSQLiteHelper(context);
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	public void close(){
		dbHelper.close();
	}
	
	public Score createScore(String name, Integer score){
		ContentValues values = new ContentValues();
		values.put(ScoresSQLiteHelper.COLUMN_NAME, name);
		values.put(ScoresSQLiteHelper.COLUMN_SCORE, score);
		
		long insertId = database.insert(
				ScoresSQLiteHelper.TABLE_SCORES, 
				null, values);
		
		Cursor cursor = database.query(
				ScoresSQLiteHelper.TABLE_SCORES, allColumns,
				ScoresSQLiteHelper.COLUMN_ID + " = " + insertId, 
				null, null, null, null, null);
		cursor.moveToFirst();
		Score scoreObject = cursorToScore(cursor);
		
		return scoreObject;
	}
	
	public void deleteScore(Score score){
		long id = score.getId();
		database.delete(ScoresSQLiteHelper.TABLE_SCORES,
				ScoresSQLiteHelper.COLUMN_ID + " = " + id, null);
	}
	
	public void deleteAll(){
		database.delete(ScoresSQLiteHelper.TABLE_SCORES, null, null);
	}
	
	public List<Score> getAllScores(){
		List<Score> scores = new ArrayList<Score>();
		
		Cursor cursor = database.query(
				ScoresSQLiteHelper.TABLE_SCORES, allColumns, 
				null, null, null, null, ScoresSQLiteHelper.COLUMN_SCORE + " DESC");
		cursor.moveToFirst();
		while(!cursor.isAfterLast()){
			Score score = cursorToScore(cursor);
			scores.add(score);
			cursor.moveToNext();
		}
		cursor.close();
		
		return scores;
	}
	
	private Score cursorToScore(Cursor cursor){
		Score score = new Score();
		score.setId(cursor.getLong(0));
		score.setName(cursor.getString(1));
		score.setScore(cursor.getInt(2));
		
		return score;
	}
	
}
