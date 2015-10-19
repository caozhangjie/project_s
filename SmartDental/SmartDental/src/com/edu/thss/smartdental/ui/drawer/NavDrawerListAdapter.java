package com.edu.thss.smartdental.ui.drawer;

import java.util.List;

import com.edu.thss.smartdental.R;



import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NavDrawerListAdapter extends BaseAdapter{

	private Context context;
	private List<NavDrawerItem> navDrawerItems;
	
	public NavDrawerListAdapter(Context context, List<NavDrawerItem> navDrawerItems){
		this.context = context;
		this.navDrawerItems = navDrawerItems;
	}
	@Override
	public int getCount() {
		return this.navDrawerItems.size();
	}

	@Override
	public Object getItem(int position) {
		return this.navDrawerItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			LayoutInflater mInflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.drawer_list_item, null);
		}
		ImageView imgIcon = (ImageView) convertView.findViewById(R.id.drawerIcon);
		TextView txtTitle = (TextView) convertView.findViewById(R.id.drawerTitle);
		TextView txtCount = (TextView) convertView.findViewById(R.id.drawerCounter);
		
		imgIcon.setImageResource(navDrawerItems.get(position).getIcon());
		txtTitle.setText(navDrawerItems.get(position).getTitle());
		if(navDrawerItems.get(position).isCounterVisible()){
			txtCount.setText(navDrawerItems.get(position).getCount());
		}else{
			txtCount.setVisibility(View.GONE);
		}
		return convertView;
	}

}
