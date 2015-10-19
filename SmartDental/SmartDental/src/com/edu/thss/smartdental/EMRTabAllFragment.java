package com.edu.thss.smartdental;

import java.util.ArrayList;

import com.edu.thss.smartdental.adapter.EMRListAdapter;
import com.edu.thss.smartdental.model.EMRElement;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;



public class EMRTabAllFragment extends Fragment {

	private EMRListAdapter listAdapter;
	private EditText editText;
	private ListView list;
	private ArrayList<EMRElement> emrs;
	
	private Spinner spinner;
	private String searchClass; //选择病历类别：门诊、住院、其他
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_emr_all, container,false);
		editText = (EditText)rootView.findViewById(R.id.emr_searchbox);
		editText.addTextChangedListener(filterTextWatcher);
		list = (ListView)rootView.findViewById(R.id.emr_list);
		initEMRS();
		listAdapter = new EMRListAdapter(emrs,this.getActivity().getApplicationContext());
		list.setAdapter(listAdapter);
		
		searchClass="0";
		spinner = (Spinner)rootView.findViewById(R.id.emr_all_spinner);
		//建立数据源
		String[] spinnerItems = getResources().getStringArray(R.array.spinnername);
		//绑定数据源
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,spinnerItems);
		//绑定控件
		spinner.setAdapter(adapter);
		//添加事件
		spinner.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
			   searchClass = Integer.toString(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}});
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
			s = searchClass+" "+s;
			Log.i("s", s.toString());
			listAdapter.getFilter().filter(s);	
		}
	};

	private void initEMRS(){
		emrs = new ArrayList<EMRElement>();
		EMRElement e = new EMRElement("病理申请","检查申请","2014-1-13",2);
		emrs.add(e);
		e = new EMRElement("体格检查","口腔颌面外科检查","2014-1-13",1);
		emrs.add(e);
		e = new EMRElement("病理检查报告单","记录一次病历检查的结果","2014-1-20",2);
		emrs.add(e);
		e = new EMRElement("病程记录","记录在口腔医院一次看诊过程","2014-1-20",2);
		emrs.add(e);
		e = new EMRElement("麻醉记录","记录一次麻醉手术过程中的身体情况","2014-1-20",2);
		emrs.add(e);
		e = new EMRElement("出院记录","记录在口腔医院住院情况","2014-1-20",2);
		emrs.add(e);
	}

}
