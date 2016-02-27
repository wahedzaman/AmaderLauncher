package com.durbinsoft.amarlauncher;

/**
 * Created by Md.Wahuduzzaman on 2/28/2016.
 */
public class CalenderConversion {

    private String [] days_in_bangla = {"শনিবার","রবিবার", "সোমবার","মঙ্গলবার", "বুধবার", "বৃহস্পতিবার","শুক্রবার"};
    private String [] days_in_english = {"saturday","sunday","monday","tuesday","wednesday","thursday"};

    private String todayActual;
    private String todayConverted;

    private String time;
    private String timeConverted;

    private String date;
    private String dateConverted;



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


}
