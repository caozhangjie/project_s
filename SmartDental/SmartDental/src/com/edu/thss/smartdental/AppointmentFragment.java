package com.edu.thss.smartdental;

import java.util.Date;

import com.edu.thss.smartdental.ui.calendar.CalendarView;
import com.edu.thss.smartdental.ui.calendar.CalendarView.OnItemClickListener;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class AppointmentFragment extends Fragment{
	CalendarView calendar;
	Button left;
	Button right;
	TextView title;
	private OnClickListener clicklistener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			if(v.getId() == left.getId()){
				title.setText(calendar.clickLeftMonth());
			}
			if(v.getId() == right.getId()){
				title.setText(calendar.clickRightMonth());
			}
		}};
	
	
	public AppointmentFragment(){
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_appointment, container,false);
		this.calendar = (CalendarView)rootView.findViewById(R.id.calendar);
		calendar.setOnItemClickListener(new calendarItemClickListener());
		
		
		left = (Button)rootView.findViewById(R.id.calendar_left);
		left.setOnClickListener(this.clicklistener);
		right = (Button)rootView.findViewById(R.id.calendar_right);
		right.setOnClickListener(this.clicklistener);
		
		title = (TextView)rootView.findViewById(R.id.calendar_title);
		title.setText(calendar.getYearAndmonth());
		return rootView;
	}
	
	class calendarItemClickListener implements OnItemClickListener{

		@Override
		public void OnItemClick(Date date) {
			Intent intent = new Intent(getActivity(),OnedayAppointActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		}  
	 }
}
