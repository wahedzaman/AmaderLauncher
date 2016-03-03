package com.durbinsoft.amarlauncher;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Md.Wahuduzzaman on 3/3/2016.
 */
public class PreferenceClassForData {

    private static boolean initialSetup;
    private static String app1;
    private static String app2;
    private static String app3;
    private static String app4;
    private static String app5;
    private static String app6;
    private static String app7;
    private static String app8;
    private static String app9;
    private static String app10;
    private static String app11;
    private static String app12;
    private static String app13;
    private static String app14;

    static SharedPreferences sp;
    Context mContext;
    static SharedPreferences.Editor editor;


    private String SP_NAME = "AMAR_LAUNCHER_SP";
    private String SP_INITIATED = "AMAR_LAUNCHER_INITIATED";
    public String SP_APP1 = "AMAR_LAUNCHER_APP1";
    public String SP_APP2 = "AMAR_LAUNCHER_APP2";
    public String SP_APP3 = "AMAR_LAUNCHER_APP3";
    public String SP_APP4 = "AMAR_LAUNCHER_APP4";
    public String SP_APP5 = "AMAR_LAUNCHER_APP5";
    public String SP_APP6 = "AMAR_LAUNCHER_APP6";
    public String SP_APP7 = "AMAR_LAUNCHER_APP7";
    public String SP_APP8 = "AMAR_LAUNCHER_APP8";
    public String SP_APP9 = "AMAR_LAUNCHER_APP9";
    public String SP_APP10 = "AMAR_LAUNCHER_APP10";
    public String SP_APP11 = "AMAR_LAUNCHER_APP11";
    public String SP_APP12 = "AMAR_LAUNCHER_APP12";
    public String SP_APP13 = "AMAR_LAUNCHER_APP13";
    public String SP_APP14 = "AMAR_LAUNCHER_APP14";

    PreferenceClassForData(Context mContext){
        this.mContext = mContext;
        sp = mContext.getSharedPreferences(SP_NAME,mContext.MODE_PRIVATE);
    }


    public void initializeSharedPrefs(){
        sp = mContext.getSharedPreferences(SP_NAME,mContext.MODE_PRIVATE);

        initialSetup = sp.getBoolean(SP_INITIATED, true);
        app1 = sp.getString(SP_APP1, "com.durbinsoft.amarlauncher");
        app2 = sp.getString(SP_APP2, "com.durbinsoft.amarlauncher");
        app3 = sp.getString(SP_APP3, "com.durbinsoft.amarlauncher");
        app4 = sp.getString(SP_APP4, "com.durbinsoft.amarlauncher");
        app5 = sp.getString(SP_APP5, "com.durbinsoft.amarlauncher");
        app6 = sp.getString(SP_APP6, "com.durbinsoft.amarlauncher");
        app7 = sp.getString(SP_APP7, "com.durbinsoft.amarlauncher");
        app8 = sp.getString(SP_APP8, "com.durbinsoft.amarlauncher");
        app9 = sp.getString(SP_APP9, "com.durbinsoft.amarlauncher");
        app10 = sp.getString(SP_APP10, "com.durbinsoft.amarlauncher");
        app11 = sp.getString(SP_APP11, "com.durbinsoft.amarlauncher");
        app12 = sp.getString(SP_APP12, "com.durbinsoft.amarlauncher");
        app13 = sp.getString(SP_APP13, "com.durbinsoft.amarlauncher");
        app14 = sp.getString(SP_APP14, "com.durbinsoft.amarlauncher");
    }

    public String getSelectedApp(int x){
        initializeSharedPrefs();
        String  val = "nu";
        switch (x){
            case 1:
                val = app1;
                break;
            case 2:
                val = app2;
                break;
            case 3:
                val = app3;
                break;
            case 4:
                val = app4;
                break;
            case 5:
                val = app5;
                break;
            case 6:
                val = app6;
                break;
            case 7:
                val = app7;
                break;
            case 8:
                val = app8;
                break;
            case 9:
                val = app9;
                break;
            case 10:
                val = app10;
                break;
            case 11:
                val = app11;
                break;
            case 12:
                val = app12;
                break;
            case 13:
                val = app13;
                break;
            case 14:
                val = app14;
                break;
        }
        return  val;
    }

    public boolean getBool(){
        return initialSetup;
    }

    private void spHeader(){
        sp = mContext.getSharedPreferences(SP_NAME,mContext.MODE_PRIVATE);
        editor = sp.edit();
    }
    private void spFooter(){
        editor.commit();
    }

    public void setBool(boolean val){
        spHeader();
        editor.putBoolean(SP_INITIATED, val);
        spFooter();
    }

    public void setApps(String ap1,String appPositionPrefsName){
        spHeader();
        editor.putString(appPositionPrefsName,ap1);
        spFooter();
    }

    public void setApps(String ap1,String ap2,String ap3,String ap4){
        spHeader();
        editor.putString(SP_APP1, ap1);
        editor.putString(SP_APP2, ap2);
        editor.putString(SP_APP3, ap3);
        editor.putString(SP_APP4, ap4);
        spFooter();
    }

}
