package com.shen.bannerlibrary.indicator;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.shen.bannerlibrary.BannerConfig;
import com.shen.bannerlibrary.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName: banner1.0
 * @Package: com.test.banner.widget.indicator
 * @ClassName: DefaultIndicator
 * @Description: java类作用描述
 * @Author: 申中佳
 * @CreateDate: 2020-07-12 14:41
 * @UpdateUser: 更新者
 * @UpdateDate: 2020-07-12 14:41
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class DefaultIndicator extends LinearLayout implements Indicator {

    private int indicatorSize;
    private DisplayMetrics dm;
    private int mIndicatorSelectedResId = R.drawable.banner_selected_shape;
    private int mIndicatorUnselectedResId = R.drawable.banner_unselected_shape;
    private int mIndicatorMargin = BannerConfig.PADDING_SIZE;
    private int mIndicatorSelectedWidth;
    private int mIndicatorSelectedHeight;
    private int mIndicatorWidth;
    private int mIndicatorHeight;
    private List<ImageView> indicatorImages;

    public DefaultIndicator(Context context) {
        this(context, null);
    }

    public DefaultIndicator(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DefaultIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        dm = context.getResources().getDisplayMetrics();
        indicatorSize = dm.widthPixels / 80;
        mIndicatorSelectedWidth = indicatorSize;
        mIndicatorSelectedHeight = indicatorSize;
        mIndicatorHeight = indicatorSize;
        mIndicatorWidth = indicatorSize;
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        LayoutParams
                Selectedparams = new LayoutParams(mIndicatorSelectedWidth, mIndicatorSelectedHeight);

        Selectedparams.leftMargin = mIndicatorMargin;
        Selectedparams.rightMargin = mIndicatorMargin;
        LayoutParams
                Unselectedparams = new LayoutParams(mIndicatorWidth, mIndicatorHeight);
        Unselectedparams.leftMargin = mIndicatorMargin;
        Unselectedparams.rightMargin = mIndicatorMargin;
        for (int i = 0; i < indicatorImages.size(); i++) {
            if (position != i) {
                indicatorImages.get(i).setImageResource(mIndicatorUnselectedResId);
//                indicatorImages.get(i).setLayoutParams(Unselectedparams);

            } else {
                indicatorImages.get(i).setImageResource(mIndicatorSelectedResId);
//                indicatorImages.get(i).setLayoutParams(Selectedparams);
            }
        }

    }

    @Override
    public void onPageScrollStateChanged(int var1) {

    }

    @Override
    public void createIndicator(int size) {
        indicatorImages = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            ImageView imageView = new ImageView(getContext());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            LayoutParams params;
            if (i == 0) {
                params = new LayoutParams(-2, -2);
                imageView.setImageResource(mIndicatorSelectedResId);
            } else {
                params = new LayoutParams(-2, -2);
                imageView.setImageResource(mIndicatorUnselectedResId);
            }
            params.leftMargin = 5;
            params.rightMargin = 5;
            indicatorImages.add(imageView);
            addView(imageView, params);
        }
    }
}
