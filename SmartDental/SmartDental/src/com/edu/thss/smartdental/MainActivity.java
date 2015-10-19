package com.edu.thss.smartdental;

import java.util.ArrayList;

import com.edu.thss.smartdental.ui.drawer.NavDrawerItem;
import com.edu.thss.smartdental.ui.drawer.NavDrawerListAdapter;


import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.TypedArray;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends FragmentActivity implements OnItemClickListener {
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private CharSequence mTitle;
	private CharSequence mDrawerTitle;
	
	private String[] mNavMenuTitles;
	private TypedArray mNavMenuIconsTypeArray;
	private ArrayList<NavDrawerItem> mNavDrawerItems;
	private NavDrawerListAdapter mAdapter;
	private ActionBarDrawerToggle mDrawerToggle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		findView();
		
		if(savedInstanceState == null){
			selectItem(0);
		}
	}
	
	@SuppressLint("NewApi")
	private void findView(){
		mTitle = mDrawerTitle = getTitle();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView)findViewById(R.id.left_drawer);
		
		mNavMenuTitles = getResources().getStringArray(R.array.nav_drawer_titles);
		mNavMenuIconsTypeArray = getResources().obtainTypedArray(R.array.nav_drawer_icons);
		mNavDrawerItems = new ArrayList<NavDrawerItem>();
		
		//------
		mNavDrawerItems.add(new NavDrawerItem(mNavMenuTitles[0],mNavMenuIconsTypeArray.getResourceId(0, -1)));
		mNavDrawerItems.add(new NavDrawerItem(mNavMenuTitles[1],mNavMenuIconsTypeArray.getResourceId(1, -1)));
		mNavDrawerItems.add(new NavDrawerItem(mNavMenuTitles[2],mNavMenuIconsTypeArray.getResourceId(2, -1)));
		mNavDrawerItems.add(new NavDrawerItem(mNavMenuTitles[3],mNavMenuIconsTypeArray.getResourceId(3, -1)));
		mNavDrawerItems.add(new NavDrawerItem(mNavMenuTitles[4],mNavMenuIconsTypeArray.getResourceId(4, -1)));
		mNavDrawerItems.add(new NavDrawerItem(mNavMenuTitles[5],mNavMenuIconsTypeArray.getResourceId(5, -1)));
		mNavDrawerItems.add(new NavDrawerItem(mNavMenuTitles[6],mNavMenuIconsTypeArray.getResourceId(6, -1)));
		mNavDrawerItems.add(new NavDrawerItem(mNavMenuTitles[7],mNavMenuIconsTypeArray.getResourceId(7, -1)));
	
		mNavMenuIconsTypeArray.recycle();
		
		mAdapter = new NavDrawerListAdapter(getApplicationContext(),mNavDrawerItems);
		mDrawerList.setAdapter(mAdapter);
		mDrawerList.setOnItemClickListener(this);
	
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.ab_bg));
		
		mDrawerToggle = new ActionBarDrawerToggle(
				this,
				mDrawerLayout,
				R.drawable.ic_drawer,
				R.string.drawer_open,
				R.string.drawer_close
				){
			public void onDrawerClosed(View view){
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu();
			}
			public void onDrawerOpened(View drawerView){
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		
		selectItem(position);
	}
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch(item.getItemId()) {
       
        default:
            return super.onOptionsItemSelected(item);
        }
    }
	private void selectItem(int position){
		Fragment fragment  = null;
		switch (position){
		case 0: 
			fragment = new InfoFragment();
			break;
		case 1: 
			fragment = new EMRFragment();
			break;
		case 2: 
			fragment = new ToothFragment();
			break;
		case 3: 
			fragment = new ImageFragment();
			break;
		case 4: 
			fragment = new AppointmentFragment();
			break;
		case 5: 
			fragment = new ReminderFragment();
			break;
		case 6: 
			fragment = new DataFragment();
			break;
		case 7: 
			fragment = new SettingFragment();
			break;
		default: break;
		}
		if(fragment != null){
			FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.content_frame,fragment).commit();
			
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(mNavMenuTitles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
		}
		else{
			Log.e("MainActivity", "Error in creating fragment");
		}
	}
    @SuppressLint("NewApi")
	@Override
    public void setTitle(CharSequence title){
    	mTitle = title;
    	getActionBar().setTitle(mTitle);
    }

}
