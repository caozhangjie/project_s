package com.edu.thss.smartdental;

import java.util.ArrayList;

import com.edu.thss.smartdental.adapter.EMRListAdapter;
import com.edu.thss.smartdental.model.EMRElement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class EMRFragment extends Fragment{
	private FragmentManager fragmentManager;
	private RadioGroup radioGroup;
	
	public EMRFragment(){
		
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_emr, container,false);
		
		fragmentManager = getFragmentManager();
		radioGroup = (RadioGroup)rootView.findViewById(R.id.emr_tab);
		radioGroup.check(R.id.emr_tab_all);
		changeFragment(0);
		radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				switch(checkedId){
				case R.id.emr_tab_all: changeFragment(0); break;
				//TODO ADD OTHER FRAGMENT
				}
               
			}} );
		return rootView;
	}
	private void changeFragment(int index){
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		Fragment tempfragment = null;
		switch(index){
		case 0: tempfragment = new EMRTabAllFragment();break;
		default: break;
		}
		if(tempfragment != null){
        	transaction.replace(R.id.emr_tab_content, tempfragment);
        	transaction.commit();
        }
	}
}
