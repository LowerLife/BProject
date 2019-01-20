package dpm.project.b.b_project.main;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import dpm.project.b.b_project.R;
import dpm.project.b.b_project.util.Log;
import dpm.project.b.b_project.util.Utils;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainViewPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater mInflater;
    private TypedArray layouts;
    private Utils utils;

    MainViewPagerAdapter(Context context){
        this.context = context;
        mInflater = LayoutInflater.from(context);
        layouts = context.getResources().obtainTypedArray(R.array.main_layout);
        utils = new Utils(context);
    }

    @Override
    public int getCount() {
        return layouts.length();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View v = mInflater.inflate(layouts.getResourceId(position,0),container,false);
        TextView timeText = v.findViewById(R.id.main_time);
        TextView payText = v.findViewById(R.id.main_pay);
        String timeStr = String.format(context.getResources().getStringArray(R.array.main_text)[position],utils.getWorkTime());
        timeText.setText(Html.fromHtml(timeStr));
        Observable.interval(1,TimeUnit.SECONDS,AndroidSchedulers.mainThread()).subscribe(a -> payText.setText(utils.getDayPay()));
        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }
}