package com.shen.bannerlibrary;

import android.content.Context;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;



import android.support.v4.view.ViewPager;


import com.shen.bannerlibrary.adapter.BannerPagerAdapter;
import com.shen.bannerlibrary.holder.ViewHolderCreator;
import com.shen.bannerlibrary.indicator.Indicator;
import com.shen.bannerlibrary.listener.OnBannerListener;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName: banner1.0
 * @Package: com.test.banner.widget
 * @ClassName: MyBanner
 * @Description: java类作用描述
 * @Author: 申中佳
 * @CreateDate: 2020-07-12 12:57
 * @UpdateUser: 更新者
 * @UpdateDate: 2020-07-12 12:57
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class BannerView<T> extends FrameLayout implements ViewPager.OnPageChangeListener {
    public String tag = "BannerView";
    private BannerViewPager mViewPager;
    private BannerScroller mScroller;
    private int scrollTime = BannerConfig.DURATION;
    private boolean isAutoPlay = BannerConfig.IS_AUTO_PLAY;// 是否自动播放
    private boolean mIsLoop = true;// 是否支持无限轮播
    private int delayTime = BannerConfig.TIME;
    private int count = 0;
    private int currentItem;

    private final Runnable mLoopRunnable = new Runnable() {
        @Override
        public void run() {
            if (count > 1 && isAutoPlay) {
                currentItem = currentItem % (count + 1) + 1;
                if (currentItem == 1) {
                    mViewPager.setCurrentItem(currentItem, false);
                    postDelayed(mLoopRunnable, delayTime);
                } else {
                    mViewPager.setCurrentItem(currentItem);

                    postDelayed(mLoopRunnable, delayTime);
                }
            }
        }
    };


    public BannerView(@NonNull Context context) {
        this(context, null);
    }

    public BannerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        handleTypedArray(context, attrs);
        init();
    }

    private void handleTypedArray(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.widget_my_banner, this, true);
        mViewPager = view.findViewById(R.id.bannerViewPager);
        mViewPager.setOffscreenPageLimit(3);
        initViewPagerScroll();
    }


    private void initViewPagerScroll() {
        try {
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            mScroller = new BannerScroller(getContext());

            mField.set(mViewPager, mScroller);
        } catch (Exception e) {
            Log.e(tag, e.getMessage());
        }
    }


    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int position) {
        currentItem = position;
        if (mIndicator != null) {
            mIndicator.onPageSelected(toRealPosition(position));
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        switch (state) {
            case 0://No operation
                if (currentItem == 0) {
                    mViewPager.setCurrentItem(count, false);
                } else if (currentItem == count + 1) {
                    mViewPager.setCurrentItem(1, false);
                }
                break;
            case 1://start Sliding
                if (currentItem == count + 1) {
                    mViewPager.setCurrentItem(1, false);
                } else if (currentItem == 0) {
                    mViewPager.setCurrentItem(count, false);
                }
                break;
            case 2://end Sliding
                break;
        }
    }

    /**
     * 返回真实的位置
     *
     * @param position
     * @return 下标从0开始
     */
    public int toRealPosition(int position) {

        return position % count;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        Log.i(tag, ev.getAction() + "--" + isAutoPlay);
        if (isAutoPlay) {
            int action = ev.getAction();
            if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL
                    || action == MotionEvent.ACTION_OUTSIDE) {
                start();
            } else if (action == MotionEvent.ACTION_DOWN) {
                stop();
            }
        }
        return super.dispatchTouchEvent(ev);
    }


    /**
     * **********************************************************************
     * 对外公开API                                   *
     * **********************************************************************
     */
    private List<T> mDatas;
    private ViewHolderCreator mCreator;
    private BannerPagerAdapter mAdapter;
    private boolean mViewPagerIsScroll = true;//是否允许手动滑动viewpager

    private Indicator mIndicator;

    /**
     * 设置数据
     *
     * @param datas
     * @return
     */
    public BannerView<T> setData(List<T> datas) {
        if (datas == null) {
            throw new NullPointerException("Data cannot be null");
        }
        mDatas = new ArrayList<>();
        mDatas.addAll(datas);
        count = mDatas.size();
        return this;
    }

    public BannerView setIndicator(Indicator indicator) {

        this.mIndicator = indicator;
        return this;
    }

    /**
     * 设置ViewHolder创建器
     *
     * @param creator
     * @return
     */
    public BannerView setViewHolderCreator(ViewHolderCreator creator) {
        if (creator == null) {
            throw new NullPointerException("ViewHolderCreator cannot be null");
        }
        this.mCreator = creator;
        return this;
    }


    /**
     * 设置点击事件
     *
     * @param listener
     * @return
     */
    public BannerView setOnMyBannerListener(OnBannerListener listener) {
        if (mAdapter != null) {
            mAdapter.setOnBannerListener(listener);
        }
        return this;
    }

    /**
     * 完成banner构建（最后调用）
     *
     * @return
     */
    public BannerView build() {
        mScroller.setDuration(scrollTime);
        mAdapter = new BannerPagerAdapter(mViewPager, mDatas, mCreator, mIsLoop);
        mViewPager.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        int currentItem = mIsLoop ? mAdapter.getStartPosition() : 0;
        mViewPager.setCurrentItem(currentItem);

        mViewPager.clearOnPageChangeListeners();
        mViewPager.addOnPageChangeListener(this);

        if (mIndicator != null) {
            mIndicator.createIndicator(count);
        }
        if (mViewPagerIsScroll && count > 1) {
            mViewPager.setScrollable(true);
        } else {
            mViewPager.setScrollable(false);
        }
        if (isAutoPlay) {
            start();
        }
        return this;
    }


    /**
     * 开始轮播
     */
    public void start() {
        if (mAdapter == null) {
            return;
        }
        if (mIsLoop) {
            stop();
            postDelayed(mLoopRunnable, delayTime);
        }
    }

    /**
     * 停止轮播
     */
    public void stop() {
        removeCallbacks(mLoopRunnable);
    }
}
