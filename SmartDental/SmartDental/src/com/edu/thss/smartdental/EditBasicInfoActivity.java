package com.edu.thss.smartdental;

import java.util.Calendar;
import java.util.Date;



import android.os.Bundle;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class EditBasicInfoActivity extends Activity {

	
	TextView tname;
	
	RadioGroup radiogroup ;
	RadioButton radiomale;
	RadioButton radiofemale;
	RadioButton radiounknown;
	CharSequence gender;
	
	TextView tbirth;
	int year;
	int month;
	int day;
	CharSequence date;
	
	EditText etid;
	CharSequence id;
	EditText etmedicare;
	CharSequence medicare;
	Button save;
	
	
	private static final int DATE_DIALOG_ID = 0;
	public static final int RESULT_CODE = 10;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_basic_info);
		getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.ab_bg));
		initialViews();
	}
	
	/**
	 * 初始化控件和UI视图
	 * */
	private void initialViews(){
		setDate();
		//------- name------------
		tname = (TextView)findViewById(R.id.editname_c);
		tname.setText(getIntent().getExtras().getCharSequence("name"));
		//-------gender-----------
		CharSequence tempgender = getIntent().getExtras().getCharSequence("gender");
		radiogroup = (RadioGroup)findViewById(R.id.editgender_c);
		radiofemale = (RadioButton)findViewById(R.id.editgender_f);
		radiomale = (RadioButton)findViewById(R.id.editgender_m);
		radiounknown = (RadioButton)findViewById(R.id.editgender_unknown);
		gender = tempgender;
		if(tempgender.equals("女"))
		radiogroup.check(R.id.editgender_f);
		else if(tempgender.equals("男"))
			radiogroup.check(R.id.editgender_m);
		else 
			radiogroup.check(R.id.editgender_unknown);
		radiogroup.setOnCheckedChangeListener(radiolistener);
		//---------------birth----------------------------
		CharSequence tempbirth = getIntent().getExtras().getCharSequence("birth");
		tbirth = (TextView)findViewById(R.id.editbirth_c);
		tbirth.setText(tempbirth);
		tbirth.setOnClickListener(birthlistener);
		//-------------id------------
		CharSequence tempid = getIntent().getExtras().getCharSequence("id");
		etid = (EditText)findViewById(R.id.editid_c);
		etid.setText(tempid);
		etid.setOnEditorActionListener(editListener);
		//-----------medicare-------------
		CharSequence tempmedicare = getIntent().getExtras().getCharSequence("medicare");
		etmedicare = (EditText)findViewById(R.id.editmedicare_c);
		etmedicare.setText(tempmedicare);
		etmedicare.setOnEditorActionListener(editListener);
		//----------button------------
		save = (Button)findViewById(R.id.editsave);
		save.setOnClickListener(saveListener);
	}
	private void setDate(){
		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
		
		//updateDateDisplay();
	}
	/**
	 * 更新日期显示
	 * */
	private void updateDateDisplay(){
		String tempdate = new String();
		tempdate += year;
		tempdate += "-";
		tempdate += (month+1) <10?"0":"";
		tempdate += (month+1);
		tempdate += "-";
		tempdate += day < 10?"0":"";
		tempdate += day;
		
		date = tempdate;
		tbirth.setText(tempdate);		
	}
	
 
	private RadioGroup.OnCheckedChangeListener radiolistener = new RadioGroup.OnCheckedChangeListener(){

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			if(checkedId == radiofemale.getId()){
				gender = "女";
			}
			else if(checkedId == radiomale.getId()){
				gender = "男";
			}
			else if(checkedId == radiounknown.getId()){
				gender = "未知";
			}
			
		}};
    private View.OnClickListener birthlistener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			DatePickerDialog dpd = new DatePickerDialog(EditBasicInfoActivity.this,datesetListener,year,month,day);
			dpd.show();
		}
	};
	
	/**
	 * 日期控件监听
	 * */
	private OnDateSetListener datesetListener = new OnDateSetListener(){

		@Override
		public void onDateSet(DatePicker view, int y, int m, int d) {
			year = y;
			month = m;
			day = d;
			
			updateDateDisplay();
		}
		
	};
	/**
	 * edit监听
	 * */
	private OnEditorActionListener editListener = new OnEditorActionListener(){

		@Override
		public boolean onEditorAction(TextView v, int actionid, KeyEvent event) {
			if(v.getId() == etid.getId()){
				id = etid.getText().toString();
			}
			else if(v.getId() == etmedicare.getId()){
				medicare = etmedicare.getText();
			}
			return false;
		}};
		
	private OnClickListener saveListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.putExtra("gender",gender);
			intent.putExtra("birth", tbirth.getText());
			intent.putExtra("id", etid.getText().toString());
			intent.putExtra("medicare", etmedicare.getText().toString());
			EditBasicInfoActivity.this.setResult(RESULT_CODE,intent);
			finish();
		}
		
	};
}
