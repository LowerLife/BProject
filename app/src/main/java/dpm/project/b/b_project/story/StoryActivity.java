package dpm.project.b.b_project.story;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import dpm.project.b.b_project.R;
import dpm.project.b.b_project.base.BaseActivity;

public class StoryActivity extends BaseActivity {

    @BindView(R.id.st_progress)
    ProgressBar stProgress;
    @BindView(R.id.st_viewpager)
    StoryViewPager stViewpager;
    @BindView(R.id.st_bottom_btn)
    TextView stBottomBtn;
    int pageSelect = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);
        ButterKnife.bind(this);
        final StoryPagerAdapter storyPagerAdapter = new StoryPagerAdapter(this);
        stBottomBtn.setText(R.string.next);
        stViewpager.setAdapter(storyPagerAdapter);
        stViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {}

            @Override
            public void onPageSelected(int i) {
                pageSelect = i;

            }

            @Override
            public void onPageScrollStateChanged(int i) {
                if(i == ViewPager.SCROLL_STATE_IDLE){
                    if(pageSelect == storyPagerAdapter.getCount()-1){
                        stBottomBtn.setText(R.string.start);
                    }
                    stProgress.setProgress(Math.round((stProgress.getMax() / (float)(storyPagerAdapter.getCount())) * (pageSelect+1)));
                }
            }
        });
        stBottomBtn.setOnClickListener(view -> {
            if(storyPagerAdapter.getCount() == stViewpager.getCurrentItem()){

            }else {
                stViewpager.setCurrentItem(stViewpager.getCurrentItem() + 1);
            }
        });
    }
}
