package dpm.project.b.b_project.main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dpm.project.b.b_project.R;
import dpm.project.b.b_project.base.BaseActivity;
import dpm.project.b.b_project.setting.MyPageActivity;
import me.kaelaela.verticalviewpager.VerticalViewPager;

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
    }

    @Override
    protected void onResume() {
        super.onResume();
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
//        Toast.makeText(this, "마이페이지는 개발 중입니다 ㅠㅠ", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, MyPageActivity.class));
    }

    @Override
    protected void onDestroy() {
        mainViewPagerAdapter.dispose();
        super.onDestroy();
    }
}
