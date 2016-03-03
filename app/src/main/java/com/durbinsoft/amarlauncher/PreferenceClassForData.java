package com.durbinsoft.amarlauncher;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Md.Wahuduzzaman on 3/3/2016.
 */
public class PreferenceClassForData {

    private static boolean initialSetup;
    private static int app1;
    private static int app2;
    private static int app3;
    private static int app4;

    static SharedPreferences sp;
    Context mContext;
    static SharedPreferences.Editor editor;


    private String SP_NAME = "AMAR_LAUNCHER_SP";
    private String SP_INITIATED = "AMAR_LAUNCHER_INITIATED";
    private String SP_APP1 = "AMAR_LAUNCHER_APP1";
    private String SP_APP2 = "AMAR_LAUNCHER_APP2";
    private String SP_APP3 = "AMAR_LAUNCHER_APP3";
    private String SP_APP4 = "AMAR_LAUNCHER_APP4";

    PreferenceClassForData(Context mContext){
        this.mContext = mContext;
        sp = mContext.getSharedPreferences(SP_NAME,mContext.MODE_PRIVATE);
    }


    public void initializeSharedPrefs(){
        sp = mContext.getSharedPreferences(SP_NAME,mContext.MODE_PRIVATE);

        initialSetup = sp.getBoolean(SP_INITIATED,true);
        app1 = sp.getInt(SP_APP1,-1);
        app2 = sp.getInt(SP_APP2,-1);
        app3 = sp.getInt(SP_APP3,-1);
        app4 = sp.getInt(SP_APP4,-1);
    }

    public int getSelectedApp(int x){
        int val = -1;
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
    public void setApps(int ap1,int ap2,int ap3,int ap4){
        spHeader();
        editor.putInt(SP_APP1,ap1);
        editor.putInt(SP_APP2,ap2);
        editor.putInt(SP_APP3,ap3);
        editor.putInt(SP_APP4,ap4);
        spFooter();
    }



}
