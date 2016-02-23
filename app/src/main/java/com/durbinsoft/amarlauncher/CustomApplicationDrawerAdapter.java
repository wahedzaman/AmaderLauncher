package com.durbinsoft.amarlauncher;

/**
 * Created by Md.Wahuduzzaman on 2/22/2016.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomApplicationDrawerAdapter extends BaseAdapter{

    private Context myContext;
    private ApplicationPackage applicationPackage;

    public CustomApplicationDrawerAdapter(Context c, ApplicationPackage ap) {
        myContext = c;
        applicationPackage = ap;
    }

    @Override
    public int getCount() {
        return applicationPackage.getNumberOfApplication();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    static class ViewHolder{
        TextView text;
        ImageView icon;
    }

    @Override
    public View getView(int position, View convertedView, ViewGroup arg2) {

        ViewHolder viewHolder = new ViewHolder();
        LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(convertedView == null){
            convertedView = inflater.inflate(R.layout.app_items, null);
            viewHolder = new ViewHolder();
            viewHolder.text = (TextView)convertedView.findViewById(R.id.app_icon_label_text);
            viewHolder.icon = (ImageView) convertedView.findViewById(R.id.app_icon_image);

            convertedView.setTag(viewHolder);

        }else{
            viewHolder = (ViewHolder) convertedView.getTag();
        }

        viewHolder.text.setText(applicationPackage.getAppLabel(position));
        viewHolder.icon.setImageDrawable(applicationPackage.getIcon(position));


        return convertedView;
    }

}