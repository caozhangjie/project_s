package com.edu.thss.smartdental;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;

/**
 * 一张图片浏览页面，实现缩放、拖动、自动居中
 * */
public class OneImageActivity extends Activity implements OnTouchListener,OnClickListener{

	private Matrix matrix = new Matrix();
	private Matrix savedMatrix = new Matrix();
	DisplayMetrics dm;
	ImageView imgView;
	Bitmap bitmap;
	Button leftRotate;
	Button rightRotate;
	
	float minScaleR; //最小缩放比例
	static final float MAX_SCALE = 4f; //最大缩放比例
	
	static final int  NONE = 0;  //初始状态
	static final int DRAG = 1; //拖动
	static final int ZOOM = 2; // 缩放
	int mode = NONE;
	
	PointF prev = new PointF();
	PointF mid = new PointF();
	float dist = 1f;
	
	int rotate; //旋转角度
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_one_image);
		
		getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.ab_bg));
		imgView = (ImageView)findViewById(R.id.one_image_view);
		//bitmap = BitmapFactory.decodeResource(getResources(), this.getIntent().getExtras().getInt("IMG")); //获取图片资源
		int tempclass = getIntent().getExtras().getInt("imageclass");
		if(tempclass == 0){
			bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.img_1_1 ); //获取图片资源
		}
		else if(tempclass == 1){
			bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.img_1_2 ); //获取图片资源
		}
		else if(tempclass == 2){
			bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.img_1_3 ); //获取图片资源
		}
		else if(tempclass == 3){
			bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.img_1_4 ); //获取图片资源
		}
		//bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.pic_1 ); //获取图片资源
		imgView.setImageBitmap(bitmap); //填充控件
		imgView.setOnTouchListener(this); //触屏监听
		dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm); //获取分辨率
		minZoom();
		center();
		imgView.setImageMatrix(matrix);
		leftRotate = (Button)findViewById(R.id.image_leftRotate);
		rightRotate = (Button)findViewById(R.id.image_rightRotate);
		leftRotate.setOnClickListener(this);
		this.rightRotate.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == leftRotate.getId()){
			//matrix.postRotate(-90,imgView.getWidth()/2,imgView.getHeight()/2);
			matrix.postRotate(-90,dm.widthPixels/2,dm.heightPixels/2);
			imgView.setImageMatrix(matrix);
			savedMatrix.set(matrix);
			mode = NONE;
			CheckView();
		}
		else if(v.getId() == rightRotate.getId()){
			matrix.postRotate(90,dm.widthPixels/2,dm.heightPixels/2);
			imgView.setImageMatrix(matrix);
			savedMatrix.set(matrix);
			mode = NONE;
			CheckView();
		}
		
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch(event.getAction()&MotionEvent.ACTION_MASK){
		case MotionEvent.ACTION_DOWN: //主点按下
			savedMatrix.set(matrix);
			prev.set(event.getX(),event.getY());
			mode = DRAG;
			break;
		case MotionEvent.ACTION_POINTER_DOWN: //副点按下
			dist = spacing(event);
			if(dist > 10f){
				//连续两点的距离大于10，多点模式
				savedMatrix.set(matrix);
				midPoint(mid,event);
				mode = ZOOM;
			}
			break;
		
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP:
			mode = NONE;
			break;
		case MotionEvent.ACTION_MOVE:
			if(mode == DRAG){
				matrix.set(savedMatrix);
				matrix.postTranslate(event.getX()-prev.x, event.getY()-prev.y);
			}
			else if(mode == ZOOM){
				float newDist = spacing(event);
				if(newDist >10f){
					matrix.set(savedMatrix);
					float tScale = newDist/dist;
					matrix.postScale(tScale, tScale,mid.x,mid.y);
				}
			}
			break;
		}
		imgView.setImageMatrix(matrix);
		CheckView();
		return true;
		
	}
	/**
	 * 限制最大最小缩放比例，自动居中
	 * */
	private void CheckView(){
		float p[] = new float[9];
		matrix.getValues(p);
		/*if(mode == ZOOM){
			if(p[0]<minScaleR){
				matrix.setScale(minScaleR, minScaleR);
			}
			if(p[0]>MAX_SCALE){
				matrix.set(savedMatrix);
			}
		}*/
		//else matrix.set(savedMatrix);
		/*if(p[0]<minScaleR){
			matrix.setScale(minScaleR, minScaleR);
		}*/
		if(p[0]>MAX_SCALE){
			matrix.set(savedMatrix);
			//matrix.setScale(MAX_SCALE, MAX_SCALE);
		}
		center();
	}
	/**
	 * 最小缩放比例
	 * */
	private void minZoom(){
		minScaleR = Math.min(
				(float)dm.widthPixels/(float)bitmap.getWidth(), 
				(float)dm.heightPixels/(float)bitmap.getHeight());
		if(minScaleR < 1.0){
			matrix.postScale(minScaleR, minScaleR);
		}
	}
	private void center(){
		center(true,true);
	}
	
	/**
	 * 横向、纵向居中
	 * */
	protected void center(boolean horizontal, boolean vertical){
		
		Matrix m = new Matrix();
		m.set(matrix);
		RectF rect = new RectF(0,0,bitmap.getWidth(),bitmap.getHeight());
		m.mapRect(rect);
		
		float height = rect.height();
		float width = rect.width();
		
		float deltaX = 0;
		float deltaY = 0;
		
		// 图片小于屏幕大小，则居中显示。大于屏幕，上方留空则往上移，下方留空则往下移
		if(vertical){
			int screenHeight = dm.heightPixels;
			if(height<screenHeight){
				deltaY = (screenHeight - height)/2 - rect.top;
			}
			else if(rect.top>0){
				deltaY = -rect.top;
			}
			else if(rect.bottom<screenHeight){
				deltaY = imgView.getHeight() - rect.bottom;
			}
		}
		if(horizontal){
			int screenWidth = dm.widthPixels;
			if(width<screenWidth){
				deltaX = (screenWidth-width)/2 - rect.left;
			}
			else if(rect.left>0){
				deltaX = - rect.left;
			}
			else if(rect.right < screenWidth){
				deltaX = screenWidth - rect.right;
			}
		}
		matrix.postTranslate(deltaX, deltaY);
	}
	
	/**
	 * 两点的距离
	 * */
	private float spacing(MotionEvent event){
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x*x+y*y);
	}
	/**
	 * 两点的中点
	 * */
	private void midPoint(PointF point, MotionEvent event){
		float x = event.getX(0)+event.getX(1);
		float y = event.getY(0)+event.getY(1);
		point.set(x/2,y/2);
	}


}
