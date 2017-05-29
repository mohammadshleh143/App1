package com.mahkota_company.android.utils;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SpinnerAdapter extends ArrayAdapter<String> {
	Context context;
	ArrayList<String> items;
	private Typeface typefaceSmall;

	public SpinnerAdapter(final Context context, final int textViewResourceId,
			ArrayList<String> objects) {
		super(context, textViewResourceId, objects);
		this.items = objects;
		this.typefaceSmall = Typeface.createFromAsset(context.getAssets(),
				"fonts/AliquamREG.ttf");
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(
					android.R.layout.simple_spinner_item, parent, false);
		}

		TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
		tv.setText(items.get(position));
		tv.setTextColor(Color.BLACK);
		tv.setTextSize(16);
		tv.setTypeface(typefaceSmall);
		return convertView;
	}
}
