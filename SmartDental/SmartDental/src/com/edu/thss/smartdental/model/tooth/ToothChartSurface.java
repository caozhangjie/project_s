package com.edu.thss.smartdental.model.tooth;


import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;

public class ToothChartSurface {
	public int width;
	public int height;
	public int realWidth = 410; 
	public int realHeight = 410;
	//颜色
	public int bgColor = Color.WHITE;
	public int colorText = Color.BLACK;  //text的颜色
	public int colorTextHighlight;
	public int colorBackHighlight;
	public int colorBackSimple=Color.rgb(150,145,153);//opendal用的背景色
	
	public Paint bgPaint;
	public Paint rectPaint;
	public Paint occlusalPaint;
	public Path occlusalPath;
	public Paint outlinePaint;


	//暂时先不考虑映射问题
//	private float WidthProjection; //投影在屏幕上的宽度
//	private int preferredPixelFormatNum;  


	public void init(){
		rectPaint = new Paint();
		rectPaint.setColor(colorBackSimple);
		rectPaint.setStyle(Style.FILL);
		
		this.bgPaint = new Paint();
		
		this.occlusalPaint = new Paint();
		
		this.outlinePaint = new Paint();
		this.outlinePaint.setColor(Color.GRAY);
		this.outlinePaint.setStyle(Style.STROKE);
		
	}
}
