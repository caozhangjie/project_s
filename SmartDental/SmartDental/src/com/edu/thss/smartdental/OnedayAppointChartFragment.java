package com.edu.thss.smartdental;

import java.util.ArrayList;

import com.edu.thss.smartdental.model.AppointmentElement;
import com.edu.thss.smartdental.ui.calendar.AppointmentChartView;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class OnedayAppointChartFragment extends Fragment {

	ArrayList<AppointmentElement> data; //数据来源
	AppointmentChartView chartView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_oneday_appoint_chart, container,false);
		//chartView = (AppointmentChartView)rootView.findViewById(R.id.app_chartview);
	  
	   
		//chartView.invalidate();
		initData(); //数据初始化
		return rootView;
		
	}
	/**
	 * 初始化数据
	 * */
	private void initData() {
		data = new ArrayList<AppointmentElement>();
	
		AppointmentElement appoint = new AppointmentElement("去正畸科进行第二次会诊","2014-5-15","09:00","10:00");
		data.add(appoint);
		
		appoint = new AppointmentElement("拔牙","2014-5-15","14:00","15:30");
		
		data.add(appoint);
		
	}
}
