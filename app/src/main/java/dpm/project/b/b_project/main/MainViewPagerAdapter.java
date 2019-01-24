package dpm.project.b.b_project.main;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.PagerAdapter;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import dpm.project.b.b_project.R;
import dpm.project.b.b_project.util.Log;
import dpm.project.b.b_project.util.Utils;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainViewPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater mInflater;
    private TypedArray layouts;
    private Utils utils;
    private String[] timeData;
    private Disposable intervalObservable;
    private ArrayList<Item> items = new ArrayList<>();
    MainViewPagerAdapter(Context context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        layouts = context.getResources().obtainTypedArray(R.array.main_layout);
        utils = new Utils(context);
        timeData = new String[]{utils.getWorkTime(), utils.getMonthlyDay() + "일", utils.getYearlyDay() + "일"};
    }

    @Override
    public int getCount() {
        return layouts.length();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View v = mInflater.inflate(layouts.getResourceId(position, 0), container, false);
        TextView timeText = v.findViewById(R.id.main_time);
        TickerView payText = v.findViewById(R.id.main_pay);
        payText.setCharacterLists(TickerUtils.provideNumberList());
        payText.setTypeface(ResourcesCompat.getFont(context,R.font.anton));
        String timeStr = String.format(context.getResources().getStringArray(R.array.main_text)[position], timeData[position]);
        timeText.setText(Html.fromHtml(timeStr));
        intervalObservable = Observable.interval(1, TimeUnit.SECONDS, AndroidSchedulers.mainThread()).subscribe(a -> {
            switch (position) {
                case 0:
                    payText.setText(utils.getDayPay());
                    if(!utils.isOffWork())
                        timeText.setText(Html.fromHtml(String.format(context.getResources().getStringArray(R.array.main_text)[position], utils.getWorkTime())));
                    else timeText.setText(R.string.main_text_offwork);
                    break;
                case 1:
                    payText.setText(utils.getMonthlyPay());
                    break;
                case 2:
                    payText.setText(utils.getYearlyPay());
                    break;
            }
        });
        items.add(new Item(v,intervalObservable));
        container.addView(v);
        return v;
    }

    public void dispose(){
        for (Item item : items){
            item.actual.dispose();
        }
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        for (Item item : items){
            if(item.view == object){
                item.actual.dispose();
                items.remove(item);
                break;
            }
        }
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    private static final class Item implements Disposable{

        View view;
        Disposable actual;

        Item(View v,Disposable actual){
            this.view = v;
            this.actual = actual;
        }

        @Override
        public void dispose() {
            actual.dispose();
        }

        @Override
        public boolean isDisposed() {
            return actual.isDisposed();
        }
    }

}
