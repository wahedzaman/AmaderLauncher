package com.durbinsoft.amarlauncher;

/**
 * Created by Md.Wahuduzzaman on 2/22/2016.
 */

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;

public class ApplicationPackage {

    Drawable[] icon;
    String[] packageName;
    String[] appLabel;
    int size;

    PackageManager packageManager;

    public ApplicationPackage(Context c) {
        packageManager = c.getPackageManager();
    }

    public void initializePackages() {
        final Intent myIntent = new Intent(Intent.ACTION_MAIN, null);
        myIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> packageList = packageManager.queryIntentActivities(
                myIntent, 0);

        size = packageList.size();
        icon = new Drawable[size];
        packageName = new String[size];
        appLabel = new String[size];

        for (int i = 0; i < size; i++) {
            icon[i] = packageList.get(i).loadIcon(packageManager);
            packageName[i] = packageList.get(i).activityInfo.packageName;
            appLabel[i] = packageList.get(i).loadLabel(packageManager)
                    .toString();
        }

        sortApps();
    }

    private void sortApps() {
        int i, j;
        String tempLabel,tempPackage;
        Drawable tempIcon;

        for (i = 0; i < size - 1; i++) {
            for (j = i + 1; j < size; j++) {
                if (appLabel[i].compareToIgnoreCase(appLabel[j]) > 0) { // ascending sort
                    tempLabel = appLabel[i];
                    tempIcon = icon[i];
                    tempPackage = packageName[i];

                    appLabel[i] = appLabel[j];
                    icon[i] = icon[j];
                    packageName [i] = packageName[j];

                    appLabel [j] = tempLabel;
                    icon [j] = tempIcon;
                    packageName [j] = tempPackage;

                }
            }
        }
    }

    public Drawable[] getIcons() {
        return icon;
    }

    public String[] getPackageNames() {
        return packageName;
    }

    public String[] getAppLabels() {
        return appLabel;
    }

    public int getNumberOfApplication() {
        return size;
    }

    public Drawable getIcon(int i) {
        return icon[i];
    }

    public String getPackageName(int i) {
        return packageName[i];
    }

    public String getAppLabel(int i) {
        return appLabel[i];
    }
}
