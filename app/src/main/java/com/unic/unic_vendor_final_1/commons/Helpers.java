package com.unic.unic_vendor_final_1.commons;

import android.graphics.PorterDuff;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class Helpers {
    public static void enableDisableViewGroup(ViewGroup viewGroup, boolean enabled) {
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = viewGroup.getChildAt(i);
            view.setEnabled(enabled);
            if (view instanceof ViewGroup) {
                enableDisableViewGroup((ViewGroup) view, enabled);
            }
        }
    }
   }
