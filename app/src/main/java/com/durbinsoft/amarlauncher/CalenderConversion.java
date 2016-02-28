package com.durbinsoft.amarlauncher;

import android.os.Debug;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Md.Wahuduzzaman on 2/28/2016.
 */
public class CalenderConversion {

    private String [] days_in_bangla = {"শনিবার","রবিবার", "সোমবার","মঙ্গলবার", "বুধবার", "বৃহস্পতিবার","শুক্রবার"};
    private String [] days_in_english = {"sat","sun","mon","tue","wed","thu"};

    private String todayActual;
    private String todayConverted;

    private String time;
    private String timeConverted;

    private String date;
    private String dateConverted;



    public void checkTest(){
        parseDateFormateString(checkCurrentTimeStatus());
    }

    public String returnConvertedCal(){

        return todayConverted;
    }

    public String returnActalCal(){

        return todayActual;
    }

    private String convertDay(String t_day){
        t_day = t_day.toLowerCase();
        for(int i=0;i<7;i++){
            if(days_in_english[i].equals(t_day)){
                t_day = days_in_bangla[i];
                break;
            }
        }
        return t_day;
    }

    private String[] checkCurrentTimeStatus(){
        DateFormat df;

       String [] dateVal = new String[4];

        df = new SimpleDateFormat("EEE, d");
        dateVal[0]=df.format(Calendar.getInstance().getTime());
        df = new SimpleDateFormat("EEE, MMM");
        dateVal[1]=df.format(Calendar.getInstance().getTime());
        df = new SimpleDateFormat("EEE, yyyy");
        dateVal[2]=df.format(Calendar.getInstance().getTime());
        df = new SimpleDateFormat("EEE, HH:mm");
        dateVal[3]=df.format(Calendar.getInstance().getTime());

        return dateVal;
    }

    private void parseDateFormateString(String val[]){
        String day = val[0];
        day = day.replaceAll("\\s", "");
        String date = day.substring(4);
        day = day.substring(0, 3);
        String month = val[1];
        month = month.replaceAll("\\s","");
        String year = val[2];
        year = year.replaceAll("\\s","");
        String time = val[3];
        time = time.replaceAll("\\s","");
        Log.d("-----------------------","day:" + day + " date: " + date + " month: " + month + " year: " + year + " time;" + time);
    }

}
