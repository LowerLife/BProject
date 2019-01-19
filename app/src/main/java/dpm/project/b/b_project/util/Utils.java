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
import java.util.TimeZone;

import okhttp3.internal.Util;

import static dpm.project.b.b_project.util.Const.*;

public class Utils {

    Context context;
    SharedPreferences sharedPreferences;
    SimpleDateFormat workTimeParse;
    Calendar calendar;
    String workTime;

    public Utils(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences("bproject",Context.MODE_PRIVATE);
        workTimeParse = new SimpleDateFormat("HHmmss",Locale.KOREA);
        workTime = sharedPreferences.getString(WORK_TIME,"1800");
        calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        calendar.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
    }

    public boolean isOffWork(){
        String workTime = sharedPreferences.getString(WORK_TIME,"1800");
        SimpleDateFormat workTimeParse = new SimpleDateFormat("HHmm",Locale.KOREA);
        try {
            long cur = workTimeParse.parse(workTimeParse.format(calendar.getTime())).getTime();
            long req = workTimeParse.parse(workTime).getTime();
            return cur > req;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getWorkTime(){
        workTime = sharedPreferences.getString(WORK_TIME,"1800");
        SimpleDateFormat workTimeFormat = new SimpleDateFormat("H시간mm분",Locale.KOREA);
        workTimeParse = new SimpleDateFormat("HHmm",Locale.KOREA);
        workTimeFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
            long cur = calendar.getTime().getTime();
            long req = workTimeParse.parse(workTime).getTime();
            return workTimeFormat.format(req - cur);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getDayPay(){
        calendar.setTime(new Date());
        int monthlyPay = sharedPreferences.getInt(WORK_TIME,2000000);
        long payInt = monthlyPay / calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        DecimalFormat myFormatter = new DecimalFormat("###,###");
        try {
            long cur = workTimeParse.parse(calendar.get(Calendar.HOUR)+""+calendar.get(Calendar.MINUTE)+""+ calendar.get(Calendar.SECOND)).getTime();
            long req = workTimeParse.parse(workTime).getTime();
            payInt = (long)(payInt * ((float)cur / req * 100) / 100);
            Log.e(payInt+"");
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
