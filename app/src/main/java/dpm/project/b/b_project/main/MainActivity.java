package dpm.project.b.b_project.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dpm.project.b.b_project.R;
import dpm.project.b.b_project.base.BaseActivity;
import dpm.project.b.b_project.setting.MyPageActivity;
import dpm.project.b.b_project.util.Log;
import me.kaelaela.verticalviewpager.VerticalViewPager;
import me.kaelaela.verticalviewpager.transforms.DefaultTransformer;
import me.kaelaela.verticalviewpager.transforms.StackTransformer;

public class MainActivity extends BaseActivity {

    @BindView(R.id.main_viewpager)
    VerticalViewPager mainViewpager;
    @BindView(R.id.setting_menu_btn)
    ImageView settingMenu;

    MainViewPagerAdapter mainViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Log.e(utils.getDayPay() + "///" + utils.getWorkTime() + "///" + utils.getYearlyDay() + "///" + utils.getMonthlyDay());
        mainViewPagerAdapter = new MainViewPagerAdapter(this);
        mainViewpager.setAdapter(mainViewPagerAdapter);
        mainViewpager.setPageTransformer(true, (view, position) -> {
            view.setTranslationX(view.getWidth() * -position);
            float yPosition = position * view.getHeight();
            view.setTranslationY(yPosition);
        });
    }

    @OnClick(R.id.setting_menu_btn)
    public void onClickSettingMenu(){
        startActivity(new Intent(this, MyPageActivity.class));
    }

    @Override
    protected void onDestroy() {
        mainViewPagerAdapter.dispose();
        super.onDestroy();
    }
}
