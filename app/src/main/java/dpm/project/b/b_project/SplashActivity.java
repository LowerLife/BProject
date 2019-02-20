package dpm.project.b.b_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.iid.FirebaseInstanceId;

import butterknife.BindView;
import butterknife.ButterKnife;
import dpm.project.b.b_project.base.BaseActivity;
import dpm.project.b.b_project.main.MainActivity;
import dpm.project.b.b_project.story.StoryActivity;
import dpm.project.b.b_project.util.Const;
import dpm.project.b.b_project.util.Log;

public class SplashActivity extends BaseActivity {

    @BindView(R.id.splash_image)
    AppCompatImageView splashImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> Log.e(task.getResult().getToken()));

        Glide.with(this).asGif().load(R.drawable.blife).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE)).listener(new RequestListener<GifDrawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                resource.setLoopCount(1);
                new Thread(() -> {
                    while(true) {
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if(!resource.isRunning()) {
                            if(isFirstInfo(Const.ENTER_DATE) && isFirstInfo(Const.MONTHLY_PAY) && isFirstInfo(Const.SALARY_DAY) && isFirstInfo(Const.WORK_START_AND_END_AT)){
                                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                                finish();
                            }else{
                                startActivity(new Intent(SplashActivity.this, StoryActivity.class));
                                finish();
                            }
                            break;
                        }
                    }
                }).start();
                return false;
            }
        }).into(splashImage);
    }
    private boolean isFirstInfo(String key){
        return sharedPreferences.contains(key);
    }
}
