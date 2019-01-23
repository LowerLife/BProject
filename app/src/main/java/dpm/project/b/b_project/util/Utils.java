package dpm.project.b.b_project.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import okhttp3.internal.Util;

import static dpm.project.b.b_project.util.Const.*;

public class Utils {

    public static SharedPreferences sharedPreferences;

    private Context context;
    private SimpleDateFormat workTimeParse;
    private Calendar calendar;
    private String workTimeStartAt;
    private String workTimeEndAt;
    private String enterDate;
    private int monthlyPay;
    private float secondPay;

    public Utils(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("bproject", Context.MODE_PRIVATE);
        workTimeParse = new SimpleDateFormat("HHmmss", Locale.KOREA);
        monthlyPay = sharedPreferences.getInt(MONTHLY_PAY, 2000000);
        String[] workTime = Objects.requireNonNull(sharedPreferences.getString(WORK_START_AND_END_AT, "09:30 / 18:30")).replace(":", "").split("/");
        enterDate = sharedPreferences.getString(Const.ENTER_DATE,"0809");
        workTimeStartAt = workTime[0].trim();
        workTimeEndAt = workTime[1].trim();
        calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        calendar.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        secondPay = ((float)monthlyPay / calendar.getActualMaximum(Calendar.DAY_OF_MONTH) / 24 / 3600);
    }

    public boolean isOffWork() {
        SimpleDateFormat workTimeParse = new SimpleDateFormat("HHmm", Locale.KOREA);
        try {
            long cur = workTimeParse.parse(workTimeParse.format(calendar.getTime())).getTime();
            long req = workTimeParse.parse(workTimeEndAt).getTime();
            return cur > req;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getWorkTime() {
        SimpleDateFormat workTimeFormat = new SimpleDateFormat("H시간mm분", Locale.KOREA);
        workTimeParse = new SimpleDateFormat("HHmm", Locale.KOREA);
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

    public String getDayPay() {
        long payInt = monthlyPay / calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        if (isOffWork()) return payFormat(payInt);
        try {
            String h = workTimeStartAt.substring(0, 2);
            String m = workTimeStartAt.substring(2, 4);
            Calendar workStartTimeCalendar = new GregorianCalendar();
            workStartTimeCalendar.setTime(new Date());
            workStartTimeCalendar.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
            workStartTimeCalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(h));
            workStartTimeCalendar.set(Calendar.MINUTE, Integer.parseInt(m));
            workStartTimeCalendar.set(Calendar.SECOND, 0);
            long cur = new Date().getTime() - workStartTimeCalendar.getTime().getTime();
            long req = workTimeParse.parse(workTimeEndAt).getTime();
            payInt = (long) (payInt * ((float) cur / req * 100) / 100);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return payFormat(payInt);
    }

    public boolean isWeekend() {
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case 1:
                return true;
            case 7:
                return true;
            default:
                return false;
        }
    }

    public int getMonthlyDay() {
        int salaryDay = Integer.parseInt(Objects.requireNonNull(sharedPreferences.getString(SALARY_DAY, "25")));
        int nowDay = calendar.get(Calendar.DAY_OF_MONTH);
        if (nowDay > salaryDay) {
            int monthMaxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            return (monthMaxDay - nowDay) + salaryDay;
        } else {
            return salaryDay - nowDay;
        }
    }

    public String getMonthlyPay(){
        int salaryDay = Integer.parseInt(Objects.requireNonNull(sharedPreferences.getString(SALARY_DAY, "25")));
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        calendar.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        Calendar monthlyCalendar = new GregorianCalendar();
        monthlyCalendar.setTime(new Date());
        monthlyCalendar.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        if(salaryDay > calendar.get(Calendar.DAY_OF_MONTH))
            monthlyCalendar.set(Calendar.MONTH,monthlyCalendar.get(Calendar.MONTH)-1);
        else monthlyCalendar.set(Calendar.MONTH,monthlyCalendar.get(Calendar.MONTH));
        monthlyCalendar.set(Calendar.DAY_OF_MONTH,salaryDay);
        monthlyCalendar.set(Calendar.HOUR_OF_DAY,0);
        monthlyCalendar.set(Calendar.MINUTE,0);
        monthlyCalendar.set(Calendar.SECOND,0);
        long elapsedTimeMillis = calendar.getTimeInMillis() - monthlyCalendar.getTimeInMillis();
        return payFormat((long)(TimeUnit.MILLISECONDS.toSeconds(elapsedTimeMillis) * secondPay));
    }

    public long getYearlyDay(){
        int nowMonth = calendar.get(Calendar.MONTH);
        int nowMday = calendar.get(Calendar.DAY_OF_MONTH);
        int enterMonth = Integer.parseInt(enterDate.substring(0,2));
        int enterDay = Integer.parseInt(enterDate.substring(2,4));
        Calendar yearlyCalendar = new GregorianCalendar();
        yearlyCalendar.setTime(new Date());
        yearlyCalendar.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        yearlyCalendar.set(Calendar.MONTH,enterMonth-1);
        yearlyCalendar.set(Calendar.DAY_OF_MONTH,enterDay);
        if(Integer.parseInt(Objects.requireNonNull(enterDate)) < Integer.parseInt(nowMonth+""+nowMday)){
            yearlyCalendar.set(Calendar.YEAR,yearlyCalendar.get(Calendar.YEAR)+1);
        }
        long elapsedTimeMillis = yearlyCalendar.getTimeInMillis() - calendar.getTimeInMillis();
        return TimeUnit.MILLISECONDS.toDays(elapsedTimeMillis);
    }

    public String getYearlyPay(){
        int nowMonth = calendar.get(Calendar.MONTH);
        int nowMday = calendar.get(Calendar.DAY_OF_MONTH);
        int enterMonth = Integer.parseInt(enterDate.substring(0,2));
        int enterDay = Integer.parseInt(enterDate.substring(2,4));
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        calendar.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        Calendar yearlyCalendar = new GregorianCalendar();
        yearlyCalendar.setTime(new Date());
        yearlyCalendar.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        yearlyCalendar.set(Calendar.MONTH,enterMonth-1);
        yearlyCalendar.set(Calendar.DAY_OF_MONTH,enterDay);
        yearlyCalendar.set(Calendar.HOUR_OF_DAY,0);
        yearlyCalendar.set(Calendar.MINUTE,0);
        yearlyCalendar.set(Calendar.SECOND,0);
        if(Integer.parseInt(Objects.requireNonNull(enterDate)) > Integer.parseInt(nowMonth+""+nowMday)){
            yearlyCalendar.set(Calendar.YEAR,yearlyCalendar.get(Calendar.YEAR)-1);
        }
        long elapsedTimeMillis = calendar.getTimeInMillis() - yearlyCalendar.getTimeInMillis();
        return payFormat((long)(TimeUnit.MILLISECONDS.toSeconds(elapsedTimeMillis) * secondPay));
    }

    private String payFormat(long pay){
        return new DecimalFormat("###,###").format(pay);
    }

//    public String getDday(){
//
//    }

}
