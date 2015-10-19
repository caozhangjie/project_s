package com.edu.thss.smartdental.adapter;

import java.util.ArrayList;

import com.edu.thss.smartdental.model.AppointmentElement;
import com.edu.thss.smartdental.ui.calendar.SlideView;

import com.edu.thss.smartdental.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

/**
 * 用于预约显示的适配器
 * */
public class AppointListAdapter extends BaseAdapter{
	public ArrayList<AppointmentElement> data;
	private Context context;
	private float downX;
	private float upX;
	private View view;

	public AppointListAdapter(ArrayList<AppointmentElement> data,Context context){
		this.data = data;
		this.context = context;
		
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
		ViewHolder holder = null;
		SlideView slideView = (SlideView) convertView;
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.appoint_list_item, null);
		    
			//slideView = new SlideView();
			holder = new ViewHolder();
			holder.name = (TextView)convertView.findViewById(R.id.appoint_item_title);
		    holder.date = (TextView)convertView.findViewById(R.id.appoint_item_date);
		    holder.start = (TextView)convertView.findViewById(R.id.appoint_item_start);
		    holder.end = (TextView)convertView.findViewById(R.id.appoint_item_end);
		    
		    
		    convertView.setTag(holder);
		}
		else{
			holder = (ViewHolder) convertView.getTag();
		}
	    //--------显示数据------------
	    holder.name.setText(data.get(position).name);
	    holder.date.setText(data.get(position).date);
	    holder.start.setText(data.get(position).startTime);
	    holder.end.setText(data.get(position).endTime);
	    
	    
	    //-----监听事件-----------
	    convertView.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				ViewHolder holder = (ViewHolder) v.getTag();
				switch(event.getAction()){
				case MotionEvent.ACTION_DOWN: //手指按下
					downX = event.getX();
					/*if(holder.name != null)
						holder.name.setVisibility(View.GONE);
					if(holder.date != null)
						holder.date.setVisibility(View.GONE);
					if(holder.end != null)
						holder.end.setVisibility(View.GONE);
					if(holder.start != null)
						holder.start.setVisibility(View.GONE);
						*/
					if(holder.delete != null)
						holder.delete.setVisibility(View.GONE);
					if(holder.done != null)
						holder.done.setVisibility(View.GONE);
					break;
				case MotionEvent.ACTION_UP: //手指离开
					upX = event.getX();
					break;
				} 
				if(holder.delete != null){
					if(Math.abs(downX-upX)>35){
						holder.delete.setVisibility(View.VISIBLE);
						holder.done.setVisibility(View.VISIBLE);
						return true;
					}
					return false;
				}
				return false;
			}});
		return convertView;
				
	}

	private class ViewHolder{
		TextView name;
		TextView date;
		TextView start;
		TextView end;
		Button delete;
		Button done;
	}
}
