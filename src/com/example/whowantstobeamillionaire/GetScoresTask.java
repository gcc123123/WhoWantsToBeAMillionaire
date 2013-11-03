package com.example.whowantstobeamillionaire;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

public class GetScoresTask extends AsyncTask<String, Integer, Boolean> {

	private static final String DEBUG_TAG = "HelloWorldDebug";
	
	HighScoreList highScoreList;
	Context context;
	ScoresTab2Activity activity;
	Activity parent;
	
	public GetScoresTask(Context context, ScoresTab2Activity a, Activity parent) {
		this.context = context;
		this.activity = a;
		this.parent = parent;
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		
		parent.setProgressBarIndeterminate(false); 
		parent.setProgressBarIndeterminateVisibility(false);
		
		if(result == true && highScoreList != null){

			List<HighScore> highScores = highScoreList.getScores();
			Collections.sort(highScores);
			activity.addScores(highScores);
		}
		else{
			CharSequence text = "Error retrieving scores from server.";
			int duration = Toast.LENGTH_SHORT;

			Toast toast = Toast.makeText(context, text, duration);
			toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
			toast.show();
		}
		
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		parent.setProgressBarIndeterminate(true); 
		parent.setProgressBarIndeterminateVisibility(true);
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
	}

	@Override
	protected Boolean doInBackground(String... params) {
		HttpClient client= new DefaultHttpClient();
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		String name = params[0];
		pairs.add(new BasicNameValuePair("name", name));
		HttpGet request = 
				new HttpGet("http://wwtbamandroid.appspot.com/rest/highscores?"
						+ URLEncodedUtils.format(pairs, "utf-8"));
		try {
			HttpResponse response = client.execute(request);
			Log.d(DEBUG_TAG, String.valueOf(response.getStatusLine().getStatusCode()));
			if(response.getStatusLine().getStatusCode() != 200){
				return false;
			}
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream stream = entity.getContent(); 
				BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				stream.close();
				String responseString = sb.toString();
				GsonBuilder builder = new GsonBuilder();
				Gson gson = builder.create();
				JSONObject json = new JSONObject(responseString);
				Log.d(DEBUG_TAG, json.toString());
				highScoreList = gson.fromJson(json.toString(), HighScoreList.class);
			}
			
		} catch (UnsupportedEncodingException e) {
			return false;
		} catch (ClientProtocolException e) {
			return false;
		} catch (IOException e) {
			return false;
		} catch (JSONException e) {
			return false;
		} catch (JsonSyntaxException e){
			return false;
		}
		
		return true;
	}
	

}
