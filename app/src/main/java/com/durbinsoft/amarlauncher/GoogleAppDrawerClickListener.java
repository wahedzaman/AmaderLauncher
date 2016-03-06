package com.durbinsoft.amarlauncher;

/**
 * Created by Md.Wahuduzzaman on 2/22/2016.
 */

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class GoogleAppDrawerClickListener implements OnItemClickListener {

    Context myContext;
    ApplicationPackage applicationPackages;
    PackageManager packageManager;


    public GoogleAppDrawerClickListener(Context c, ApplicationPackage ap) {
        myContext = c;
        packageManager = c.getPackageManager();
        applicationPackages = ap;
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

        Intent launchIntent;
        launchIntent = packageManager.getLaunchIntentForPackage(applicationPackages.getGooglePackageName(position));
        myContext.startActivity(launchIntent);

    }
}
