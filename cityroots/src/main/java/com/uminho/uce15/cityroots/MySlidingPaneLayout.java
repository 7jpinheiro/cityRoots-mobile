package com.uminho.uce15.cityroots;

import android.content.Context;
import android.support.v4.widget.SlidingPaneLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by lgomes on 17-01-2014.
 */
public class MySlidingPaneLayout  extends SlidingPaneLayout {
    private boolean isSlideable;
    
    public MySlidingPaneLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        isSlideable=true;
        this.openPane();


        this.setPanelSlideListener(new PanelSlideListener() {
            @Override
            public void onPanelSlide(View view, float v) {
            }

            @Override
            public void onPanelOpened(View view) {
                findViewById(R.id.arrow).setVisibility(View.GONE);
            }

            @Override
            public void onPanelClosed(View view) {
                findViewById(R.id.arrow).setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(!this.isOpen()) isSlideable = false;
        else isSlideable = true;
        return !isSlideable || super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        if (!isSlideable) {
            // Careful here, view might be null
            getChildAt(1).dispatchTouchEvent(ev);
            return true;
        }
        return super.onTouchEvent(ev);
    }
}