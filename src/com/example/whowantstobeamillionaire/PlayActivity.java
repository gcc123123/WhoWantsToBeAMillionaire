package com.example.whowantstobeamillionaire;

import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v4.app.ActivityCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class PlayActivity extends Activity {
	
	private int currentQuestion;
	private Question question;
	private String checkpoint;
	private int currentGrants;
	private int globalGrants;
	private TextView answer1;
	private TextView answer2;
	private TextView answer3;
	private TextView answer4;
	
	protected static final int MENSAJEID = 0x100;
	public static final String CURRENT_QUESTION = "current_question";
	public static final String CURRENT_GRANTS = "current_grants"; 
	
	private final Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
	        switch (msg.what) {
	        	case MENSAJEID:
	        		int correct = msg.arg1;
	        		int id = msg.arg2;
	        		Question question = (Question) msg.obj;
	        		highlightAnswer(correct, id, question);
	                break;
	            default:
	            	break;
	        }
			super.handleMessage(msg);
		}
		
	};
	
	private final Runnable run = new Runnable() {
	
		@Override
		public void run() {
			setViewData(question);
		}
	};
	
	private final Runnable runEndGameWrong = new Runnable() {
		
		@Override
		public void run() {
			Intent endGameIntent = new Intent(PlayActivity.this, 
					EndActivity.class);
			endGameIntent.putExtra("result", R.string.end_wrong_answer);
			endGameIntent.putExtra("prize", PlayActivity.this.checkpoint);
			endGameIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			startActivity(endGameIntent);
		}
	};
	
	private final Runnable runEndGameWin = new Runnable() {
		
		@Override
		public void run() {
			Intent endGameIntent = new Intent(PlayActivity.this, 
					EndActivity.class);
			endGameIntent.putExtra("result", R.string.end_win);
			endGameIntent.putExtra("prize", PlayActivity.this.prizes[15]);
			endGameIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			startActivity(endGameIntent);
		}
	};
	
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
		if(currentGrants == globalGrants){
			menu.getItem(0).setEnabled(false);
			menu.getItem(1).setEnabled(false);
			menu.getItem(2).setEnabled(false);
		}
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		TextView textViewAnswer;
		switch(item.getItemId()){
			case R.id.action_phone:
				int phone = question.getPhone();
				switch(phone){
					case 1:
						textViewAnswer = (TextView) findViewById(R.id.textViewAnswer1);
						textViewAnswer.setBackgroundColor(getResources().getColor(R.color.color_phone));
						break;
					case 2:
						textViewAnswer = (TextView) findViewById(R.id.textViewAnswer1);
						textViewAnswer.setBackgroundColor(getResources().getColor(R.color.color_phone));
						break;
					case 3:
						textViewAnswer = (TextView) findViewById(R.id.textViewAnswer1);
						textViewAnswer.setBackgroundColor(getResources().getColor(R.color.color_phone));
						break;
					case 4:
						textViewAnswer = (TextView) findViewById(R.id.textViewAnswer1);
						textViewAnswer.setBackgroundColor(getResources().getColor(R.color.color_phone));
						break;						
				}
				
				currentGrants++;
				item.setEnabled(false);
				
				if(currentGrants == globalGrants){
					ActivityCompat.invalidateOptionsMenu(PlayActivity.this);
				}
					
				return true;
			case R.id.action_50_percent:
			
				int fifty1 = question.getFifty1();
				int fifty2 = question.getFifty2();
				
				if(fifty1 == 1 || fifty2 == 1){
					textViewAnswer = (TextView) findViewById(R.id.textViewAnswer1);
					textViewAnswer.setVisibility(View.INVISIBLE);
				}
				if(fifty1 == 2 || fifty2 == 2){
					textViewAnswer = (TextView) findViewById(R.id.textViewAnswer2);
					textViewAnswer.setVisibility(View.INVISIBLE);
				}
				if(fifty1 == 3 || fifty2 == 3){
					textViewAnswer = (TextView) findViewById(R.id.textViewAnswer3);
					textViewAnswer.setVisibility(View.INVISIBLE);
				}
				if(fifty1 == 4 || fifty2 == 4){
					textViewAnswer = (TextView) findViewById(R.id.textViewAnswer4);
					textViewAnswer.setVisibility(View.INVISIBLE);
				}
				
				currentGrants++;
				item.setEnabled(false);
				
				if(currentGrants == globalGrants){
					ActivityCompat.invalidateOptionsMenu(PlayActivity.this);
				}
				
				return true;
			case R.id.action_audience:
				
				int audience = question.getAudience();
				switch(audience){
					case 1:
						textViewAnswer = (TextView) findViewById(R.id.textViewAnswer1);
						textViewAnswer.setBackgroundColor(getResources().getColor(R.color.color_audience));
						break;
					case 2:
						textViewAnswer = (TextView) findViewById(R.id.textViewAnswer1);
						textViewAnswer.setBackgroundColor(getResources().getColor(R.color.color_audience));
						break;
					case 3:
						textViewAnswer = (TextView) findViewById(R.id.textViewAnswer1);
						textViewAnswer.setBackgroundColor(getResources().getColor(R.color.color_audience));
						break;
					case 4:
						textViewAnswer = (TextView) findViewById(R.id.textViewAnswer1);
						textViewAnswer.setBackgroundColor(getResources().getColor(R.color.color_audience));
						break;						
				}
				
				currentGrants++;
				item.setEnabled(false);
				
				if(currentGrants == globalGrants){
					ActivityCompat.invalidateOptionsMenu(PlayActivity.this);
				}
				
				return true;
			case R.id.action_end_game:
				Intent endGame = new Intent(PlayActivity.this, EndActivity.class);
				endGame.putExtra("result", R.string.end_exit);
				endGame.putExtra("prize", prizes[currentQuestion-1]);
				endGame.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				if(currentQuestion-1 > 0){
					publishScore(prizes[currentQuestion-1]);
				}
				currentGrants = 0;
				currentQuestion = 1;
				question = getQuestion(currentQuestion);
				startActivity(endGame);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		
		localStorage.close();
		saveCurrentQuestion(currentQuestion);
		saveCurrentGrants(currentGrants);
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		globalGrants = restoreGlobalGrants();
		currentGrants = restoreCurrentGrants();
		
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
			
			TextView textViewIfLose = (TextView) findViewById(R.id.textViewLosePrize);
			String checkpoint = "0";
			if(currentQuestion > 5 && currentQuestion <= 10){
				checkpoint = prizes[5];
			}
			else if(currentQuestion >  10){
				checkpoint = prizes[10];
			}
			textViewIfLose.setText(checkpoint);
			
			TextView textViewIfEnd = (TextView) findViewById(R.id.textViewEndPrize);
			textViewIfEnd.setText(prizes[question.getNumber()-1]);
			
			TextView textViewQuestion = (TextView) findViewById(R.id.textViewQuestion);
			textViewQuestion.setText(question.getText());
			
			TextView textViewAnswer = (TextView) findViewById(R.id.textViewAnswer1);
			textViewAnswer.setText(question.getAnswer1());
			textViewAnswer.setBackgroundColor(getResources().getColor(R.color.color_button));
			textViewAnswer.setVisibility(View.VISIBLE);
			
			textViewAnswer = (TextView) findViewById(R.id.textViewAnswer2);
			textViewAnswer.setText(question.getAnswer2());
			textViewAnswer.setBackgroundColor(getResources().getColor(R.color.color_button));
			textViewAnswer.setVisibility(View.VISIBLE);
			
			textViewAnswer = (TextView) findViewById(R.id.textViewAnswer3);
			textViewAnswer.setText(question.getAnswer3());
			textViewAnswer.setBackgroundColor(getResources().getColor(R.color.color_button));
			textViewAnswer.setVisibility(View.VISIBLE);
			
			textViewAnswer = (TextView) findViewById(R.id.textViewAnswer4);
			textViewAnswer.setText(question.getAnswer4());
			textViewAnswer.setBackgroundColor(getResources().getColor(R.color.color_button));
			textViewAnswer.setVisibility(View.VISIBLE);
			
			
			ActivityCompat.invalidateOptionsMenu(PlayActivity.this);
		}
	}
	
	private void highlightAnswer(int correct, int id, Question question){
		TextView textViewAnswer = (TextView) findViewById(id);
		if(correct == 1){
			textViewAnswer.setBackgroundColor(getResources().getColor(R.color.question_correct));
		}
		else{
			textViewAnswer.setBackgroundColor(getResources().getColor(R.color.question_wrong));
			TextView textViewCorrectAnswer = null;
			switch(question.getRight()){
				case 1:
					textViewCorrectAnswer = (TextView) findViewById(R.id.textViewAnswer1);
					break;
				case 2:
					textViewCorrectAnswer = (TextView) findViewById(R.id.textViewAnswer2);
					break;
				case 3:
					textViewCorrectAnswer = (TextView) findViewById(R.id.textViewAnswer3);
					break;
				case 4:
					textViewCorrectAnswer = (TextView) findViewById(R.id.textViewAnswer4);
					break;
			}
			if(textViewCorrectAnswer != null){
				textViewCorrectAnswer.setBackgroundColor(
						getResources().getColor(R.color.question_correct));
			}
		}
	}
	
	private int restoreCurrentQuestion(){
		SharedPreferences preferences = 
				getSharedPreferences(SettingsActivity.SETTINGS_FILE, Context.MODE_PRIVATE);
		
		int cQuestion = preferences.getInt(CURRENT_QUESTION, 1);	
		
		return cQuestion;
	}
	
	private int restoreGlobalGrants(){
		SharedPreferences preferences = 
				getSharedPreferences(SettingsActivity.SETTINGS_FILE, Context.MODE_PRIVATE);
		
		int grants = preferences.getInt(SettingsActivity.GRANTS, 1);
		return grants;
	}
	
	private int restoreCurrentGrants(){
		SharedPreferences preferences = 
				getSharedPreferences(SettingsActivity.SETTINGS_FILE, Context.MODE_PRIVATE);
		
		int grants = preferences.getInt(CURRENT_GRANTS, 0);
		
		return grants;
	}
	
	private void saveCurrentQuestion(Integer cQuestion){
		SharedPreferences preferences =
				getSharedPreferences(SettingsActivity.SETTINGS_FILE, Context.MODE_PRIVATE);
		
		Editor editor = preferences.edit();
		
		editor.putInt(CURRENT_QUESTION, cQuestion);
		
		editor.commit();
	}
	
	private void saveCurrentGrants(int currentGrants){
		SharedPreferences preferences = 
				getSharedPreferences(SettingsActivity.SETTINGS_FILE, Context.MODE_PRIVATE);
		
		Editor editor = preferences.edit();
		
		editor.putInt(CURRENT_GRANTS, currentGrants);
		
		editor.commit();
	}
	
	void publishScore(String sco){
		SharedPreferences preferences = 
				getSharedPreferences(SettingsActivity.SETTINGS_FILE, Context.MODE_PRIVATE);
		
		String name = preferences.getString(SettingsActivity.USERNAME, null);
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
		    	  
		    	  question = getQuestion(currentQuestion);
		    	  Message msg = new Message();
		    	  msg.what = MENSAJEID;

		    	  msg.arg1 = 1;
		    	  msg.arg2 = id;
		    	  msg.obj = question;
		    			
		    	  handler.sendMessage(msg);
		    	 
		    	  if(question.getNumber() < 15){
			    	  currentQuestion++;
			    	  question = getQuestion(currentQuestion);
			    	  handler.postDelayed(run, 500);
		    	  }
		    	  else{
		    		  // Publish score
			    	  publishScore( prizes[currentQuestion] );
			    	  handler.postDelayed(runEndGameWin, 500);
			    	  
			    	  currentQuestion = 1;
			    	  currentGrants = 0;
			    	  question = getQuestion(currentQuestion);
		    	  }
		      }
		      else{
		    	  if(currentQuestion > 5 && currentQuestion <= 10){
		    		  publishScore( prizes[5] );
		    		  checkpoint = prizes[5];
		    	  }
		    	  else if(currentQuestion > 10){
		    		  publishScore( prizes[10] );
		    		  checkpoint = prizes[10];
		    	  }
		    	  else{
		    		  checkpoint = prizes[0];
		    	  }
		    	  
		    	  Message msg = new Message();
		    	  msg.what = MENSAJEID;
		    	  msg.obj = question;
		    	  msg.arg1 = 0;
		    	  msg.arg2 = id;

		    	  handler.sendMessage(msg);
		    	  handler.postDelayed(runEndGameWrong, 1000);
		    	  
		    	  currentGrants = 0;
		    	  currentQuestion = 1;
		    	  question = getQuestion(currentQuestion);
		      }
		      
		  }
	};
}
