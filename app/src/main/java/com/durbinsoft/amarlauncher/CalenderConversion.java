package com.durbinsoft.amarlauncher;

import android.content.Context;
import android.os.Debug;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Md.Wahuduzzaman on 2/28/2016.
 */
public class CalenderConversion {

    private String [] days_in_bangla = {"শনিবার","রবিবার", "সোমবার","মঙ্গলবার", "বুধবার", "বৃহস্পতিবার","শুক্রবার"};
    private String [] days_in_english = {"sat","sun","mon","tue","wed","thu","fri"};

    private String [] bangla_nmber = {"০","১","২","৩","৪","৫","৬","৭","৮","৯"};

    private String [] month_in_bangla = {"জানুয়ারী","ফেরুয়ারী","মার্চ","এপ্রিল","মে","জুন","জুলাই","অগাস্ট","সেপ্টেম্বর","অক্টোবর","নভেম্বর","ডিসেম্বর"};

    private String[] todayActual = new String [2];
    private String[] todayConverted = new String [2];

    private String time;
    private String timeConverted;

    private String day;
    private String dayConverted;

    private String date;
    private String dateConverted;

    private String month;
    private String monthConverted;

    private String rawTimeData,rawDateData,rawDayData;

    private String homeCalTimeH;
    private String homeCalTimeM;
    private String homeExtraDetails;

    private String ampm;


    private void returnConvertedCal(){
        checkCurrentTimeStatus();
        formatTime12();

        timeConverted = convertNumberToBangla(rawTimeData.substring(0,5));
        dateConverted = convertNumberToBangla(rawDateData);
        dayConverted = convertDay(rawDayData);
        dateConverted = dateConverted.substring(0,2)+","+dateConverted.substring(6);
        monthConverted = convertMonth(rawDateData.substring(3,5));
        todayActual[0] = timeConverted;
        todayActual[1] = dayConverted+","+monthConverted+","+dateConverted;
    }

    private void processConvertedCalForHome(){
        checkCurrentTimeStatus();
        formatTime12();

        homeCalTimeH = rawTimeData.substring(0, 2);
        homeCalTimeM = rawTimeData.substring(3, 5);

        homeExtraDetails = rawDayData.substring(0,3);
        homeExtraDetails += "\n"+ convertMonthOri(rawDateData.substring(3, 5));
        homeExtraDetails += ", "+ rawDateData.substring(0,2);
        homeExtraDetails += "\n";

    }

    public String getAmpm(){
        String timeParsed = rawTimeData.substring(0,5);
        int timeInHour = Integer.parseInt(rawTimeData.substring(0,2));
        if(timeInHour>12){
            ampm = "PM";
        }else ampm = "AM";
        return ampm;
    }

    private String convertMonthOri(String val){
        String returnVal = "";
        if(val.equals("01")){

        }else if(val.equals("01")){
            returnVal = "Jan";
        }else if(val.equals("02")){
            returnVal = "Feb";
        }else if(val.equals("03")){
            returnVal = "Mar";
        }else if(val.equals("04")){
            returnVal = "Apr";
        }else if(val.equals("05")){
            returnVal = "May";
        }else if(val.equals("06")){
            returnVal = "Jun";
        }else if(val.equals("07")){
            returnVal = "Jul";
        }else if(val.equals("08")){
            returnVal = "Aug";
        }else if(val.equals("09")){
            returnVal = "Sep";
        }else if(val.equals("10")){
            returnVal = "Oct";
        }else if(val.equals("11")){
            returnVal = "Nov";
        }else if(val.equals("12")){
            returnVal = "Dec";
        }

        return  returnVal;
    }

    public String returnHomeCalTimeH(){

        return homeCalTimeH;
    }

    public String returnHomeCalM(){
        processConvertedCalForHome();
        return  homeCalTimeM;
    }

    public String returnHomeExtra(){
        return homeExtraDetails;
    }

    public String returnConvertedTime(){
        returnConvertedCal();
        return todayActual[0];
    }

    public String retunConvertedDate(){
        returnConvertedCal();
        return todayActual[1];
    }

    public String[] returnActualCal(){

        return todayActual;
    }

    private String convertDay(String t_day){
        t_day = t_day.toLowerCase();
        for(int i=0;i<7;i++){
            if(t_day.contains(days_in_english[i])){
                t_day = days_in_bangla[i];
                break;
            }
        }
        return t_day;
    }

    private String convertMonth(String val){
        String returnVal = "";
        if(val.equals("01")){

        }else if(val.equals("01")){
            returnVal = month_in_bangla [0];
        }else if(val.equals("02")){
            returnVal = month_in_bangla [1];
        }else if(val.equals("03")){
            returnVal = month_in_bangla [2];
        }else if(val.equals("04")){
            returnVal = month_in_bangla [3];
        }else if(val.equals("05")){
            returnVal = month_in_bangla [4];
        }else if(val.equals("06")){
            returnVal = month_in_bangla [5];
        }else if(val.equals("07")){
            returnVal = month_in_bangla [6];
        }else if(val.equals("08")){
            returnVal = month_in_bangla [7];
        }else if(val.equals("09")){
            returnVal = month_in_bangla [8];
        }else if(val.equals("10")){
            returnVal = month_in_bangla [9];
        }else if(val.equals("11")){
            returnVal = month_in_bangla [10];
        }else if(val.equals("12")){
            returnVal = month_in_bangla [11];
        }

        return  returnVal;
    }

    private String  convertNumberToBangla(String val){
        String tempVal,returnVal = "";
        tempVal = val;
        for(int i=0; i<tempVal.length();i++){
            if((tempVal.charAt(i)==':')||(tempVal.charAt(i)=='/')){
                returnVal = returnVal+":";
            }else{
                int convertedInt = Integer.parseInt("" + tempVal.charAt(i));
                switch (convertedInt){
                    case 0:
                        returnVal = returnVal+bangla_nmber[0];
                        break;
                    case 1:
                        returnVal = returnVal+bangla_nmber[1];
                        break;
                    case 2:
                        returnVal = returnVal+bangla_nmber[2];
                        break;
                    case 3:
                        returnVal = returnVal+bangla_nmber[3];
                        break;
                    case 4:
                        returnVal = returnVal+bangla_nmber[4];
                        break;
                    case 5:
                        returnVal = returnVal+bangla_nmber[5];
                        break;
                    case 6:
                        returnVal = returnVal+bangla_nmber[6];
                        break;
                    case 7:
                        returnVal = returnVal+bangla_nmber[7];
                        break;
                    case 8:
                        returnVal = returnVal+bangla_nmber[8];
                        break;

                    case 9:
                        returnVal = returnVal+bangla_nmber[9];
                        break;
                }
            }
        }

        return returnVal;
    }

    private void formatTime12(){
        String timeParsed = rawTimeData.substring(0,5);
        int timeInHour = Integer.parseInt(rawTimeData.substring(0,2));
        if(timeInHour == 00){
            timeParsed = "12";
            ampm = "AM";
        }else if(timeInHour>12){
            timeInHour = timeInHour-12;
            if(10-timeInHour>0){
                timeParsed = "0"+ timeInHour;
            }else{
                timeParsed = ""+ timeInHour;
            }
            ampm = "PM";
        }
        String tempVal = timeParsed + rawTimeData.substring(2);
        rawTimeData = tempVal;
    }

    private void checkCurrentTimeStatus(){
        rawDateData = new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());
        rawTimeData = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
        rawDayData = new SimpleDateFormat("EEEE", Locale.US).format(Calendar.getInstance().getTime());
        rawTimeData = rawTimeData.substring(0,5);
    }

}
