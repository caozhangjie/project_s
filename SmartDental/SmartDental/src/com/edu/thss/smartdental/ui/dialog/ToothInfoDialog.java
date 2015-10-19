package com.edu.thss.smartdental.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.TextView;

public class ToothInfoDialog extends Dialog{
	private DialogInterface.OnClickListener closeListener;
	public ToothInfoDialog(Context context) {
		super(context);
	}

	public ToothInfoDialog(Context context, int theme) {
		super(context, theme);
	}
	
	public static class Builder{
		private Context context;
		private String title;
		private TextView close;
		private String message;
		private DialogInterface.OnClickListener closeListener;
		public Builder(Context context){
			this.context = context;
		}
		public Builder setMessage(String message){
			this.message = message;
			return this;
		}
	}

}
