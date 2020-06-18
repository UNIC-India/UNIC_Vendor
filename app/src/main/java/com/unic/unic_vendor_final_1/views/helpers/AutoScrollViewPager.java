package com.unic.unic_vendor_final_1.views.helpers;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.Interpolator;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

public class AutoScrollViewPager extends ViewPager {
    public static final int DEFAULT_INTERVAL = 1500;
    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    private static final String TAG = "AutoScrollViewPager";
    public static final int SLIDE_BORDER_MODE_NONE = 0;
    public static final int SLIDE_BORDER_MODE_CYCLE = 1;
    public static final int SLIDE_BORDER_MODE_TO_PARENT = 2;
    private long interval = DEFAULT_INTERVAL;
    private int direction = RIGHT;
    private boolean isCycle = true;
    private boolean stopScrollWhenTouch = true;
    private int slideBorderMode = SLIDE_BORDER_MODE_NONE;
    private boolean isBorderAnimation = true;
    private double autoScrollFactor = 1.0;
    private double swipeScrollFactor = 1.0;
    private Handler handler;
    @Nullable
    private DurationScroller scroller;
    public static final int SCROLL_WHAT = 0;
    public AutoScrollViewPager(Context paramContext) {
        super(paramContext);
        init();
    }
    public AutoScrollViewPager(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        init();
    }
    private void init() {
        handler = new MyHandler(this);
        setViewPagerScroller();
    }
    /**
     * start auto scroll, first scroll delay time is {@link #getInterval()}.
     */
    public void startAutoScroll() {
        if (scroller != null) {
            sendScrollMessage(
                    (long) (interval + scroller.getDuration() / autoScrollFactor * swipeScrollFactor));
        }
    }
    /**
     * start auto scroll.
     *
     * @param delayTimeInMills first scroll delay time.
     */
    public void startAutoScroll(int delayTimeInMills) {
        sendScrollMessage(delayTimeInMills);
    }
    /**
     * stop auto scroll.
     */
    public void stopAutoScroll() {
        handler.removeMessages(SCROLL_WHAT);
    }
    /**
     * set the factor by which the duration of sliding animation will change while swiping.
     */
    public void setSwipeScrollDurationFactor(double scrollFactor) {
        swipeScrollFactor = scrollFactor;
    }
    /**
     * set the factor by which the duration of sliding animation will change while auto scrolling.
     */
    public void setAutoScrollDurationFactor(double scrollFactor) {
        autoScrollFactor = scrollFactor;
    }
    private void sendScrollMessage(long delayTimeInMills) {
        /** remove messages before, keeps one message is running at most **/
        handler.removeMessages(SCROLL_WHAT);
        handler.sendEmptyMessageDelayed(SCROLL_WHAT, delayTimeInMills);
    }
    /**
     * set ViewPager scroller to change animation duration when sliding.
     */
    private void setViewPagerScroller() {
        try {
            Field scrollerField = ViewPager.class.getDeclaredField("mScroller");
            scrollerField.setAccessible(true);
            Field interpolatorField = ViewPager.class.getDeclaredField("sInterpolator");
            interpolatorField.setAccessible(true);
            scroller =
                    new DurationScroller(getContext(), (Interpolator) interpolatorField.get(null));
            scrollerField.set(this, scroller);
        } catch (IllegalAccessException e) {
            Log.e(TAG, "setViewPagerScroller: ",e );
            //  Timber.e(e);
        } catch (NoSuchFieldException e) {
            Log.e(TAG, "setViewPagerScroller: ",e );
            // Timber.e(e);
        }
    }
    /**
     * scroll only once.
     */
    public void scrollOnce() {
        PagerAdapter adapter = getAdapter();
        int currentItem = getCurrentItem();
        int totalCount = adapter != null ? adapter.getCount() : -100;
        if (adapter == null || totalCount <= 1) {
            return;
        }
        int nextItem = (direction == LEFT) ? --currentItem : ++currentItem;
        if (nextItem < 0) {
            if (isCycle) {
                setCurrentItem(totalCount - 1, isBorderAnimation);
            }
        } else if (nextItem == totalCount) {
            if (isCycle) {
                setCurrentItem(0, isBorderAnimation);
            }
        } else {
            setCurrentItem(nextItem, true);
        }
    }
    private static class MyHandler extends Handler {
        private final WeakReference<AutoScrollViewPager> autoScrollViewPager;
        public MyHandler(AutoScrollViewPager autoScrollViewPager) {
            this.autoScrollViewPager = new WeakReference<AutoScrollViewPager>(autoScrollViewPager);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == SCROLL_WHAT) {
                AutoScrollViewPager pager = this.autoScrollViewPager.get();
                if (pager != null && pager.scroller != null) {
                    pager.scroller.setScrollDurationFactor(pager.autoScrollFactor);
                    pager.scrollOnce();
                    pager.scroller.setScrollDurationFactor(pager.swipeScrollFactor);
                    pager.sendScrollMessage(pager.interval + pager.scroller.getDuration());
                }
            }
        }
    }
    /**
     * get auto scroll time in milliseconds, default is {@link #DEFAULT_INTERVAL}.
     *
     * @return the interval.
     */
    public long getInterval() {
        return interval;
    }
    /**
     * set auto scroll time in milliseconds, default is {@link #DEFAULT_INTERVAL}.
     *
     * @param interval the interval to set.
     */
    public void setInterval(long interval) {
        this.interval = interval;
    }
    /**
     * get auto scroll direction.
     *
     * @return {@link #LEFT} or {@link #RIGHT}, default is {@link #RIGHT}
     */
    public int getDirection() {
        return (direction == LEFT) ? LEFT : RIGHT;
    }
    /**
     * set auto scroll direction.
     *
     * @param direction {@link #LEFT} or {@link #RIGHT}, default is {@link #RIGHT}
     */
    public void setDirection(int direction) {
        this.direction = direction;
    }
    /**
     * whether automatic cycle when auto scroll reaching the last or first item, default is true.
     *
     * @return the isCycle.
     */
    public boolean isCycleScroll() {
        return isCycle;
    }
    /**
     * set whether automatic cycle when auto scroll reaching the last or first item, default is true.
     *
     * @param isCycle the isCycle to set.
     */
    public void setCycle(boolean isCycle) {
        this.isCycle = isCycle;
    }
    /**
     * whether stop auto scroll when touching, default is true.
     *
     * @return the stopScrollWhenTouch.
     */
    public boolean isStopScrollWhenTouch() {
        return stopScrollWhenTouch;
    }
    /**
     * set whether stop auto scroll when touching, default is true.
     */
    public void setStopScrollWhenTouch(boolean stopScrollWhenTouch) {
        this.stopScrollWhenTouch = stopScrollWhenTouch;
    }
    /**
     * get how to process when sliding at the last or first item.
     *
     * @return the slideBorderMode {@link #SLIDE_BORDER_MODE_NONE},
     * {@link #SLIDE_BORDER_MODE_TO_PARENT},
     * {@link #SLIDE_BORDER_MODE_CYCLE}, default is {@link #SLIDE_BORDER_MODE_NONE}
     */
    public int getSlideBorderMode() {
        return slideBorderMode;
    }
    /**
     * set how to process when sliding at the last or first item.
     *
     * @param slideBorderMode {@link #SLIDE_BORDER_MODE_NONE}, {@link #SLIDE_BORDER_MODE_TO_PARENT},
     * {@link #SLIDE_BORDER_MODE_CYCLE}, default is {@link #SLIDE_BORDER_MODE_NONE}
     */
    public void setSlideBorderMode(int slideBorderMode) {
        this.slideBorderMode = slideBorderMode;
    }
    /**
     * whether animating when auto scroll at the last or first item, default is true.
     */
    public boolean isBorderAnimationEnabled() {
        return isBorderAnimation;
    }
    /**
     * set whether animating when auto scroll at the last or first item, default is true.
     */
    public void setBorderAnimation(boolean isBorderAnimation) {
        this.isBorderAnimation = isBorderAnimation;
    }
}
