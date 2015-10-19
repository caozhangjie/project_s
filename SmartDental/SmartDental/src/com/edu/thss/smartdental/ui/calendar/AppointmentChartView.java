package com.edu.thss.smartdental.ui.calendar;

import java.util.ArrayList;

import com.edu.thss.smartdental.OnedayAppointListFragment.AppointmentItem;
import com.edu.thss.smartdental.model.AppointmentElement;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

@SuppressLint("DrawAllocation")
public class AppointmentChartView extends View{
	private AppointmentChartSurface surface;
	private ArrayList<AppointmentElement> data;

	public AppointmentChartView(Context context) {
		super(context);
		setWillNotDraw(false);
		init();
	}
	public AppointmentChartView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setWillNotDraw(false);
		init();
	}
	private void init() {
		surface = new AppointmentChartSurface();
		setBackgroundColor(surface.bgColor); //设置背景色
		initApp();
	}
	@Override
	protected void onDraw(Canvas canvas) {
		Log.i("ondraw","调用了");
		float itemHeight = surface.height/data.size(); //一个事件的高度
		float radius = (itemHeight/8)<(surface.width/8)?itemHeight/8:surface.width/8; //圆的半径
		
		float above = itemHeight/4;
		for(int i=0;i<data.size();i++){
			/*绘制事件*/
			//绘制线
			surface.linePaint.setStrokeWidth(surface.collineWidth);
			canvas.drawLine(surface.width/2, itemHeight*i, 
					        surface.width/2, itemHeight*(i+1), surface.linePaint);
			//绘制圆
			canvas.drawCircle(surface.width/2,
					          itemHeight*i+above+radius, 
					          radius, surface.circlePaint);
		   //绘制左边的指示横线
			surface.linePaint.setStrokeWidth(surface.rowlineWidth);
			canvas.drawLine(surface.width/2-radius-50, 
					        itemHeight*i+above+radius, 
					        surface.width/2-radius, 
					        itemHeight*i+above+radius, 
					        surface.linePaint);
			//绘制左边的时间1
			//surface.timePaint.setColor(Color.RED);
			surface.timePaint.setTextSize(60);
			canvas.drawText(getHour(data.get(i).startTime),
					        surface.width/2 - radius-55-surface.hourWidth-surface.minuteWidth, 
					        itemHeight*i+above+radius, 
					        surface.timePaint);
			//绘制左边的时间2
			surface.timePaint.setTextSize(40);
			canvas.drawText(getMinute(data.get(i).startTime),
					        surface.width/2-radius-55-surface.minuteWidth, 
					        itemHeight*i+above+radius-surface.hourHeight/2,
					        surface.timePaint);
			
			//绘制右边的事件
			surface.textLayout = new StaticLayout(data.get(i).name,
					             surface.textPaint,
					             (int)(surface.width-surface.width/2 -radius-20),Alignment.ALIGN_NORMAL,
					             1.0f,0.0f,true);
			canvas.translate(surface.width/2+radius+20,itemHeight*i+above+radius);
			surface.textLayout.draw(canvas);
			canvas.translate(-(surface.width/2+radius+20), -(itemHeight*i+above+radius));
		}
		
		super.onDraw(canvas);
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
        surface.height = (int) (getResources().getDisplayMetrics().heightPixels - 45); 
        widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(surface.width,  
                View.MeasureSpec.EXACTLY);  
        heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(surface.height,  
                View.MeasureSpec.EXACTLY);  
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);  
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	private String getHour(String str){
		String[] strs = str.split(":");
		return strs[0];
	}
	private String getMinute(String str){
		String[] strs = str.split(":");
		return strs[1];
	}

	/**
	 * 测试数据
	 * */
	private void initApp(){
		data = new ArrayList<AppointmentElement>();
		
		AppointmentElement appoint = new AppointmentElement("去正畸科进行第二次会诊","2014-5-15","09:00","10:00");
		data.add(appoint);
		
		appoint = new AppointmentElement("拔牙","2014-5-15","14:00","15:30");
		
		data.add(appoint);
	}
}
