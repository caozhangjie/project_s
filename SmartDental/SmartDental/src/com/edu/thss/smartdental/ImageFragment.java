package com.edu.thss.smartdental;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

public class ImageFragment extends Fragment {

	private FragmentManager fragmentManager;
	private RadioGroup radioGroup;
	
	public ImageFragment(){
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_image, container,false);
		
		fragmentManager = getFragmentManager();
		radioGroup = (RadioGroup)rootView.findViewById(R.id.img_tab);
		radioGroup.check(R.id.img_tab_all);
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
		case 0: tempfragment = new ImgTabAllFragment();break; //
		default: break;
		}
		if(tempfragment != null){
        	transaction.replace(R.id.img_tab_content, tempfragment);
        	transaction.commit();
        }
	}
	
}
