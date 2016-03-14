package com.durbinsoft.amarlauncher;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Md.Wahuduzzaman on 3/3/2016.
 */
public class PreferenceClassForData {

    private static boolean initialSetup;

    private static int hiddenAppsNumber;

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

    private static String Happ1;
    private static String Happ2;
    private static String Happ3;
    private static String Happ4;
    private static String Happ5;
    private static String Happ6;
    private static String Happ7;
    private static String Happ8;
    private static String Happ9;
    private static String Happ10;


    static SharedPreferences sp;
    Context mContext;
    static SharedPreferences.Editor editor;

    static boolean isAnyChangeMade = false;


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

    public String SP_HAPP1 = "AMAR_LAUNCHER_HAPP1";
    public String SP_HAPP2 = "AMAR_LAUNCHER_HAPP2";
    public String SP_HAPP3 = "AMAR_LAUNCHER_HAPP3";
    public String SP_HAPP4 = "AMAR_LAUNCHER_HAPP4";
    public String SP_HAPP5 = "AMAR_LAUNCHER_HAPP5";
    public String SP_HAPP6 = "AMAR_LAUNCHER_HAPP6";
    public String SP_HAPP7 = "AMAR_LAUNCHER_HAPP7";
    public String SP_HAPP8 = "AMAR_LAUNCHER_HAPP8";
    public String SP_HAPP9 = "AMAR_LAUNCHER_HAPP9";
    public String SP_HAPP10 = "AMAR_LAUNCHER_HAPP10";

    public String SP_HAPPNumber = "AMAR_LAUNCHER_HAPPNUMBER";

    public static int lockAppsConter = 0;
    public String SP_LCOUNTER = "AMAR_LAUNCHER_LOCK_COUNTER";

    private static ArrayList<String> lockedApps = new ArrayList<String>();

    PreferenceClassForData(Context mContext){
        this.mContext = mContext;
        sp = mContext.getSharedPreferences(SP_NAME,mContext.MODE_PRIVATE);
    }


    public void initializeSharedPrefs(){
        sp = mContext.getSharedPreferences(SP_NAME, mContext.MODE_PRIVATE);

        initialSetup = sp.getBoolean(SP_INITIATED, true);
        hiddenAppsNumber = sp.getInt(SP_HAPPNumber, 0);
        lockAppsConter = sp.getInt(SP_LCOUNTER,0);

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

        if(hiddenAppsNumber>0){
            Happ1 = sp.getString(SP_HAPP1, "com.durbinsoft.amarlauncher");
            Happ2 = sp.getString(SP_HAPP2, "com.durbinsoft.amarlauncher");
            Happ3 = sp.getString(SP_HAPP3, "com.durbinsoft.amarlauncher");
            Happ4 = sp.getString(SP_HAPP4, "com.durbinsoft.amarlauncher");
            Happ5 = sp.getString(SP_HAPP5, "com.durbinsoft.amarlauncher");
            Happ6 = sp.getString(SP_HAPP6, "com.durbinsoft.amarlauncher");
            Happ7 = sp.getString(SP_HAPP7, "com.durbinsoft.amarlauncher");
            Happ8 = sp.getString(SP_HAPP8, "com.durbinsoft.amarlauncher");
            Happ9 = sp.getString(SP_HAPP9, "com.durbinsoft.amarlauncher");
            Happ10 = sp.getString(SP_HAPP10, "com.durbinsoft.amarlauncher");

        }
        if(lockAppsConter>0){
            initLockedApps();
        }
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

    public String getHiddenApp(int x){
        initializeSharedPrefs();
        String  val = "nu";
        switch (x){
            case 1:
                val =Happ1;
                break;
            case 2:
                val = Happ2;
                break;
            case 3:
                val = Happ3;
                break;
            case 4:
                val = Happ4;
                break;
            case 5:
                val = Happ5;
                break;
            case 6:
                val = Happ6;
                break;
            case 7:
                val = Happ7;
                break;
            case 8:
                val = Happ8;
                break;
            case 9:
                val = Happ9;
                break;
            case 10:
                val = Happ10;
                break;
        }
        return  val;
    }

    public boolean getBool(){
        return initialSetup;
    }

    public int getHiddenAppsNumber(){
        return hiddenAppsNumber;
    }

    public void setBool(boolean val){
        spHeader();
        editor.putBoolean(SP_INITIATED, val);
        spFooter();
    }

    public void setChangeMadeBool(boolean val){
        isAnyChangeMade = val;
    }

    public boolean getAnyChangeMadeBool(){ return isAnyChangeMade;}

    public void setApps(String ap1,String appPositionPrefsName){
        spHeader();
        editor.putString(appPositionPrefsName, ap1);
        spFooter();
    }

    public void setApps(String ap1,String ap2,String ap3,String ap4){
        spHeader();
        editor.putString(SP_APP1, ap1);
        editor.putString(SP_APP2, ap2);
        editor.putString(SP_APP3, ap3);
        editor.putString(SP_APP4, ap4);
        spFooter();
        isAnyChangeMade = true;
    }


    private void spHeader(){
        sp = mContext.getSharedPreferences(SP_NAME,mContext.MODE_PRIVATE);
        editor = sp.edit();
    }
    private void spFooter(){
        editor.commit();
    }

    public void setLockedApps(String appPackName){
        String prefsName;
        spHeader();
        if(appPackName.equals("com.durbinsoft.amarlauncher")){
            prefsName = "Locked"+ lockAppsConter;
        }else{
            lockAppsConter ++;
            prefsName = "Locked"+ lockAppsConter;
            Toast.makeText(mContext,"Application Locked!",Toast.LENGTH_SHORT).show();
        }
        editor.putString(prefsName, appPackName);
        editor.putInt(SP_LCOUNTER, lockAppsConter);
        spFooter();
    }

    public boolean checkAppPackLockStat(String appPackName){
        boolean boolStatus = false;
        for(int i=0; i<lockAppsConter;i++){
            if(lockedApps.get(i).equals(appPackName)){
               boolStatus = true;
                break;
            }
        }

        return boolStatus;
    }

    public int getLockAppsConter() {
        return lockAppsConter;
    }

    private void initLockedApps(){
        String prefsName;

        for(int i=0; i<lockAppsConter;i++){
            prefsName= "Locked"+ (i+1);
            lockedApps.add(sp.getString(prefsName,"com.durbinsoft.amarlauncher"));
            Log.d("dfdf",lockedApps.get(i));
        }
    }

    public void removeLockedApp(String appPackName){
        lockAppsConter--;
        int newLockAppsCounter = lockAppsConter;
        ArrayList <String> newLockedApps = new ArrayList<String>();
        if(lockAppsConter == 0){
            setLockedApps("com.durbinsoft.amarlauncher");
        }else{
            int iCounter = 0;
            for(int i =0; i<newLockAppsCounter+1; i++){
                if(checkAppPackLockStat(appPackName)){
                    iCounter++;
                }else {
                    newLockedApps.add(lockedApps.get(i-iCounter));
                    iCounter = 0;
                }
            }
            lockAppsConter = 0;
            Toast.makeText(mContext,"size:"+newLockedApps.size(),Toast.LENGTH_SHORT).show();
            for(int i =0; i<newLockAppsCounter; i++){
                setLockedApps(newLockedApps.get(i));
            }
        }
        newLockAppsCounter = 0;
        newLockedApps = null;
        Toast.makeText(mContext,"Application UnLocked!",Toast.LENGTH_SHORT).show();
    }

    public void toggleAppLock(String appPackNames){
        //Toast.makeText(mContext,"Ap name: "+ appPackNames,Toast.LENGTH_SHORT).show();
        boolean res = checkAppPackLockStat(appPackNames);
        if(res){
            removeLockedApp(appPackNames);
        }else{
            setLockedApps(appPackNames);
        }
        initializeSharedPrefs();
    }

}
