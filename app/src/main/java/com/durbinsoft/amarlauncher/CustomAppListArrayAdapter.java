package com.durbinsoft.amarlauncher;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Md.Wahuduzzaman on 3/3/2016.
 */
public class CustomAppListArrayAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] appLabels;
    private final Drawable[] imageIds;

    public CustomAppListArrayAdapter(Activity context, String[] appLabel, Drawable[] imageIds) {
        super(context, R.layout.app_choser_row, appLabel);
        this.context = context;
        this.appLabels = appLabel;
        this.imageIds = imageIds;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.app_choser_row, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.txtAppliationLabelRow);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.iconAppliationLabelRow);
        txtTitle.setText(appLabels[position]);
        imageView.setImageDrawable(imageIds[position]);
        return rowView;
    }
}