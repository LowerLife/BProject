package dpm.project.b.b_project.story;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.Group;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import dpm.project.b.b_project.R;
import dpm.project.b.b_project.base.BaseActivity;
import dpm.project.b.b_project.input.View.InputInfoActivity;

public class StoryActivity extends BaseActivity {

    @BindView(R.id.st_progress)
    ProgressBar stProgress;
    @BindView(R.id.st_viewpager)
    StoryViewPager stViewpager;
    @BindView(R.id.st_bottom_btn)
    TextView stBottomBtn;
    int pageSelect = 0;
    @BindView(R.id.story_bottom_group)
    Group storyBottomGroup;


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
            public void onPageScrolled(int i, float v, int i1) {
                int aa = Math.round((stProgress.getMax() / (float) (storyPagerAdapter.getCount())) * (i + 1));
                stProgress.setProgress(Math.round((v * 34 + aa)));
            }

            @Override
            public void onPageSelected(int i) {
                pageSelect = i;
            }



            @Override
            public void onPageScrollStateChanged(int i) {
                if (i == ViewPager.SCROLL_STATE_IDLE) {
                    storyBottomGroup.setVisibility(View.INVISIBLE);
                    if (pageSelect == storyPagerAdapter.getCount() - 1) {
                        storyBottomGroup.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        stBottomBtn.setOnClickListener(view -> {
            if (storyPagerAdapter.getCount() == stViewpager.getCurrentItem() + 1) {
                startActivity(new Intent(this, InputInfoActivity.class));
            } else {
                stViewpager.setCurrentItem(stViewpager.getCurrentItem() + 1);
            }
        });
    }
}
