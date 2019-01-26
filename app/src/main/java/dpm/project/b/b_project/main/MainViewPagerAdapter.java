package dpm.project.b.b_project.main;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.Group;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.PagerAdapter;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import dpm.project.b.b_project.R;
import dpm.project.b.b_project.api.ApiManager;
import dpm.project.b.b_project.api.BLifeApi;
import dpm.project.b.b_project.api.ItemData;
import dpm.project.b.b_project.util.Utils;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater mInflater;
    private TypedArray layouts;
    private Utils utils;
    private String[] timeData;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

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
        TextView buyText = v.findViewById(R.id.main_buy_main);
        TickerView payText = v.findViewById(R.id.main_pay);
        payText.setCharacterLists(TickerUtils.provideNumberList());
        payText.setTypeface(ResourcesCompat.getFont(context, R.font.anton));
        String timeStr = String.format(context.getResources().getStringArray(R.array.main_text)[position], timeData[position]);
        timeText.setText(Html.fromHtml(timeStr));
        Group buyGroup = v.findViewById(R.id.main_buy_group);
        Disposable intervalObservable = Observable.interval(1, TimeUnit.SECONDS, AndroidSchedulers.mainThread()).subscribe(a -> {
            switch (position) {
                case 0:
                    payText.setText(utils.getDayPay());
                    if (!utils.isOffWork())
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
            payText.invalidate();
        });

        int pay = 0;
        switch (position) {
            case 0:
                pay = Integer.parseInt(utils.getDayPay().replace(",", ""));
                break;
            case 1:
                pay = Integer.parseInt(utils.getMonthlyPay().replace(",", ""));
                break;
            case 2:
                pay = Integer.parseInt(utils.getYearlyPay().replace(",", ""));
                break;
        }
        ApiManager.client().create(BLifeApi.class).getItem(pay, 30).enqueue(new Callback<List<ItemData>>() {
            @Override
            public void onResponse(@NonNull Call<List<ItemData>> call, @NonNull Response<List<ItemData>> response) {
                if (response.body() != null) {
                    setBuyAnimate(response.body(), 0, buyText);
                    buyGroup.setVisibility(View.VISIBLE);
                    buyText.setOnClickListener(view -> {
                        for (ItemData itemData : response.body()) {
                            if (buyText.getText() == itemData.name) {
                                if (itemData.url != null && !itemData.url.isEmpty()) {
                                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(itemData.url)));
                                    break;
                                }
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ItemData>> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });


        compositeDisposable.add(intervalObservable);
        container.addView(v);
        return new Item(v, intervalObservable);
    }

    void dispose() {
        compositeDisposable.dispose();
    }

    private void setBuyAnimate(List<ItemData> items, int listPosition, TextView buyText) {
        buyText.animate().translationY((float) -buyText.getHeight()).alpha(0f).setDuration(1000)
                .setStartDelay((listPosition == 0) ? 0 : 2000).setInterpolator(new AccelerateDecelerateInterpolator()).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                int newPosition = listPosition + 1;
                if (items.size() <= newPosition) newPosition = 0;
                buyText.setText(items.get(listPosition).name);
                buyText.setTranslationY((float) buyText.getHeight());
                int finalNewPosition = newPosition;
                buyText.animate().setStartDelay(300).translationY(0f).alpha(1f).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        setBuyAnimate(items, finalNewPosition, buyText);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {
                    }
                });
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        if (object instanceof Item) {
            final Item item = (Item) object;
            container.removeView(item.view);
            item.dispose();
        }
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == ((Item) o).view;
    }

    private static final class Item implements Disposable {

        View view;
        Disposable actual;

        Item(View v, Disposable actual) {
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
