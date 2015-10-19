package com.edu.thss.smartdental.ui.calendar;

import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;

public class CalendarView extends View implements View.OnTouchListener{
	private final static String TAG = "anCalendar";
	private Date selectedStartDate;  
    private Date selectedEndDate;  
	private Date curDate;//当前显示的月
	private Date today;//当日日期
	private Date downDate; // 手指按下状态时临时日期 
	private int downIndex; // 按下的格子索引
	private Date showFirstDate,showLastDate;
	private Calendar calendar;
	private CalendarSurface surface;
	private int[] date = new int[42]; //日历上显示的数字
	private int curStartIndex,curEndIndex; //当前显示的日历起始的索引
	private OnItemClickListener dayClickListener ;
 
		//日期选择监听事件


	public CalendarView(Context context) {
		super(context);
		init();
	}
	public CalendarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	private void init(){
		this.curDate = selectedStartDate = selectedEndDate = today = new Date();
		calendar = Calendar.getInstance();
		calendar.setTime(curDate);
		surface = new CalendarSurface();
		surface.density = getResources().getDisplayMetrics().density;
		setBackgroundColor(surface.bgColor);
		setOnTouchListener(this);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		 // 画框  
        canvas.drawPath(surface.boxPath, surface.borderPaint);  
        float weekTextY = surface.monthHeight + surface.weekHeight * 3 / 4f;  
        // 星期背景  
        for (int i = 0; i < surface.weekText.length; i++) {  
            float weekTextX = i  
                    * surface.cellWidth  
                    + (surface.cellWidth - surface.weekPaint  
                            .measureText(surface.weekText[i])) / 2f;  
            drawWeekBg(canvas,i);
            canvas.drawText(surface.weekText[i], weekTextX, weekTextY,  
                    surface.weekPaint);  
            
        }  
          
        // 计算日期  
        calculateDate();  
        // 按下状态，选择状态背景色  
        drawDownOrSelectedBg(canvas);   
        int todayIndex = -1;  
        calendar.setTime(curDate);  
        String curYearAndMonth = calendar.get(Calendar.YEAR) + ""  
                + calendar.get(Calendar.MONTH);  
        calendar.setTime(today);  
        String todayYearAndMonth = calendar.get(Calendar.YEAR) + ""  
                + calendar.get(Calendar.MONTH);  
        if (curYearAndMonth.equals(todayYearAndMonth)) {  
            int todayNumber = calendar.get(Calendar.DAY_OF_MONTH);  
            todayIndex = curStartIndex + todayNumber - 1;  
        }  
        for (int i = 0; i < 42; i++) {  
            int color = surface.textColor;  
            if (isLastMonth(i)) {  
                color = surface.borderColor;  
            } else if (isNextMonth(i)) {  
                color = surface.borderColor;  
            }  
            if (todayIndex != -1 && i == todayIndex) {  
                color = surface.todayNumberColor;  
            }  
            if(i == 26 || i== 13 || i==17||i==20||i==21) {
            	drawCellTip(canvas,i,surface.tipColor);
            }
            drawCellText(canvas, i, date[i] + "", color);  
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
        surface.height = (int) (getResources().getDisplayMetrics().heightPixels*2/5); 
       // surface.height = (int)getHeight();
        widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(surface.width,  
                View.MeasureSpec.EXACTLY);  
        heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(surface.height,  
                View.MeasureSpec.EXACTLY);  
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);  
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	@Override
	
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {  
        case MotionEvent.ACTION_DOWN:  
            setSelectedDateByCoor(event.getX(), event.getY());  
            break;  
        case MotionEvent.ACTION_UP:  
            if (downDate != null) {  
//              if (!completed) {  
//                  if (downDate.before(selectedStartDate)) {  
//                      selectedEndDate = selectedStartDate;  
//                      selectedStartDate = downDate;  
//                  } else {  
//                      selectedEndDate = downDate;  
//                  }  
//                  completed = true;  
//              } else {  
//                  selectedStartDate = selectedEndDate = downDate;  
//                  completed = false;  
//              }  
                selectedStartDate = selectedEndDate = downDate;  
                //响应监听事件  
                //setOnItemClickListener(new OnItemClickListener());
                this.dayClickListener.OnItemClick(selectedStartDate);    
                downDate = null;  
                invalidate();  
            }  
            break;  
        }  
        return true;  
	}
	
	private void calculateDate() {  
        calendar.setTime(curDate);  
        calendar.set(Calendar.DAY_OF_MONTH, 1);  
        int dayInWeek = calendar.get(Calendar.DAY_OF_WEEK);    
        int monthStart = dayInWeek;  
        if (monthStart == 1) {  
            monthStart = 8;  
        }  
        monthStart -= 1;  //以日为开头-1，以星期一为开头-2  
        curStartIndex = monthStart;  
        date[monthStart] = 1;  
        // last month  
        if (monthStart > 0) {  
            calendar.set(Calendar.DAY_OF_MONTH, 0);  
            int dayInmonth = calendar.get(Calendar.DAY_OF_MONTH);  
            for (int i = monthStart - 1; i >= 0; i--) {  
                date[i] = dayInmonth;  
                dayInmonth--;  
            }  
            calendar.set(Calendar.DAY_OF_MONTH, date[0]);  
        }  
        showFirstDate = calendar.getTime();  
        // this month  
        calendar.setTime(curDate);  
        calendar.add(Calendar.MONTH, 1);  
        calendar.set(Calendar.DAY_OF_MONTH, 0);   
        int monthDay = calendar.get(Calendar.DAY_OF_MONTH);  
        for (int i = 1; i < monthDay; i++) {  
            date[monthStart + i] = i + 1;  
        }  
        curEndIndex = monthStart + monthDay;  
        // next month  
        for (int i = monthStart + monthDay; i < 42; i++) {  
            date[i] = i - (monthStart + monthDay) + 1;  
        }  
        if (curEndIndex < 42) {  
            // 显示了下一月的  
            calendar.add(Calendar.DAY_OF_MONTH, 1);  
        }  
        calendar.set(Calendar.DAY_OF_MONTH, date[41]);  
        showLastDate = calendar.getTime();  
    } 
	private void drawCellText(Canvas canvas, int index, String text, int color) {  
        int x = getXByIndex(index);  
        int y = getYByIndex(index);  
        surface.datePaint.setColor(color);  
        float cellY = surface.monthHeight + surface.weekHeight + (y - 1)  
                * surface.cellHeight + surface.cellHeight * 3 / 4f;  
        float cellX = (surface.cellWidth * (x - 1))  
                + (surface.cellWidth - surface.datePaint.measureText(text))  
                / 2f;  
        canvas.drawText(text, cellX, cellY, surface.datePaint);  
    } 
	private void drawCellBg(Canvas canvas, int index, int color) {  
        int x = getXByIndex(index);  
        int y = getYByIndex(index);  
        surface.cellBgPaint.setColor(color);  
        float left = surface.cellWidth * (x - 1) + surface.borderWidth;  
        float top = surface.monthHeight + surface.weekHeight + (y - 1)  
                * surface.cellHeight + surface.borderWidth;  
        canvas.drawRect(left, top, left + surface.cellWidth  
                - surface.borderWidth, top + surface.cellHeight  
                - surface.borderWidth, surface.cellBgPaint);  
    }
	private void drawCellTip(Canvas canvas, int index,int color){
		Path path = new Path();
		int x = getXByIndex(index);  
        int y = getYByIndex(index);  
		float left = surface.cellWidth*(x-1)+surface.borderWidth;
		float top = surface.monthHeight + surface.weekHeight + (y - 1)  
                * surface.cellHeight + surface.borderWidth; 
		path.moveTo(left, top);
		path.lineTo(left, top+surface.weekHeight/4);
		path.lineTo(left+surface.cellWidth/4, top);
		path.close();
		surface.weekBgPaint.setColor(color);
		canvas.drawPath(path, surface.weekBgPaint);
		 //绘制三角形
	}
	private void drawWeekBg(Canvas canvas,int index){
		int x = getXByIndex(index);
		//int y = getYByWeekIndex(index);
		surface.weekBgPaint.setColor(Color.parseColor("#00bfff"));
		float left = surface.cellWidth*(x-1)+surface.borderWidth;
		float top = surface.monthHeight + surface.borderWidth;
		canvas.drawRect(left, top,left+surface.cellWidth-surface.borderWidth,
				top+surface.weekHeight-surface.borderWidth, surface.weekBgPaint);
		
		/*Path path = new Path();
		path.moveTo(left, top);
		path.lineTo(left, top+surface.weekHeight/4);
		path.lineTo(left+surface.cellWidth/4, top);
		path.close();
		canvas.drawPath(path, surface.weekBgPaint);
		*/ //绘制三角形
	}
	
	private void drawDownOrSelectedBg(Canvas canvas) {  
        // down and not up  
        if (downDate != null) {  
            drawCellBg(canvas, downIndex, surface.cellDownColor);  
        }  
        // selected bg color  
        if (!selectedEndDate.before(showFirstDate)  
                && !selectedStartDate.after(showLastDate)) {  
            int[] section = new int[] { -1, -1 };  
            calendar.setTime(curDate);  
            calendar.add(Calendar.MONTH, -1);  
            findSelectedIndex(0, curStartIndex, calendar, section);  
            if (section[1] == -1) {  
                calendar.setTime(curDate);  
                findSelectedIndex(curStartIndex, curEndIndex, calendar, section);  
            }  
            if (section[1] == -1) {  
                calendar.setTime(curDate);  
                calendar.add(Calendar.MONTH, 1);  
                findSelectedIndex(curEndIndex, 42, calendar, section);  
            }  
            if (section[0] == -1) {  
                section[0] = 0;  
            }  
            if (section[1] == -1) {  
                section[1] = 41;  
            }  
            for (int i = section[0]; i <= section[1]; i++) {  
                drawCellBg(canvas, i, surface.cellSelectedColor);  
            }  
        }  
    }  
	
	  private void findSelectedIndex(int startIndex, int endIndex,  
	            Calendar calendar, int[] section) {  
	        for (int i = startIndex; i < endIndex; i++) {  
	            calendar.set(Calendar.DAY_OF_MONTH, date[i]);  
	            Date temp = calendar.getTime();  
	            // Log.d(TAG, "temp:" + temp.toLocaleString());  
	            if (temp.compareTo(selectedStartDate) == 0) {  
	                section[0] = i;  
	            }  
	            if (temp.compareTo(selectedEndDate) == 0) {  
	                section[1] = i;  
	                return;  
	            }  
	        }  
	    }  
	   
	  public Date getSelectedStartDate() {
			return selectedStartDate;
		}

		public Date getSelectedEndDate() {
			return selectedEndDate;
		}

	    private boolean isLastMonth(int i) {  
	        if (i < curStartIndex) {  
	            return true;  
	        }  
	        return false;  
	    }  
	  
	    private boolean isNextMonth(int i) {  
	        if (i >= curEndIndex) {  
	            return true;  
	        }  
	        return false;  
	    }  
	  
	    private int getXByIndex(int i) {  
	        return i % 7 + 1; // 1 2 3 4 5 6 7  
	    }  
	  
	    private int getYByIndex(int i) {  
	        return i / 7 + 1; // 1 2 3 4 5 6  
	    }  
	  
	    // 获得当前应该显示的年月  
	    public String getYearAndmonth() {  
	        calendar.setTime(curDate);  
	        int year = calendar.get(Calendar.YEAR);  
	        int month = calendar.get(Calendar.MONTH);  
	        return year+"年" + surface.monthText[month]+"月";  
	    }  
	      
	    //上一月  
	    public String clickLeftMonth(){  
	        calendar.setTime(curDate);  
	        calendar.add(Calendar.MONTH, -1);  
	        curDate = calendar.getTime();  
	        invalidate();  
	        return getYearAndmonth();  
	    }  
	    //下一月  
	    public String clickRightMonth(){  
	        calendar.setTime(curDate);  
	        calendar.add(Calendar.MONTH, 1);  
	        curDate = calendar.getTime();  
	        invalidate();  
	        return getYearAndmonth();  
	    }  
	  
	    private void setSelectedDateByCoor(float x, float y) {  
	        // cell click down  
	        if (y > surface.monthHeight + surface.weekHeight) {  
	            int m = (int) (Math.floor(x / surface.cellWidth) + 1);  
	            int n = (int) (Math  
	                    .floor((y - (surface.monthHeight + surface.weekHeight))  
	                            / Float.valueOf(surface.cellHeight)) + 1);  
	            downIndex = (n - 1) * 7 + m - 1;  
	        
	            calendar.setTime(curDate);  
	            if (isLastMonth(downIndex)) {  
	                calendar.add(Calendar.MONTH, -1);  
	            } else if (isNextMonth(downIndex)) {  
	                calendar.add(Calendar.MONTH, 1);  
	            }  
	            calendar.set(Calendar.DAY_OF_MONTH, date[downIndex]);  
	            downDate = calendar.getTime();  
	        }  
	        invalidate();  
	    }  
	    //给控件设置监听事件  
	    public void setOnItemClickListener(OnItemClickListener onItemClickListener){  
	        this.dayClickListener =  onItemClickListener;  
	    }  
	    //监听接口  
	    public interface OnItemClickListener {  
	        void OnItemClick(Date date);  
	    }  
	

}
