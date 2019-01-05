package dpm.project.b.b_project.story;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

import java.lang.reflect.Field;

public class StoryViewPager extends ViewPager {

    private boolean enabled;

    public StoryViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.enabled = true;
//        try {
////            Class<?> viewpager = ViewPager.class;
////            Field scroller = viewpager.getDeclaredField("mScroller");
////            scroller.setAccessible(true);
////            scroller.set(this, new OwnScroller(getContext()));
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.enabled) {
            return super.onTouchEvent(event);
        }

        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.enabled) {
            return super.onInterceptTouchEvent(event);
        }

        return false;
    }

    public void setPagingEnabled(boolean enabled) {
        this.enabled = enabled;
    }


    public class OwnScroller extends Scroller {

        private int durationScrollMillis = 1000;

        public OwnScroller(Context context) {
            super(context, new DecelerateInterpolator());
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, durationScrollMillis);
        }
    }
}