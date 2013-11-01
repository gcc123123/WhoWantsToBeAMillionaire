package com.example.whowantstobeamillionaire;

import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class PlayActivity extends Activity {
	
	private int currentQuestion;
	private Question question;
	
	private final Handler handler = new Handler();
	
	private final Runnable run = new Runnable() {
	
		@Override
		public void run() {
			setViewData(question);
		}
	};
	
	TextView answer1;
	TextView answer2;
	TextView answer3;
	TextView answer4;
	
	private ScoresDataSource localStorage;
	
	String [] prizes = new String [] {
			"0", "100", "200", "300", "500", "1000", "2000", "4000", "8000", 
			"16000", "32000", "64000", "125000", "250000", "500000", "1000000"
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.play);
		
		App appState = ((App)this.getApplication());
		localStorage = appState.getDatabase();
		
		answer1 = (TextView) findViewById(R.id.textViewAnswer1);
		answer1.setOnClickListener(clickQuestionsHandler);
		answer2 = (TextView) findViewById(R.id.textViewAnswer2);
		answer2.setOnClickListener(clickQuestionsHandler);
		answer3 = (TextView) findViewById(R.id.textViewAnswer3);
		answer3.setOnClickListener(clickQuestionsHandler);
		answer4 = (TextView) findViewById(R.id.textViewAnswer4);
		answer4.setOnClickListener(clickQuestionsHandler);
		
	}

	private Question getQuestion(int currentQuestion) {
		Question q = null;
		
		if(currentQuestion > 15 || currentQuestion < 1){
			return q;
		}
		
		InputStream inputStream = 
				getResources().openRawResource(R.raw.questions0001);
		
		XmlPullParser parser;
		int numberOfQuestions = 1;
		
		try {
			parser = XmlPullParserFactory.newInstance().newPullParser();
			parser.setInput(inputStream, null);
			
			int eventType = XmlPullParser.START_DOCUMENT;
			
			while(eventType != XmlPullParser.END_DOCUMENT){
				if(eventType == XmlPullParser.START_TAG && !"quizz".equals(parser.getName()) 
						&& "question".equals(parser.getName()) ){
					if(currentQuestion == numberOfQuestions){
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
						break;
					}
					numberOfQuestions++;
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
		
		return q;
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
		saveCurrentQuestion(currentQuestion);
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		localStorage.open();
		currentQuestion = restoreCurrentQuestion();
		question= getQuestion(currentQuestion);	
		
		
		if(question != null){
			setViewData(question);
		}
		else{
			// Mostrar error
		}
		
		
	}
	
	private void setViewData(Question question){
		if(question != null){
			TextView textViewQuestionPrize = (TextView) findViewById(R.id.textViewQuestionPrize);
			textViewQuestionPrize.setText(prizes[question.getNumber()]);
			
			TextView textViewQuestionNumber = (TextView) findViewById(R.id.textViewQuestionNumber);
			textViewQuestionNumber.setText(String.valueOf(question.getNumber()));
			
			TextView textViewQuestion = (TextView) findViewById(R.id.textViewQuestion);
			textViewQuestion.setText(question.getText());
			
			TextView textViewAnswer = (TextView) findViewById(R.id.textViewAnswer1);
			textViewAnswer.setText(question.getAnswer1());
			
			textViewAnswer = (TextView) findViewById(R.id.textViewAnswer2);
			textViewAnswer.setText(question.getAnswer2());
			
			textViewAnswer = (TextView) findViewById(R.id.textViewAnswer3);
			textViewAnswer.setText(question.getAnswer3());
			
			textViewAnswer = (TextView) findViewById(R.id.textViewAnswer4);
			textViewAnswer.setText(question.getAnswer4());
		}
	}
	
	private int restoreCurrentQuestion(){
		SharedPreferences preferences = 
				getSharedPreferences("settings", Context.MODE_PRIVATE);
		
		int cQuestion = preferences.getInt("current_question", 1);	
		
		return cQuestion;
	}
	
	private void saveCurrentQuestion(Integer cQuestion){
		SharedPreferences preferences =
				getSharedPreferences("settings", Context.MODE_PRIVATE);
		
		Editor editor = preferences.edit();
		
		editor.putInt("current_question", cQuestion);
		
		editor.commit();
	}
	
	void publishScore(String sco){
		SharedPreferences preferences = 
				getSharedPreferences("settings", Context.MODE_PRIVATE);
		
		String name = preferences.getString("username", null);
		String score = sco;
		
		if(name != null && !name.contentEquals("") && score != null && !score.contentEquals("")){
			
				localStorage.open();
				localStorage.createScore(name, Integer.valueOf(score));
			
				PublishScoreTask publishScoreTask = new PublishScoreTask(this);
				publishScoreTask.execute(name, score);
		}
	}
	
	private View.OnClickListener clickQuestionsHandler = new View.OnClickListener() {
		
		  public void onClick(View v) {
			  int id = ((TextView) v).getId();
			  boolean isCorrect = false;
		      if( answer1.getId() == id && question.getRight() == 1){
		    	  isCorrect = true;
		      }
		      else if( answer2.getId() == id && question.getRight() == 2){
		    	  isCorrect = true;
		      }
		      else if( answer3.getId() == id && question.getRight() == 3){
		    	  isCorrect = true;
		      }
		      else if( answer4.getId() == id && question.getRight() == 4){
		    	  isCorrect = true;
		      }
		      
		      if(isCorrect){
		    	  
		    	  // Publish score
		    	  publishScore( prizes[currentQuestion] );
		    	  
		    	  if(question.getNumber() < 15){
			    	  currentQuestion++;
		    	  }
		    	  else{
			    	  currentQuestion = 1;
		    	  }
		    	  
		    	  question = getQuestion(currentQuestion);
		    	  handler.post(run);
		      }
		      else{
		    	  // New intent...Perdiste
		    	  currentQuestion = 1;
		    	  question = getQuestion(currentQuestion);
		    	  handler.post(run);
		      }
		      
		  }
	};
}
