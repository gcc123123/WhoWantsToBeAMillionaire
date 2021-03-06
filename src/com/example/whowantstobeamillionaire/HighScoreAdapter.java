package com.example.whowantstobeamillionaire;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class HighScoreAdapter extends BaseAdapter{
	
	private static class ViewHolder
	{
		TextView score;
		TextView name;
	}
	
	private ArrayList<HighScore> data;
	private LayoutInflater inflater = null;
	
	public HighScoreAdapter(Context c, ArrayList<HighScore> data){
		this.data = data;
		this.inflater = LayoutInflater.from(c);
	}
	
	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.score_list_row, null);
			
			holder = new ViewHolder();
			
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.score = (TextView) convertView.findViewById(R.id.score);
			
			convertView.setTag(holder);
		}
		else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		// Setting all values in listview
		holder.name.setText(data.get(position).getName());
		holder.score.setText(String.valueOf(data.get(position).getScoring()));
				
		return convertView;
	}

}
