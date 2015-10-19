package com.edu.thss.smartdental;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
/**
 * 乳牙查看列表
 * */
public class ToothPermanentFragment extends Fragment{
	ExpandableListView list;
    public String[] groupTypes = {"左上部位","右上部位","左下部位","右下部位"};
    public String[][] child ={{"1",  "2",  "3",  "4",  "5",  "6", "7","8"},
    						 {"9", "10", "11", "12", "13", "14", "15", "16"},
    						 {"32", "31", "30", "29", "28", "27", "26","25"},
    						 {"24", "23", "22", "21", "20", "19", "18", "17"}
    		                 };
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.tooth_permanent, container,false);
		list = (ExpandableListView)rootView.findViewById(R.id.tooth_permanentview);
		list.setAdapter(new TreeViewAdapter(this.getActivity()));
		
		list.setOnChildClickListener(new OnChildClickListener(){

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				
				Log.i("childclick", "点击了子节点");
				AlertDialog dialog_try1= new AlertDialog.Builder(getActivity()).create();
				dialog_try1.show();
				dialog_try1.getWindow().setContentView(R.layout.dialog_tooth);
				return false;
			}});
		return rootView;
	}
   public class TreeViewAdapter extends BaseExpandableListAdapter{
	   private LayoutInflater inflater;
	   private LayoutInflater inflater1;
	public TreeViewAdapter(Context c){
		this.inflater = LayoutInflater.from(c.getApplicationContext());
		this.inflater1 = LayoutInflater.from(c.getApplicationContext());
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return child[groupPosition][childPosition];
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
			ViewGroup parent) {
		View childview = inflater1.inflate(R.layout.tooth_list_child_item, null);
		TextView textview = (TextView)childview.findViewById(R.id.tooth_child_title);
		textview.setText(child[groupPosition][childPosition]);
		return childview;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return child[groupPosition].length;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return groupTypes[groupPosition];
	}

	@Override
	public int getGroupCount() {
		return groupTypes.length;
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		View groupView = inflater.inflate(R.layout.tooth_list_father_item, null);
		
		ImageView iv = (ImageView)groupView.findViewById(R.id.tooth_father_arrow);
		if(!isExpanded){
			iv.setBackgroundResource(R.drawable.ic_arrow_right);
		}else{
			iv.setBackgroundResource(R.drawable.ic_arrow_left);
		}
		TextView textview = (TextView)groupView.findViewById(R.id.tooth_father_title);
		textview.setText(groupTypes[groupPosition]);
		
		return groupView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		return true;
	}
	   
   }
}
