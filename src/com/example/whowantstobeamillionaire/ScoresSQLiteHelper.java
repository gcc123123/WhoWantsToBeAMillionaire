package com.example.whowantstobeamillionaire;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ScoresSQLiteHelper extends SQLiteOpenHelper {

	public static final String TABLE_SCORES = "scores";
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_SCORE = "score";
	
	private static final String DATABASE_NAME = "scores.db";
	private static final int DATABASE_VERSION = 1;
	
	String sqlCreate = "CREATE TABLE " + TABLE_SCORES  + "(" +
			COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			COLUMN_NAME + " TEXT NOT NULL, " +
			COLUMN_SCORE + " INTEGER NOT NULL" +
			");";
	
	public ScoresSQLiteHelper(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(sqlCreate);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	    //Se elimina la versión anterior de la tabla
        db.execSQL("DROP TABLE IF EXISTS Scores");
		
        // Se crea la nueva versión de la tabla
        onCreate(db);
        
	}

}
