package com.example.whowantstobeamillionaire;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.os.AsyncTask;
import android.view.Gravity;
import android.widget.Toast;

public class PublishScoreTask extends AsyncTask<String, Integer, Boolean> {
	
	Context context;
	String score;
	
	public PublishScoreTask(Context context){
		this.context = context;
	}
	
	
	@Override
	protected Boolean doInBackground(String... arg0) {
		// TODO Auto-generated method stub
		HttpClient client= new DefaultHttpClient();
		HttpPut request = 
				new HttpPut("http://wwtbamandroid.appspot.com/rest/highscores");
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		String name = arg0[0];
		score = arg0[1];
		pairs.add(new BasicNameValuePair("name", name));
		pairs.add(new BasicNameValuePair("score", score));
		try {
			request.setEntity(new UrlEncodedFormEntity(pairs));
			HttpResponse response = client.execute(request);
			if(response.getStatusLine().getStatusCode() != 204){
				return false;
			}
			// TODO Si la respuesta no es 200 -> Mostrar el error en la interfaz
			// TODO Mostrar que se ha guardado el amigo
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		
		CharSequence text = "Published score: " + score;
		if(result == false){
			text = "Error publishing score: " + score;
		}
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, text, duration);
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		toast.show();
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}

}
