package com.durbinsoft.amarlauncher;

/**
 * Created by Md.Wahuduzzaman on 2/22/2016.
 */

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class AppDrawerClickListener implements OnItemClickListener, AdapterView.OnItemLongClickListener{

    Context myContext;
    ApplicationPackage applicationPackages;
    PackageManager packageManager;
    LinearLayout detailsMenu;
    GridView appDrawer;

    String appPackName;

    DisplayMetrics dmetrics = new DisplayMetrics();
    int heightPixels=dmetrics.heightPixels;

    private boolean isAppDrawerLongpressDetailsVisible = false;
    private static boolean isItLongPress = false;

    public AppDrawerClickListener(Context c, ApplicationPackage ap){
        myContext = c;
        packageManager = c.getPackageManager();
        applicationPackages = ap;
    }
    public AppDrawerClickListener(Context c, ApplicationPackage ap,LinearLayout appDetailsMenu, GridView appDrawerView){
        myContext = c;
        packageManager = c.getPackageManager();
        applicationPackages = ap;
        detailsMenu = appDetailsMenu;
        appDrawer = appDrawerView;
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
        if(isItLongPress == false){
            Intent  launchIntent;
            launchIntent = packageManager.getLaunchIntentForPackage(applicationPackages.getPackageName(position));
            myContext.startActivity(launchIntent);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        isItLongPress = true;
        appPackName = applicationPackages.getPackageName(position);
        getDetailsMenuInView();
        return false;
    }

    public void getDetailsMenuInView(){
        // direction true == in and false == out
        if(isAppDrawerLongpressDetailsVisible == false){
            appDrawer.setEnabled(false);
            detailsMenu.setVisibility(View.VISIBLE);
            detailsMenu.setAlpha(0.0f);
            detailsMenu.animate().translationY(heightPixels).alpha(1.0f);
            isAppDrawerLongpressDetailsVisible = true;
        }else{
            appDrawer.setEnabled(true);
            //mainHomeView.animate().translationX(0);
            detailsMenu.animate().translationY(detailsMenu.getHeight()).alpha(0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    detailsMenu.setVisibility(View.VISIBLE);
                }
            });
            isItLongPress = false;
            isAppDrawerLongpressDetailsVisible = false;
        }
    }

    public boolean getAppDetailsMenuVisibility(){
        return  isAppDrawerLongpressDetailsVisible;
    }

    public String getPressedAppName(){
        return appPackName;
    }

    public void resetVisibilityAndOther(){
        appDrawer.setEnabled(true);
        getDetailsMenuInView();
    }
}
