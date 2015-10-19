package com.edu.thss.smartdental;

import com.edu.thss.smartdental.ToothPermanentFragment.TreeViewAdapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.ExpandableListView.OnChildClickListener;

public class ToothPrimaryFragment extends Fragment{
	ExpandableListView list;
	public String[] groupTypes = {"左上","右上","左下","右下"};
    public String[][] child ={{"A",  "B",  "C",  "D",  "E"},
    						  {"F",  "G",  "H",  "I",  "J"},
    						  {"T",  "S",  "R",  "Q",  "P"},
    						  {"O",  "N",  "M",  "L",  "K"}
    		                  };
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.tooth_primary, container,false);
		list = (ExpandableListView)rootView.findViewById(R.id.tooth_primaryview);
		list.setAdapter(new TreeViewAdapter(this.getActivity()));
		
		list.setOnChildClickListener(new OnChildClickListener(){

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// TODO Auto-generated method stub
				Log.i("childclick", "点击了子节点");
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
