package dpm.project.b.b_project.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import okhttp3.internal.Util;

public class Utils {

    Context context;
    SharedPreferences sharedPreferences;
    public Utils(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences("bproject",Context.MODE_PRIVATE);
    }

    public boolean isOffWork(){
        String workTime = sharedPreferences.getString("worktime","1800");
        SimpleDateFormat workTimeParse = new SimpleDateFormat("HHmm",Locale.KOREA);
        try {
            long cur = workTimeParse.parse(workTimeParse.format(new Date())).getTime();
            long req = workTimeParse.parse(workTime).getTime();
            return cur > req;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getWorkTime(){
        String workTime = sharedPreferences.getString("worktime","1800");
        SimpleDateFormat workTimeFormat = new SimpleDateFormat("H시간mm분",Locale.KOREA);
        SimpleDateFormat workTimeParse = new SimpleDateFormat("HHmm",Locale.KOREA);
        workTimeFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
            long cur = new Date().getTime();
            long req = workTimeParse.parse(workTime).getTime();
            return workTimeFormat.format(req - cur);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
