package com.edu.thss.smartdental.ui.calendar;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;

public class CalendarSurface {
	public float density;
	public int width;
	public int height;
	public float monthHeight;
	public float weekHeight;
	public float cellWidth;
	public float cellHeight;
	public float borderWidth;
	public int bgColor = Color.parseColor("#FFFFFF");
	public int textColor = Color.BLACK;
	public int btnColor = Color.parseColor("#666666");
	public int borderColor = Color.parseColor("#CCCCCC");
	public int todayNumberColor = Color.parseColor("#00bfff");
	public int cellDownColor = Color.parseColor("#CCFFFF");
	public int cellSelectedColor = Color.parseColor("#DCDCDC");
	public int weekBgColor = Color.parseColor("#CCCCCC");
	public int tipColor = Color.parseColor("#EE82EE");
	public Paint borderPaint;
	public Paint monthPaint;
	public Paint weekPaint;
	public Paint datePaint;
	public Paint monthChangeBtnPaint;
	public Paint cellBgPaint;
	public Paint weekBgPaint;
	public Path boxPath;
	public String[] weekText = {"周日","周一","周二","周三","周四","周五","周六"};
	public String[] monthText = {"1","2","3","4","5","6","7","8","8","10","11","12"};

    public void init(){
    	float temp = height/7f;
    	this.monthHeight = 0;
    	this.weekHeight = (float)((temp+temp*0.3f)*0.7f);
    	//this.weekHeight = temp;
    	this.cellHeight = (height-monthHeight-weekHeight)/6f; //6f;
    	this.cellWidth = width/7f;
    	
    	this.borderPaint = new Paint();
    	this.borderPaint.setColor(borderColor);
    	this.borderPaint.setStyle(Paint.Style.STROKE);
    	this.borderWidth = (float)(0.5*density);
    	 borderWidth = borderWidth < 1 ? 1 : borderWidth;  
         borderPaint.setStrokeWidth(borderWidth);  
         
         monthPaint = new Paint();  
         monthPaint.setColor(textColor);  
         monthPaint.setAntiAlias(true);  
         float textSize = cellHeight * 0.4f;    
         monthPaint.setTextSize(textSize);  
         monthPaint.setTypeface(Typeface.DEFAULT);  
         
         weekPaint = new Paint();  
         //weekPaint.setColor(textColor);  
         weekPaint.setColor(bgColor);
         weekPaint.setAntiAlias(true);  
         float weekTextSize = weekHeight * 0.6f;  
         weekPaint.setTextSize(weekTextSize);  
         weekPaint.setTypeface(Typeface.DEFAULT); 
         
         datePaint = new Paint();  
         datePaint.setColor(textColor);  
         datePaint.setAntiAlias(true);  
         float cellTextSize = cellHeight * 0.5f;  
         datePaint.setTextSize(cellTextSize);  
         datePaint.setTypeface(Typeface.DEFAULT);  
         
         boxPath = new Path();  
         boxPath.rLineTo(width, 0);  
         boxPath.moveTo(0, monthHeight + weekHeight);  
         boxPath.rLineTo(width, 0);  
         for (int i = 1; i < 6; i++) {  
             boxPath.moveTo(0, monthHeight + weekHeight + i * cellHeight);  
             boxPath.rLineTo(width, 0);  
             boxPath.moveTo(i * cellWidth, monthHeight);  
             boxPath.rLineTo(0, height - monthHeight);  
         }  
         boxPath.moveTo(6 * cellWidth, monthHeight);  
         boxPath.rLineTo(0, height - monthHeight);
         
         monthChangeBtnPaint = new Paint();  
         monthChangeBtnPaint.setAntiAlias(true);  
         monthChangeBtnPaint.setStyle(Paint.Style.FILL_AND_STROKE);  
         monthChangeBtnPaint.setColor(btnColor);  
         
         cellBgPaint = new Paint();  
         cellBgPaint.setAntiAlias(true);  
         cellBgPaint.setStyle(Paint.Style.FILL);  
         cellBgPaint.setColor(cellSelectedColor);
         
         this.weekBgPaint = new Paint();
         weekBgPaint.setAntiAlias(true);
         weekBgPaint.setStyle(Paint.Style.FILL);
         weekBgPaint.setColor(this.weekBgColor);
    }
}
