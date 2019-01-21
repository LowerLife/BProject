package dpm.project.b.b_project.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import okhttp3.internal.Util;

import static dpm.project.b.b_project.util.Const.*;

public class Utils {

    Context context;
    public static SharedPreferences sharedPreferences;
    SimpleDateFormat workTimeParse;
    Calendar calendar;
    String workTimeStartAt;
    String workTimeEndAt;

    public Utils(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences("bproject",Context.MODE_PRIVATE);
        workTimeParse = new SimpleDateFormat("HHmmss",Locale.KOREA);
        String[] workTime = Objects.requireNonNull(sharedPreferences.getString(WORK_START_AND_END_AT, "09:30 / 06:30")).replace(":","").split("/");
        workTimeStartAt = workTime[0].trim();
        workTimeEndAt = workTime[1].trim();
        calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        calendar.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
    }

    public boolean isOffWork(){
        SimpleDateFormat workTimeParse = new SimpleDateFormat("HHmm",Locale.KOREA);
        try {
            long cur = workTimeParse.parse(workTimeParse.format(calendar.getTime())).getTime();
            long req = workTimeParse.parse(workTimeEndAt).getTime();
            return cur > req;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getWorkTime(){
        SimpleDateFormat workTimeFormat = new SimpleDateFormat("H시간mm분",Locale.KOREA);
        workTimeParse = new SimpleDateFormat("HHmm",Locale.KOREA);
        workTimeFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
            long cur = calendar.getTime().getTime();
            long req = workTimeParse.parse(workTimeEndAt).getTime();
            return workTimeFormat.format(req - cur);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getDayPay(){
        calendar.setTime(new Date());
        int monthlyPay = sharedPreferences.getInt(MONTHLY_PAY,2000000);
        long payInt = monthlyPay / calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        DecimalFormat myFormatter = new DecimalFormat("###,###");
        if(isOffWork()) return myFormatter.format(payInt);
        try {
            String h = workTimeEndAt.substring(0,2);
            String m = workTimeEndAt.substring(2,4);
            Calendar workEndTimeCalendar = new GregorianCalendar();
            workEndTimeCalendar.setTime(new Date());
            workEndTimeCalendar.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
            workEndTimeCalendar.set(Calendar.HOUR_OF_DAY,Integer.parseInt(h));
            workEndTimeCalendar.set(Calendar.MINUTE,Integer.parseInt(m));
            workEndTimeCalendar.set(Calendar.SECOND,0);
            long cur = calendar.getTime().getTime() - workEndTimeCalendar.getTime().getTime();
            long req = workTimeParse.parse(workTimeEndAt).getTime();
            payInt = (long)(payInt * ((float)cur / req * 100) / 100);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return myFormatter.format(payInt);
    }

    public boolean isWeekend(){
        switch (calendar.get(Calendar.DAY_OF_WEEK)){
            case 1:
                return true;
            case 7:
                return true;
            default:
                return false;
        }
    }

//    public String getDday(){
//
//    }

}
