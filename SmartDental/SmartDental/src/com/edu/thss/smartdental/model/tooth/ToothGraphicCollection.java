package com.edu.thss.smartdental.model.tooth;

import java.util.ArrayList;

public class ToothGraphicCollection {
	ArrayList<ToothGraphic> toothgraphiclist;
	public ToothGraphic getToothGraphics(int index){
		return this.toothgraphiclist.get(index);
	}
	public void setToothGraphics(int index,ToothGraphic tooth){
		this.toothgraphiclist.set(index, tooth);
	}
	/*
	public ToothGraphic getToothGraphics(String toothID) {
		for(int i=0;i<this.toothgraphiclist.size();i++){
			if(this.toothgraphiclist.get(i).toothID ==toothID){
				return this.toothgraphiclist.get(i);
			}
		}
		return null;
	}*/
	public int getCount(){
		return this.toothgraphiclist.size();
	}
	public void Add(ToothGraphic value) {
		this.toothgraphiclist.add(value);
	}

	public int IndexOf(ToothGraphic value) {
		return this.toothgraphiclist.indexOf(value);
	}

	///<summary></summary>
	public void Insert(int index,ToothGraphic value) {
		//List.Insert(index,value);
		//this.toothgraphiclist.s
	}

	///<summary></summary>
	public void Remove(ToothGraphic value) {
		this.toothgraphiclist.remove(value);
	}

	public boolean Contains(ToothGraphic value) {
		return this.toothgraphiclist.contains(value);
	}

}
