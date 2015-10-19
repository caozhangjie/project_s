package com.edu.thss.smartdental.model;

public class AppointmentElement {

	public String name;
	public String date;
	public String startTime;
	public String endTime;
	
	public AppointmentElement(String name,String data,String start,String end){
		this.name = name;
		this.date = data;
		this.startTime = start;
		this.endTime = end;
	}
}
