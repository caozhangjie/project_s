package com.edu.thss.smartdental;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.view.Menu;
import android.widget.ArrayAdapter;

@SuppressLint("NewApi")
public class OnedayAppointActivity extends Activity implements ActionBar.OnNavigationListener {

	ActionBar actionBar = null;
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_oneday_appoint);
		
		actionBar = this.getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.ab_bg));

		ArrayAdapter arrayAdapter = new ArrayAdapter(OnedayAppointActivity.this,R.layout.appoint_actionbar_text,
				                    R.id.calendar_actionbar_text1,new String[]{"图表显示","列表显示","备忘"});
		actionBar.setListNavigationCallbacks(arrayAdapter,this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.oneday_appoint, menu);
		return true;
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		Fragment fragment  = null;
		switch(itemPosition){
		case 0: fragment =  new OnedayAppointChartFragment();break;
		case 1: fragment = new OnedayAppointListFragment();break;
		default: break;
		}
		/*Bundle bundle = new Bundle();
		bundle.putInt("key", itemPosition+1);*/
		if(fragment != null){
	    FragmentTransaction action = this.getFragmentManager().beginTransaction();
	    action.replace(R.id.app_content, fragment);
	    action.commit();
		}
		return true;
	}

}
