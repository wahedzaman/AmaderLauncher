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

    public static boolean isGoogleApp = false;
    public static boolean isNewApp = false;

    private static Context myContext;
    private static ApplicationPackage applicationPackage;
    private PreferenceClassForData sPrefs;

    public CustomApplicationDrawerAdapter(Context c, ApplicationPackage ap) {
        myContext = c;
        applicationPackage = ap;
        sPrefs = new PreferenceClassForData(myContext);
        sPrefs.initializeSharedPrefs();

      //  initiatePackage();
    }

    public CustomApplicationDrawerAdapter(){}

    private void initiatePackage(){
        if(sPrefs.getHiddenAppsNumber()>0){
            for(int i=0;i<sPrefs.getHiddenAppsNumber();i++){
                applicationPackage.setHiddenAppPackageName(sPrefs.getHiddenApp(i+1));
            }
            applicationPackage.initializePackages();
        }
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

        if(isGoogleApp){
            viewHolder.text.setText(applicationPackage.getGoogleAppLabel(position));
            viewHolder.icon.setImageDrawable(applicationPackage.getGoogleIcon(position));
        }else if(isNewApp){
            viewHolder.text.setText(applicationPackage.getGoogleAppLabel(position));
            viewHolder.icon.setImageDrawable(applicationPackage.getGoogleIcon(position));
        }else{
            viewHolder.text.setText(applicationPackage.getNewAppLabel(position));
            viewHolder.icon.setImageDrawable(applicationPackage.getIcon(position));
        }

        isGoogleApp = false;
        isNewApp = false;

        return convertedView;
    }

    public boolean getGoogleAppStatus(){
        return isGoogleApp;
    }
    public boolean getNewAppStatus(){
        return isNewApp;
    }

    public void setGoogleAppStatus(boolean x){
        isGoogleApp = x;
    }

    public void setNewAppStatus(boolean x){
        isNewApp = x;
    }

}