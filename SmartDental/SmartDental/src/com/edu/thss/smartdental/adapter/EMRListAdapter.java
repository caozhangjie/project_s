package com.edu.thss.smartdental.adapter;

import java.util.ArrayList;

import com.edu.thss.smartdental.OneEMRActivity;
import com.edu.thss.smartdental.R;
import com.edu.thss.smartdental.model.EMRElement;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("DefaultLocale")
public class EMRListAdapter extends BaseAdapter implements Filterable{

	private class buttonViewHolder{
		Button read; //阅读
		Button delete; //删除
		Button hide; //隐藏
		Button mark;//标记
	}
	private ArrayList<EMRElement> list;
	private Context context;
	private EMRFilter filter;
	private buttonViewHolder holder;
	private ImageView icon;
	
	public EMRListAdapter(ArrayList<EMRElement> list,Context context){
		this.list = list;
		this.context = context;
	}
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.emr_list_item, null);
		}
		EMRElement emr = list.get(position);
		//add!!!!
		TextView name =(TextView)convertView.findViewById(R.id.emr_list_item_title);
		TextView description = (TextView)convertView.findViewById(R.id.emr_list_item_description);
		TextView time = (TextView)convertView.findViewById(R.id.emr_list_item_time);
		
		name.setText(emr.name);
		description.setText(emr.description);
		time.setText(emr.time);
		
		icon = (ImageView)convertView.findViewById(R.id.emr_list_item_icon);
		if(emr.isMarked == true){
			icon.setBackgroundResource(R.drawable.ic_mark);
		}else{
			icon.setBackgroundColor(Color.WHITE);
		}
		
		holder = new buttonViewHolder();
		holder.read = (Button)convertView.findViewById(R.id.emr_list_item_read);
		holder.delete = (Button)convertView.findViewById(R.id.emr_list_item_delete);
		holder.hide = (Button)convertView.findViewById(R.id.emr_list_item_hide);
		holder.mark = (Button)convertView.findViewById(R.id.emr_list_item_hint);
		
		holder.hide.setText(emr.isHidden?"不隐藏":"隐藏");
		holder.mark.setText(emr.isMarked?"不标记":"标记");
		holder.read.setText(emr.isRead?"已读":"未读");
		
		holder.read.setOnClickListener(new ButtonListner(position));
		holder.delete.setOnClickListener(new ButtonListner(position));
		holder.mark.setOnClickListener(new ButtonListner(position));
		holder.hide.setOnClickListener(new ButtonListner(position));
		return convertView;
	}
    
	@Override
	public Filter getFilter() {
		if(filter == null){
			filter = new EMRFilter(list);
		}
		return filter;
	}
	@SuppressLint("DefaultLocale")
	public class EMRFilter extends Filter{
		
		private ArrayList<EMRElement> original;
		
		public EMRFilter(ArrayList<EMRElement> list){
			this.original = list;
		}

		@SuppressLint("DefaultLocale")
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			
			String[] strs = constraint.toString().split(" ");
			Log.i("constraint", constraint.toString());
			String emrclass = strs[0];
			String searchword = strs[1];
			Log.i("emrclass", emrclass);
			Log.i("searchword", searchword);
			
			FilterResults results = new FilterResults();
			if(searchword == null || searchword.length()==0){ //没有过滤条件
				results.values = this.original;
				results.count = this.original.size();
				
			}
			else{
				
				ArrayList<EMRElement> mList = new ArrayList<EMRElement>();
				for(EMRElement emr: original){
					if(emr.name.toUpperCase().contains(searchword.toString().toUpperCase())
					   ||emr.description.toUpperCase().contains(searchword.toString().toUpperCase())
					   ||emr.time.contains(searchword)){
						if(emrclass.equals("0")||Integer.parseInt(emrclass)==emr.emrClass)
						mList.add(emr);
					}
				}
				results.values = mList;
				results.count = mList.size();
			}
			return results;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
			list = (ArrayList<EMRElement>)results.values;
			notifyDataSetChanged();
			
		}

	}
    /**
     * button 监听事件
     * */
	
	class ButtonListner implements OnClickListener{
		private int itemPosition;
		public ButtonListner(int pos){
			this.itemPosition = pos;
		}

		@Override
		public void onClick(View v) {
			int vid = v.getId();
			if(vid == holder.delete.getId()){
			 //删除
				list.remove(itemPosition);
				notifyDataSetChanged();
			}
		    if(vid == holder.hide.getId()){
		    	EMRElement temp = list.get(this.itemPosition);
		    	if(temp.isHidden == false)
		    	temp.isHidden = true;
		    	else temp.isHidden = false;
		    	list.set(this.itemPosition, temp);
		    	notifyDataSetChanged();
		    }
		    if(vid == holder.mark.getId()){
		    	
		    	EMRElement temp = list.get(this.itemPosition);
		    	if(temp.isMarked ==false)
		    	temp.isMarked = true;
		    	else temp.isMarked = false;
		    	list.set(this.itemPosition, temp);
		    	notifyDataSetChanged();
		    	
		    }
		    if(vid == holder.read.getId()){
		    	EMRElement temp = list.get(this.itemPosition);
		    	if(temp.isRead == false)
		    	temp.isRead = true;
		    	list.set(this.itemPosition, temp);
		    	notifyDataSetChanged();
		    	Intent intent = new Intent(context,OneEMRActivity.class);
		    	int tempclass = 3;
		    	if(this.itemPosition == 2)  tempclass = 2;
		    	intent.putExtra("emrclass",tempclass);
		    	intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
		    	context.startActivity(intent);
		    }
			
		}
		
	}
}
