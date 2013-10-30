package com.example.whowantstobeamillionaire;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Menu;
import android.widget.TextView;

public class PlayActivity extends Activity {
	
	private int currentQuestion;
	private List<Question> questions;
	
	private ScoresDataSource localStorage;
	
	String [] prizes = new String [] {
			"0", "100", "200", "300", "500", "1000", "2000", "4000", "8000", 
			"16000", "32000", "64000", "125000", "250000", "500000", "1000000"
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.play);
		
		questions = generateQuestionList2();
		
		App appState = ((App)this.getApplication());
		localStorage = appState.getDatabase();
		localStorage.open();
		localStorage.createScore("hola", 1000000);
		localStorage.createScore("hola", 1000000);
		localStorage.createScore("hola", 1000000);
		
		SharedPreferences preferences = 
				getSharedPreferences("settings", Context.MODE_PRIVATE);
		String name = preferences.getString("username", null);
		String score = "100000";
		if(name != null && !name.contentEquals("") && score != null && !score.contentEquals("")){
				PublishScoreTask publishScoreTask = new PublishScoreTask(this);
				publishScoreTask.execute(name, score);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.play, menu);
		return true;
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		localStorage.close();
		saveData();
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		localStorage.open();
		restoreData();		

		TextView textViewQuestionPrize = (TextView) findViewById(R.id.textViewQuestionPrize);
		textViewQuestionPrize.setText(prizes[currentQuestion+1]);
		
		TextView textViewQuestionNumber = (TextView) findViewById(R.id.textViewQuestionNumber);
		textViewQuestionNumber.setText(String.valueOf(questions.get(currentQuestion).getNumber()));
		
		TextView textViewQuestion = (TextView) findViewById(R.id.textViewQuestion);
		textViewQuestion.setText(questions.get(currentQuestion).getText());
		
		TextView textViewAnswer = (TextView) findViewById(R.id.textViewAnswer1);
		textViewAnswer.setText(questions.get(currentQuestion).getAnswer1());
		
		textViewAnswer = (TextView) findViewById(R.id.textViewAnswer2);
		textViewAnswer.setText(questions.get(currentQuestion).getAnswer2());
		
		textViewAnswer = (TextView) findViewById(R.id.textViewAnswer3);
		textViewAnswer.setText(questions.get(currentQuestion).getAnswer3());
		
		textViewAnswer = (TextView) findViewById(R.id.textViewAnswer4);
		textViewAnswer.setText(questions.get(currentQuestion).getAnswer4());
		
		
	}
	
	private void restoreData(){
		SharedPreferences preferences = 
				getSharedPreferences("settings", Context.MODE_PRIVATE);
		
		currentQuestion = preferences.getInt("current_question", 0);
		
	}
	
	private void saveData(){
		SharedPreferences preferences =
				getSharedPreferences("settings", Context.MODE_PRIVATE);
		
		Editor editor = preferences.edit();
		
		editor.putInt("current_question", currentQuestion);
		
		editor.commit();
	}
	
	public List<Question> generateQuestionList2(){
		
		List<Question> list = new ArrayList<Question>();
		Question q = null;
		
		InputStream inputStream = 
				getResources().openRawResource(R.raw.questions0001);
		
		XmlPullParser parser;
		
		try {
			parser = XmlPullParserFactory.newInstance().newPullParser();
			parser.setInput(inputStream, null);
			
			int eventType = XmlPullParser.START_DOCUMENT;
			
			while(eventType != XmlPullParser.END_DOCUMENT){
				if(eventType == XmlPullParser.START_TAG && !"quizz".equals(parser.getName())){
					
					q = new Question(
							parser.getAttributeValue(null, "number"), 
							parser.getAttributeValue(null, "text"),
							parser.getAttributeValue(null, "answer1"),
							parser.getAttributeValue(null, "answer2"),
							parser.getAttributeValue(null, "answer3"),
							parser.getAttributeValue(null, "answer4"),
							parser.getAttributeValue(null, "right"), 
							parser.getAttributeValue(null, "audience"), 
							parser.getAttributeValue(null, "phone"), 
							parser.getAttributeValue(null, "fifty1"), 
							parser.getAttributeValue(null, "fifty2")
							);
					list.add(q);
				}
				eventType = parser.next();
			}
			
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		return list;
	}

	public List<Question> generateQuestionList() {
		List<Question> list = new ArrayList<Question>();
		Question q = null;
		
		q = new Question(
				"1", 
				"Which is the Sunshine State of the US?",
				"North Carolina",
		        "Florida",
		        "Texas",
		        "Arizona",
				"2", 
				"2", 
				"2", 
				"1", 
				"4"
				);
		list.add(q);

		q = new Question(
				"2", 
				"Which of these is not a U.S. state?",
		        "New Hampshire",
		        "Washington",
		        "Wyoming",
		        "Manitoba",
				"4", 
				"4", 
				"4", 
				"2", 
				"3"
				);
		list.add(q);

		q = new Question(
				"3", 
				"What is Book 3 in the Pokemon book series?",
		        "Charizard",
		        "Island of the Giant Pokemon",
		        "Attack of the Prehistoric Pokemon",
		        "I Choose You!",
				"3", 
				"2", 
				"3", 
				"1", 
				"4"
				);
		list.add(q);

		q = new Question(
				"4", 
				"Who was forced to sign the Magna Carta?",
		        "King John",
		        "King Henry VIII",
		        "King Richard the Lion-Hearted",
		        "King George III",
				"1", 
				"3", 
				"1", 
				"2", 
				"3"
				);
		list.add(q);

		q = new Question(
				"5", 
				"Which ship was sunk in 1912 on its first voyage, although people said it would never sink?",
		        "Monitor",
		        "Royal Caribean",
		        "Queen Elizabeth",
		        "Titanic",
				"4", 
				"4", 
				"4", 
				"1", 
				"2"
				);
		list.add(q);

		q = new Question(
				"6", 
				"Who was the third James Bond actor in the MGM films? (Do not include &apos;Casino Royale&apos;.)",
		        "Roger Moore",
		        "Pierce Brosnan",
		        "Timothy Dalton",
		        "Sean Connery",
				"1", 
				"3", 
				"3", 
				"2", 
				"3"
				);
		list.add(q);

		q = new Question(
				"7", 
				"Which is the largest toothed whale?",
		        "Humpback Whale",
		        "Blue Whale",
		        "Killer Whale",
		        "Sperm Whale",
				"4", 
				"2", 
				"2", 
				"2", 
				"3"
				);
		list.add(q);

		q = new Question(
				"8", 
				"In what year was George Washington born?",
		        "1728",
		        "1732",
		        "1713",
		        "1776",
				"2", 
				"2", 
				"2", 
				"1", 
				"4"
				);
		list.add(q);

		q = new Question(
				"9", 
				"Which of these rooms is in the second floor of the White House?",
		        "Red Room",
		        "China Room",
		        "State Dining Room",
		        "East Room",
				"2", 
				"2", 
				"2", 
				"3", 
				"4"
				);
		list.add(q);

		q = new Question(
				"10", 
				"Which Pope began his reign in 963?",
		        "Innocent III",
		        "Leo VIII",
		        "Gregory VII",
		        "Gregory I",
				"2", 
				"1", 
				"2", 
				"3", 
				"4"
				);
		list.add(q);

		q = new Question(
				"11", 
				"What is the second longest river in South America?",
		        "Parana River",
		        "Xingu River",
		        "Amazon River",
		        "Rio Orinoco",
				"1", 
				"1", 
				"1", 
				"2", 
				"3"
				);
		list.add(q);

		q = new Question(
				"12", 
				"What Ford replaced the Model T?",
		        "Model U",
		        "Model A",
		        "Edsel",
		        "Mustang",
				"2", 
				"4", 
				"4", 
				"1", 
				"3"
				);
		list.add(q);

		q = new Question(
				"13", 
				"When was the first picture taken?",
		        "1860",
		        "1793",
		        "1912",
		        "1826",
				"4", 
				"4", 
				"4", 
				"1", 
				"3"
				);
		list.add(q);

		q = new Question(
				"14", 
				"Where were the first Winter Olympics held?",
		        "St. Moritz, Switzerland",
		        "Stockholm, Sweden",
		        "Oslo, Norway",
		        "Chamonix, France",
				"4", 
				"1", 
				"4", 
				"2", 
				"3"
				);
		list.add(q);

		q = new Question(
				"15", 
				"Which of these is not the name of a New York tunnel?",
		        "Brooklyn-Battery",
		        "Lincoln",
		        "Queens Midtown",
		        "Manhattan",
				"4", 
				"4", 
				"4", 
				"1", 
				"3"
				);
		list.add(q);
		
		return list;
	}
	

}
