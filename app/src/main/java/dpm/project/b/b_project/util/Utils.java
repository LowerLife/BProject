package dpm.project.b.b_project.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import static dpm.project.b.b_project.util.Const.MONTHLY_PAY;
import static dpm.project.b.b_project.util.Const.SALARY_DAY;
import static dpm.project.b.b_project.util.Const.WORK_START_AND_END_AT;

public class Utils {

    public static SharedPreferences sharedPreferences;

    private SimpleDateFormat workTimeParse;
    private Calendar calendar;
    private String workTimeStartAt;
    private String workTimeEndAt;
    private String enterDate;
    private int monthlyPay;

    public Utils(Context context) {
        sharedPreferences = context.getSharedPreferences("bproject", Context.MODE_PRIVATE);
        workTimeParse = new SimpleDateFormat("HHmmss", Locale.KOREA);
        monthlyPay = sharedPreferences.getInt(MONTHLY_PAY, 2000000);
        String[] workTime = Objects.requireNonNull(sharedPreferences.getString(WORK_START_AND_END_AT, "09:30 / 18:30")).replace(":", "").split("/");
        enterDate = sharedPreferences.getString(Const.ENTER_DATE, "0809");
        workTimeStartAt = workTime[0].trim();
        workTimeEndAt = workTime[1].trim();
        calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        calendar.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
    }

    public boolean isOffWork() {
        Calendar nowCalendar = new GregorianCalendar();
        nowCalendar.setTime(new Date());
        nowCalendar.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        int startTime = Integer.parseInt(workTimeStartAt);
        int endTime = Integer.parseInt(workTimeEndAt);
        String min = (nowCalendar.get(Calendar.MINUTE) >= 10) ? nowCalendar.get(Calendar.MINUTE) + "" : "0" + nowCalendar.get(Calendar.MINUTE);
        int nowTime = Integer.parseInt(nowCalendar.get(Calendar.HOUR_OF_DAY) + min);
        if (nowTime > startTime && nowTime < endTime) {
            return false;
        }
        return true;
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
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        long dailyPay = monthlyPay / daysInMonth;
        if (isOffWork()) return payFormat(dailyPay);
        Calendar now = Calendar.getInstance();
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        start.set(Calendar.HOUR_OF_DAY, Integer.parseInt(workTimeStartAt.substring(0, 2)));
        start.set(Calendar.MINUTE, Integer.parseInt(workTimeStartAt.substring(2, 4)));
        start.set(Calendar.SECOND, 0);
        end.set(Calendar.HOUR_OF_DAY, Integer.parseInt(workTimeEndAt.substring(0, 2)));
        end.set(Calendar.MINUTE, Integer.parseInt(workTimeEndAt.substring(2, 4)));
        end.set(Calendar.SECOND, 0);
        long paied = (long) ((float) dailyPay * calculateRatio(now, start, end));
        return payFormat(paied);
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
        if (nowDay >= salaryDay) {
            int monthMaxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            return (monthMaxDay - nowDay) + salaryDay;
        } else {
            return salaryDay - nowDay;
        }
    }

    public String getMonthlyPay() {
        int salaryDay = Integer.parseInt(Objects.requireNonNull(sharedPreferences.getString(SALARY_DAY, "25")));

        Calendar now = Calendar.getInstance();
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();

        if (salaryDay >= now.get(Calendar.DAY_OF_MONTH))
            end.add(Calendar.MONTH, +1);
        else start.add(Calendar.MONTH, -1);

        start.set(Calendar.DAY_OF_MONTH, salaryDay);
        start.set(Calendar.HOUR_OF_DAY, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        end.set(Calendar.DAY_OF_MONTH, salaryDay);
        end.set(Calendar.HOUR_OF_DAY, 0);
        end.set(Calendar.MINUTE, 0);
        end.set(Calendar.SECOND, 0);

        return payFormat((long) (monthlyPay * calculateRatio(now, start, end)));
    }

    public long getYearlyDay() {
        int nowMonth = calendar.get(Calendar.MONTH);
        int nowMday = calendar.get(Calendar.DAY_OF_MONTH);
        int enterMonth = Integer.parseInt(enterDate.substring(0, 2));
        int enterDay = Integer.parseInt(enterDate.substring(2, 4));
        Calendar yearlyCalendar = new GregorianCalendar();
        yearlyCalendar.setTime(new Date());
        yearlyCalendar.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        yearlyCalendar.set(Calendar.MONTH, enterMonth - 1);
        yearlyCalendar.set(Calendar.DAY_OF_MONTH, enterDay);
        if (Integer.parseInt(Objects.requireNonNull(enterDate)) < Integer.parseInt(nowMonth + "" + nowMday)) {
            yearlyCalendar.set(Calendar.YEAR, yearlyCalendar.get(Calendar.YEAR) + 1);
        }
        long elapsedTimeMillis = yearlyCalendar.getTimeInMillis() - calendar.getTimeInMillis();
        return TimeUnit.MILLISECONDS.toDays(elapsedTimeMillis);
    }

    public String getYearlyPay() {
        int nowMonth = calendar.get(Calendar.MONTH);
        int nowMday = calendar.get(Calendar.DAY_OF_MONTH);
        int enterMonth = Integer.parseInt(enterDate.substring(0, 2));
        int enterDay = Integer.parseInt(enterDate.substring(2, 4));
        Calendar now = Calendar.getInstance();
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();

        if (Integer.parseInt(Objects.requireNonNull(enterDate)) < Integer.parseInt(nowMonth + "" + nowMday))
            end.add(Calendar.YEAR, +1);
        else start.add(Calendar.YEAR, -1);
        start.set(Calendar.MONTH, enterMonth - 1);
        start.set(Calendar.DAY_OF_MONTH, enterDay);
        start.set(Calendar.HOUR_OF_DAY, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        end.set(Calendar.MONTH, enterMonth - 1);
        end.set(Calendar.DAY_OF_MONTH, enterDay);
        end.set(Calendar.HOUR_OF_DAY, 0);
        end.set(Calendar.MINUTE, 0);
        end.set(Calendar.SECOND, 0);

        return payFormat((long) ((monthlyPay * 12) * calculateRatio(now, start, end)));
    }

    private String payFormat(long pay) {
        return new DecimalFormat("###,###").format(pay);
    }

    private float calculateRatio(Calendar n, Calendar s, Calendar e) {
        long a = n.getTimeInMillis() - s.getTimeInMillis();
        long b = e.getTimeInMillis() - s.getTimeInMillis();
        return (float) a / (float) b;
    }
}
