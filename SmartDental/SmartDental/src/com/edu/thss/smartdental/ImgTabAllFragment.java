package com.edu.thss.smartdental;

import java.util.ArrayList;

import com.edu.thss.smartdental.adapter.EMRListAdapter;
import com.edu.thss.smartdental.adapter.ImgListAdapter;
import com.edu.thss.smartdental.model.EMRElement;
import com.edu.thss.smartdental.model.ImageElement;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class ImgTabAllFragment extends Fragment{
	private ImgListAdapter listAdapter;
	private EditText editText;
	private ListView list;
	private ArrayList<ImageElement> images;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_image_all, container,false);
		editText = (EditText)rootView.findViewById(R.id.image_searchbox);
		editText.addTextChangedListener(filterTextWatcher);
		list = (ListView)rootView.findViewById(R.id.image_list);
		initImages();
		listAdapter = new ImgListAdapter(images,this.getActivity().getApplicationContext());
		list.setAdapter(listAdapter);
		
		return rootView;
	}

	private TextWatcher filterTextWatcher = new TextWatcher(){

		@Override
		public void afterTextChanged(Editable s) {
			
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			listAdapter.getFilter().filter(s);	
		}
	};

	private void initImages(){
		images = new ArrayList<ImageElement>();
		ImageElement i = new ImageElement("活检1_*40","活体组织第一次检查，镜下*40","2011-1-15");
		images.add(i);
		i = new ImageElement("活检1_*100","活体组织第一次检查，镜下*100","2011-1-15");
		images.add(i);
		i = new ImageElement("活检2_*40","活体组织第二次检查，镜下*40","2012-1-25");
		images.add(i);
		i = new ImageElement("活检2_*100","活体组织第二次检查，镜下*100","2013-1-25");
		images.add(i);
	}
}
