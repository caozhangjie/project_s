package com.edu.thss.smartdental.adapter;

import java.util.ArrayList;

import com.edu.thss.smartdental.OneEMRActivity;
import com.edu.thss.smartdental.OneImageActivity;
import com.edu.thss.smartdental.R;
import com.edu.thss.smartdental.model.ImageElement;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;


public class ImgListAdapter extends BaseAdapter implements Filterable{
	private class buttonViewHolder{
		Button read; //阅读
		Button delete; //删除
		Button hide; //隐藏
		Button mark;//标记
	}
	private ArrayList<ImageElement> list;
	private Context context;
	private ImageFilter filter;
	private buttonViewHolder holder;
	
	public ImgListAdapter(ArrayList<ImageElement> list, Context context){
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
			convertView = LayoutInflater.from(context).inflate(R.layout.image_list_item, null);
		}
		ImageElement image = list.get(position);
		//add!!!!
		TextView name =(TextView)convertView.findViewById(R.id.image_list_item_title);
		TextView description = (TextView)convertView.findViewById(R.id.image_list_item_description);
		TextView time = (TextView)convertView.findViewById(R.id.image_list_item_time);
		
		name.setText(image.name);
		description.setText(image.description);
		time.setText(image.time);
		
		holder = new buttonViewHolder();
		holder.read = (Button)convertView.findViewById(R.id.image_list_item_read);
		holder.delete = (Button)convertView.findViewById(R.id.image_list_item_delete);
		holder.hide = (Button)convertView.findViewById(R.id.image_list_item_hide);
		holder.mark = (Button)convertView.findViewById(R.id.image_list_item_hint);
		
		holder.hide.setText(image.isHidden?"不隐藏":"隐藏");
		holder.mark.setText(image.isMarked?"不标记":"标记");
		holder.read.setText(image.isRead?"已读":"未读");
		
		holder.read.setOnClickListener(new ButtonListner(position));
		holder.delete.setOnClickListener(new ButtonListner(position));
		holder.mark.setOnClickListener(new ButtonListner(position));
		holder.hide.setOnClickListener(new ButtonListner(position));
		return convertView;
	}

	@Override
	public Filter getFilter() {
		if(filter == null){
			filter = new ImageFilter(list);
		}
		return filter;
	}
    public class ImageFilter extends Filter{
    	private ArrayList<ImageElement> original;
    	public ImageFilter(ArrayList<ImageElement> list){
    		this.original = list;
    	}

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			
			FilterResults results = new FilterResults();
			if(constraint == null || constraint.length()==0){ //没有过滤条件
				results.values = this.original;
				results.count = this.original.size();
				
			}
			else{
				
				ArrayList<ImageElement> mList = new ArrayList<ImageElement>();
				for(ImageElement image: original){
					if(image.name.toUpperCase().contains(constraint.toString().toUpperCase())
					   ||image.description.toUpperCase().contains(constraint.toString().toUpperCase())
					   ||image.time.contains(constraint)){
						
						mList.add(image);
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
			list = (ArrayList<ImageElement>)results.values;
			notifyDataSetChanged();
			
		}
    }
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
		    	ImageElement temp = list.get(this.itemPosition);
		    	if(temp.isHidden == false)
		    	temp.isHidden = true;
		    	else temp.isHidden = false;
		    	list.set(this.itemPosition, temp);
		    	notifyDataSetChanged();
		    }
		    if(vid == holder.mark.getId()){
		    	
		    	ImageElement temp = list.get(this.itemPosition);
		    	if(temp.isMarked ==false)
		    	temp.isMarked = true;
		    	else temp.isMarked = false;
		    	list.set(this.itemPosition, temp);
		    	notifyDataSetChanged();
		    }
		    if(vid == holder.read.getId()){
		    	ImageElement temp = list.get(this.itemPosition);
		    	if(temp.isRead == false)
		    	temp.isRead = true;
		    	list.set(this.itemPosition, temp);
		    	notifyDataSetChanged();
		    	Intent intent = new Intent(context,OneImageActivity.class);
		    	intent.putExtra("imageclass", this.itemPosition);
		    	intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
		    	context.startActivity(intent);
		    }
			
		}
		
	}
}
