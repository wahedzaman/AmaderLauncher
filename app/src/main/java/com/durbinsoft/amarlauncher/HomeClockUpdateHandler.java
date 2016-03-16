package com.durbinsoft.amarlauncher;

import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Md.Wahuduzzaman on 3/16/2016.
 */

public class HomeClockUpdateHandler {
    //text view for the home clock widget
    private static TextView homeClockM,homeClockH,homeClockAP, homeClockExtra;
    static CalenderConversion myCalenderConversion;
    static  boolean init = false;
    static  String batteryLevel;
    Context context;

    HomeClockUpdateHandler(TextView m, TextView h, TextView ap, TextView ex, CalenderConversion cl,Context x){
        homeClockM = m;
        homeClockH = h;
        homeClockAP = ap;
        homeClockExtra = ex;
        myCalenderConversion = cl;
        init = true;
        context = x;
    }

    HomeClockUpdateHandler(){}

    public void updateHomeClockTime(){
        homeClockM.setText(myCalenderConversion.returnHomeCalM());
        homeClockH.setText(myCalenderConversion.returnHomeCalTimeH());
        homeClockAP.setText(myCalenderConversion.getAmpm());
        if(batteryLevel== null){
            batteryLevel = "...";
        }
        homeClockExtra.setText(myCalenderConversion.returnHomeExtra()+batteryLevel);
    }

    public boolean isInitialized(){
        return init;
    }

    public void setBatteryLevel(String bat){
        batteryLevel = bat;
    }

}
