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
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.os.AsyncTask;
import android.view.Gravity;
import android.widget.Toast;

public class AddFriendTask extends AsyncTask<String, Integer, Boolean> {

	Context context;
	String friend_name;
	
	public AddFriendTask(Context context){
		this.context = context;
	}
	
	@Override
	protected Boolean doInBackground(String... arg0) {
		// TODO Auto-generated method stub
		HttpClient client= new DefaultHttpClient();
		HttpPost request = 
				new HttpPost("http://wwtbamandroid.appspot.com/rest/friends");
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		String name = arg0[0];
		friend_name = arg0[1];
		pairs.add(new BasicNameValuePair("name", name));
		pairs.add(new BasicNameValuePair("friend_name", friend_name));
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
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}

	@Override
	protected void onPostExecute(Boolean result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		
		CharSequence text = "Added " + friend_name + " as friend.";
		if(result == false){
			text = "Error adding " + friend_name + " as friend.";
		}
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, text, duration);
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		toast.show();
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}

}
