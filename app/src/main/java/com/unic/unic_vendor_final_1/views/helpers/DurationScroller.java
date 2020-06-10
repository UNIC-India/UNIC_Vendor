package com.unic.unic_vendor_final_1.views.helpers;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

public class DurationScroller extends Scroller {
    private double scrollFactor = 1;
    public DurationScroller(Context context) {
        super(context);
    }
    public DurationScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }
    public void setScrollDurationFactor(double scrollFactor) {
        this.scrollFactor = scrollFactor;
    }
    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, (int)(duration * scrollFactor));
    }
}
