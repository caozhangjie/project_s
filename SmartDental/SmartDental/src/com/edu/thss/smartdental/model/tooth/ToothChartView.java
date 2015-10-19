package com.edu.thss.smartdental.model.tooth;

import java.util.ArrayList;

import com.edu.thss.smartdental.R;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

@SuppressLint("DrawAllocation")
public class ToothChartView extends View{
	ToothChartSurface surface;
	private ArrayList<ToothGraphic> listToothGraphics;

	public ToothChartView(Context context) {
		super(context);
		init();
	}

	public ToothChartView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	@Override
	protected void onDraw(Canvas canvas) {
		/*try 1: 绘制第一颗牙齿*/
		Log.i("l","hao");
		try {
			//绘制图片
			Bitmap bgImg = BitmapFactory.decodeResource(getResources(),R.drawable.tooth_bg_5); 
			
			//图片宽度
			int imgWidth = bgImg.getWidth();
			int startx = (surface.width-imgWidth)/2;
			
			canvas.drawBitmap(bgImg, startx, 0, surface.bgPaint);
			
			/*
			for(int i=0;i<this.listToothGraphics.size();i++){
			//绘制facialView
			drawFacialView(this.listToothGraphics.get(i),canvas);
			//绘制occlusalView
			drawOcculusalView(this.listToothGraphics.get(i),canvas);
			}
			*/
		   //绘制牙齿编号
			
			super.onDraw(canvas);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	



	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		if (changed) {  
            surface.init();  
        }
		super.onLayout(changed, left, top, right, bottom);
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		surface.width = getResources().getDisplayMetrics().widthPixels;
		int temp =getResources().getDisplayMetrics().heightPixels;
		Log.i("heightPixels",Integer.toString(temp));
		surface.height = surface.width*(surface.realHeight/surface.realWidth); //surface.realHeight;
		widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(surface.width,  
                View.MeasureSpec.EXACTLY);  
        heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(surface.height,  
                View.MeasureSpec.EXACTLY);  
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec); 
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	private void init() {
		surface = new ToothChartSurface();
		setBackgroundColor(surface.bgColor); //设置背景色
		initList();
		
	}

	
	private void initList() {
		listToothGraphics = new ArrayList<ToothGraphic>();
		for(int i=1;i<=32;i++){
			ToothGraphic item;
			item = new ToothGraphic(Integer.toString(i));
			item.importObjSimple();
			item.visible = true;
			item.SetDefaultColors();
			this.listToothGraphics.add(item);
		}
		
	}


	/**
	 * 绘制牙齿的镜面图像
	 * @throws Exception 
	 * */
	private void drawFacialView(ToothGraphic toothGraphic, Canvas canvas) throws Exception {
		float x,y;
		//toothGraphic.toothID = "1";
		x = getTransX(toothGraphic.toothID);
		y = getTransYfacial(toothGraphic.toothID);
		if(toothGraphic.visible||toothGraphic.IsPontic 
			||(toothGraphic.IsCrown && toothGraphic.IsImplant)){
			
		}
		float w = 0;
		if(!Tooth.IsPrimary(toothGraphic.toothID)){
			w = ToothGraphic.GetWidth(toothGraphic.toothID)*surface.width/surface.realWidth;
		}
		if(!Tooth.IsPrimary(toothGraphic.toothID)
			&&(!toothGraphic.visible||toothGraphic.IsPontic)){
			if(Tooth.IsMaxillary(toothGraphic.toothID)){
				//绘制上颚矩形
				canvas.drawRect(x-w/2,0,x+w/2,surface.height/2-20,surface.rectPaint);
			}
			else{
				//绘制下颚的矩形
				canvas.drawRect(x-w/2, surface.height/2+20,x+w/2, surface.height, surface.rectPaint);
			}
		}
		//绘制大红叉
			
		   
		
	}
	/**
	 * 得到牙齿的纵坐标
	 * */
	private float getTransYfacial(String toothID) {
		float basic = 30;
		if(Tooth.IsMaxillary(toothID)){
			return surface.height/2 - basic;
		}
		return surface.height/2 + basic;
		
	}
   /**
    * 得到牙齿的横坐标
    * */
	private float getTransX(String toothID) throws Exception {
		int tooth_int = Tooth.ToInt(toothID);
		if(tooth_int == -1){
			throw new Exception();
		}
		float xmm = ToothGraphic.GetDefaultOrthoXpos(tooth_int);
		return (xmm+surface.realWidth/2)*surface.width/surface.realWidth;
	}

	/**
	 * 绘制牙齿的咬合面图像
	 * @throws Exception 
	 * */
	private void drawOcculusalView(ToothGraphic toothGraphic, Canvas canvas) throws Exception {
		float x,y;
		x = getTransX(toothGraphic.toothID);
		y = getTransYocculusal(toothGraphic.toothID);
		if(toothGraphic.visible 
				||(toothGraphic.IsCrown&&toothGraphic.IsImplant)
				||toothGraphic.IsPontic){
			drawToothOcclusal(toothGraphic,canvas);
		}
		if(toothGraphic.visible && toothGraphic.IsSealant){
			
		}
	}

	private void drawToothOcclusal(ToothGraphic toothGraphic, Canvas canvas) throws Exception {
		ToothGroup group;
		float x,y;
		for(int i=0;i<toothGraphic.groups.size();i++){
			group = toothGraphic.groups.get(i);
			if(group.visible == false) continue;
			x = getTransX(toothGraphic.toothID);
			y = getTransYocculusal(toothGraphic.toothID);
			float sqB = 4; //4
			float cirB = 9.5f; //9.5
			float sqS = 3; //3
			float cirS = 8f; //8
			String dir;
			
			if(group.groupType == ToothGroupType.O){
				surface.occlusalPaint.setColor(group.paintColor);
				canvas.drawRect(x-sqB, y-sqB, x+sqB, y+sqB, surface.occlusalPaint);
				canvas.drawRect(x-sqB, y-sqB, x+sqB, y+sqB, surface.outlinePaint);
			}
			else if(group.groupType == ToothGroupType.I){
				surface.occlusalPaint.setColor(group.paintColor);
				canvas.drawRect(x-sqS, y-sqS, x+sqS, y+sqS, surface.occlusalPaint);
				canvas.drawRect(x-sqS, y-sqS, x+sqS, y+sqS, surface.outlinePaint);
			}
			else if(group.groupType == ToothGroupType.B){
				if(Tooth.IsMaxillary(toothGraphic.toothID)){
					surface.occlusalPath = getPath("U",x,y,sqB,cirB);
				}
				else{
					surface.occlusalPath = getPath("D",x,y,sqB,cirB);
				}
				surface.occlusalPaint.setColor(group.paintColor);
				canvas.drawPath(surface.occlusalPath, surface.occlusalPaint);
				canvas.drawPath(surface.occlusalPath, surface.outlinePaint);
			}
			else if(group.groupType == ToothGroupType.F){
				if(Tooth.IsMaxillary(toothGraphic.toothID)){
					surface.occlusalPath = getPath("U",x,y,sqS,cirS);
				}
				else{
					surface.occlusalPath = getPath("D",x,y,sqS,cirS);
				}
				surface.occlusalPaint.setColor(group.paintColor);
				canvas.drawPath(surface.occlusalPath, surface.occlusalPaint);
				canvas.drawPath(surface.occlusalPath, surface.outlinePaint);
			}
			else if(group.groupType == ToothGroupType.L){
				if(Tooth.IsMaxillary(toothGraphic.toothID)){
					dir = "D";
				}
				else{
					dir = "U";
				}
				if(Tooth.IsAnterior(toothGraphic.toothID)){
					surface.occlusalPath = getPath(dir,x,y,sqS,cirS);
				}
				else{
					surface.occlusalPath = getPath(dir,x,y,sqB,cirB);
				}
				surface.occlusalPaint.setColor(group.paintColor);
				canvas.drawPath(surface.occlusalPath, surface.occlusalPaint);
				canvas.drawPath(surface.occlusalPath, surface.outlinePaint);
			}
			else if(group.groupType == ToothGroupType.M){
				if(ToothGraphic.IsRight(toothGraphic.toothID)){
					dir = "R";
				}
				else{
					dir = "L";
				}
				if(Tooth.IsAnterior(toothGraphic.toothID)){
					surface.occlusalPath = getPath(dir,x,y,sqS,cirS);
				}
				else{
					surface.occlusalPath = getPath(dir,x,y,sqB,cirB);
				}
				surface.occlusalPaint.setColor(group.paintColor);
				canvas.drawPath(surface.occlusalPath, surface.occlusalPaint);
				canvas.drawPath(surface.occlusalPath, surface.outlinePaint);
			}
			else if(group.groupType == ToothGroupType.D){
				if(ToothGraphic.IsRight(toothGraphic.toothID)){
					dir = "L";
				}
				else{
					dir = "R";
				}
				if(Tooth.IsAnterior(toothGraphic.toothID)){
					surface.occlusalPath = getPath(dir,x,y,sqS,cirS);
				}
				else{
					surface.occlusalPath = getPath(dir,x,y,sqB,cirB);
				}
				surface.occlusalPaint.setColor(group.paintColor);
				canvas.drawPath(surface.occlusalPath, surface.occlusalPaint);
				canvas.drawPath(surface.occlusalPath, surface.outlinePaint);
			}
		}
		
	}
    private Path getPath(String UDLR, float x, float y,float sq,float cir){
    	Path path = new Path();
    	float pt = cir *0.7071f; //sin45。
    	RectF oval = new RectF();
    	oval.set(x-cir,y-cir, x+cir, y+cir);
    	if(UDLR.equals("U")){
    		path.moveTo(x-sq, y-sq);
    		path.lineTo(x+sq, y-sq);
    		path.lineTo(x+pt, y-pt);
    		
    		path.moveTo(x-cir, y-cir);
    		oval.set(x-cir,y-cir, x+cir, y+cir);
    		path.addArc(oval, 360-45, -90);
    		
    		path.moveTo(x-pt, y-pt);
    		path.lineTo(x-sq, y-sq);
    		
    		path.close();
    	}
    	else if(UDLR.equals("D")){
    		path.moveTo(x+sq, y+sq);
    		path.lineTo(x-sq, y+sq);
    		path.lineTo(x-pt, y+pt);
    		
    		path.moveTo(x-cir, y-cir);
    		path.addArc(oval, 90+45, -90);
    		
    		path.moveTo(x+pt, y+pt);
    		path.lineTo(x+sq, y+sq);
    		
    		path.close();
    		
    	}
    	else if(UDLR.equals("L")){
    		path.moveTo(x-sq, y+sq);
    		path.lineTo(x-sq, y-sq);
    		path.lineTo(x-pt, y-pt);
    		
    		path.moveTo(x-cir, y-cir);
    		path.addArc(oval, 180+45, -90);
    		
    		path.moveTo(x-pt, y+pt);
    		path.lineTo(x-sq, y+sq);
    		
    		path.close();
    		
    	}
        else if(UDLR.equals("R")){
        	path.moveTo(x+sq, y-sq);
        	path.lineTo(x+sq, y+sq);
        	path.lineTo(x+pt, y+pt);
        	
        	path.moveTo(x-cir, y-cir);
    		path.addArc(oval, 45, -90);
    		
    		path.moveTo(x+pt, y-pt);
    		path.lineTo(x+sq, y-sq);
    		
    		path.close();
        }
    	return path;
    }
	private float getTransYocculusal(String toothID) {
		if(Tooth.IsMaxillary(toothID)){
			return (float)surface.height/2 - 48;
		}
		return (float)surface.height/2+48;
	}
}
