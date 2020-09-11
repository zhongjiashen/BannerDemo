package com.shen.bannerlibrary.indicator;

import android.support.annotation.Px;

public interface Indicator {
    void onPageScrolled(int position, float positionOffset, @Px int positionOffsetPixels);
    void onPageSelected(int position);
    void onPageScrollStateChanged(int var1);

    void createIndicator(int size);
}
