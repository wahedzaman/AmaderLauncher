package com.durbinsoft.amarlauncher;

/**
 * Created by Md.Wahuduzzaman on 2/22/2016.
 */

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

public class ApplicationPackage {

    ///// EVERY NEW APP SHOULD RESET AFTER THREE TIMES PRESSED.. SET A STATIC COUNTER AND COUNT UNTIL THREE....

    static Drawable[] icon;
    static String[] packageName;
    static String[] appLabel;
    static int size;

    static ArrayList<String> hiddenAppPackageName = new ArrayList<String>();
    static int hiddenSize=0;

    static Drawable[] deliverableIcon;
    static String[] deliverablePackageName;
    static String[] deliverableAppLabel;
    static int deliverableSize;

    static Drawable[] newIcon;
    static String[] newPackageName;
    static String[] newAppLabel;
    static int newAppSize;

    final String[] gAppsPacks = {"com.google.android.apps.authenticator2","com.google.android.apps.blogger","com.google.android.calendar","com.android.chrome","com.google.android.apps.adm","com.google.android.apps.docs.editors.docs","com.google.android.apps.docs","com.google.android.gm","com.google.android.googlequicksearchbox","com.google.android.launcher","com.google.android.apps.pdfviewer","com.google.android.play.games","com.google.android.apps.magazines","com.android.vending","com.google.android.talk","com.google.android.keep","com.google.android.apps.maps","com.google.android.apps.messaging","com.google.android.apps.genie.geniewidget","com.google.chromeremotedesktop","com.google.android.apps.docs.editors.slides","com.niksoftware.snapseed","com.google.android.street","com.google.android.youtube"};

    static boolean isAppHidden = false;

    private Context mContext;

    PackageManager packageManager;

    ThemeManager themeManager;

    private static boolean isThemeChanged = false;
    private static PreferenceClassForData sPrefs;

    public ApplicationPackage(){}

    public ApplicationPackage(Context c, PreferenceClassForData prefs) {
        mContext = c;
        sPrefs = prefs;
        packageManager = c.getPackageManager();
        themeManager = new ThemeManager(mContext);
        themeManager.updateThemeManager(sPrefs);
    }

    public void setBooleanTheme(boolean booleanTheme, PreferenceClassForData prefs, ArrayList<String> iconPacks){
        isThemeChanged = true;
        sPrefs = prefs;
        themeManager.setCurrentIconPacName(iconPacks);
        themeManager.updateThemeManager(sPrefs);
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
        int index;

        //use isThemeChanged bool to call func from ThemeManager accordingly

        for (int i = 0; i < size; i++) {
            packageName[i] = packageList.get(i).activityInfo.packageName;
            appLabel[i] = packageList.get(i).loadLabel(packageManager)
                    .toString();

                index = themeManager.getThemeIconIndex(appLabel[i],packageName[i]);
                if(index == -1){
                    //icon[i] = packageList.get(i).loadIcon(packageManager);
                    icon[i] = themeManager.createNewThemeIcon(packageList.get(i).loadIcon(packageManager));
                }else {
                    icon[i] = themeManager.getThemeIcon(index);
                }

        }
        sortApps();
        //resortForHiddenApps();
        //initiateGooglePacks();
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


    public int searchAndReturnPackage(String iPackageName){
        int packageIndexNumber =-1;
        for(int i=0;i<size;i++){
            if(packageName.length>0){
                if(iPackageName.equals("com.durbinsoft.amarlauncher")){
                    packageIndexNumber =-1;
                }
                else if(packageName[i].equals(iPackageName)){
                    packageIndexNumber = i;
                }
            }

        }
        return packageIndexNumber;
    }
    public void resortForHiddenApps(){
        if(hiddenSize>0){
            deliverableSize = size-hiddenSize;
            deliverableAppLabel = new String[deliverableSize];
            deliverablePackageName = new String[deliverableSize];
            deliverableIcon = new Drawable[deliverableSize];
            int j;
            int i =0;
            int m =0;
            int k;

            boolean matched = false;

            for(j =m; j<size;j++,m++){
                for(k = 0; k<hiddenSize;k++){
                    if(packageName[j].equals(hiddenAppPackageName.get(k))){
                        matched = true;
                        break;
                    }
                }
                if(matched) {
                    matched = false;
                }
                else if((matched==false)&&(k==hiddenSize)){

                    deliverablePackageName[i] =packageName[j];
                    deliverableAppLabel[i] =appLabel[j];
                    deliverableIcon[i] =icon[j];
                    i++;
                }
            }


        }else{
            deliverableSize = size;
            deliverableAppLabel = new String[deliverableSize];
            deliverablePackageName = new String[deliverableSize];
            deliverableIcon = new Drawable[deliverableSize];

            for(int i=0;i<size;i++){
                deliverableAppLabel[i] = appLabel[i];
                deliverablePackageName[i] = packageName[i];
                deliverableIcon[i] = icon[i];
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
        Drawable ico;
        if(i==-1){
            //ico = mContext.getResources().getDrawable(R.drawable.blueblurbg);
            //  d.setAlpha(200);
            //  bottomDrawerView.setBackground(d);

              Bitmap tmpImg = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.defaultaddicon);
            ico = new BitmapDrawable(mContext.getResources(),tmpImg);
            //   BitmapDrawable draw = new BitmapDrawable(Bitmap.createScaledBitmap(tmpImg, 16, 16, false));
            //   airplaneToggleButton.setBackground(draw);
        }else{
            ico = icon[i];
        }
        return ico;
    }

    public String getPackageName(int i) {
        return packageName[i];
    }

    public String getAppLabel(int i) {
        return appLabel[i];
    }

    public String getNewAppLabel(int i) {
        return appLabel[i];
    }

    public String getNewPackageName(int i) {
        return packageName[i];
    }

    public void setHiddenAppPackageName(String hidPackName){
        hiddenSize ++;
        hiddenAppPackageName.add(hidPackName);
    }

}
