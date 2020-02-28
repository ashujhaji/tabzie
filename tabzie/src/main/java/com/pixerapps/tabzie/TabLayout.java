package com.pixerapps.tabzie;


/*MIT License

        Copyright (c) 2020 Ashutosh Jha

        Permission is hereby granted, free of charge, to any person obtaining a copy
        of this software and associated documentation files (the "Software"), to deal
        in the Software without restriction, including without limitation the rights
        to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
        copies of the Software, and to permit persons to whom the Software is
        furnished to do so, subject to the following conditions:

        The above copyright notice and this permission notice shall be included in all
        copies or substantial portions of the Software.

        THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
        IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
        FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
        AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
        LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
        OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
        SOFTWARE.*/

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

public class TabLayout extends FrameLayout {

    private static final int INDICATOR_ANIM_DURATION = 10000;

    private float acceleration = 200f;
    private float headMoveOffset = 200f;
    private float radiusMax;
    private float radiusMin;

    private float textSize;
    private int textColorId;
    private int textBgResId;
    private int selectedTextColorId;
    private int indicatorColorId;
    private int indicatorColorsId;
    private int[] indicatorColorArray;

    private LinearLayout tabContainer;
    private IndicatorView springView;
    private ViewPager viewPager;

    private List<TextView> tabs;

    private ViewPager.OnPageChangeListener delegateListener;
    private TabClickListener tabClickListener;
    private ObjectAnimator indicatorColorAnim;

    private float screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private ArrayList<Float> tabXCords = new ArrayList();

    private float x1, x2;
    static final int MIN_DISTANCE = 10;

    private int _xDelta;
    private int _yDelta;


    public TabLayout(Context context) {
        this(context, null);
    }

    public TabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
    }

    private void initAttrs(AttributeSet attrs) {
        textColorId = R.color.si_default_text_color;
        selectedTextColorId = R.color.si_default_text_color_selected;
        indicatorColorId = R.color.si_default_indicator_bg;
        textSize = getResources().getDimension(R.dimen.si_default_text_size);
        radiusMax = getResources().getDimension(R.dimen.si_default_radius_max);
        radiusMin = getResources().getDimension(R.dimen.si_default_radius_min);

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.TabLayout);
        textColorId = a.getResourceId(R.styleable.TabLayout_siTextColor, textColorId);
        selectedTextColorId = a.getResourceId(R.styleable.TabLayout_siSelectedTextColor, selectedTextColorId);
        textSize = a.getDimension(R.styleable.TabLayout_siTextSize, textSize);
        textBgResId = a.getResourceId(R.styleable.TabLayout_siTextBg, 0);
        indicatorColorId = a.getResourceId(R.styleable.TabLayout_siIndicatorColor, indicatorColorId);
        indicatorColorsId = a.getResourceId(R.styleable.TabLayout_siIndicatorColors, 0);
        radiusMax = a.getDimension(R.styleable.TabLayout_siRadiusMax, radiusMax);
        radiusMin = a.getDimension(R.styleable.TabLayout_siRadiusMin, radiusMin);
        a.recycle();

        if (indicatorColorsId != 0) {
            indicatorColorArray = getResources().getIntArray(indicatorColorsId);
        }
    }


    public void setViewPager(final ViewPager viewPager) {
        this.viewPager = viewPager;
        initSpringView();
        setUpListener();
    }


    private void initSpringView() {
        addPointView();
        addTabContainerView();
        addTabItems();
        for (int i = 0; i <= tabs.size(); i++) {
            tabXCords.add(i * screenWidth / tabs.size());
        }
    }

    private void addPointView() {
        springView = new IndicatorView(getContext());
        springView.setIndicatorColor(getResources().getColor(indicatorColorId));
        addView(springView);
    }

    private void addTabContainerView() {
        tabContainer = new LinearLayout(getContext());
        tabContainer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f));
        tabContainer.setOrientation(LinearLayout.HORIZONTAL);
        tabContainer.setGravity(Gravity.CENTER);
        addView(tabContainer);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void addTabItems() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
        tabs = new ArrayList<>();
        for (int i = 0; i < viewPager.getAdapter().getCount(); i++) {
            TextView textView = new TextView(getContext());
            if (viewPager.getAdapter().getPageTitle(i) != null) {
                textView.setText(viewPager.getAdapter().getPageTitle(i));
            }
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            textView.setTextColor(getResources().getColor(textColorId));
            textView.setPadding(5, 10, 5, 10);
            textView.setCompoundDrawablePadding(5);
            if (textBgResId != 0) {
                textView.setBackgroundResource(textBgResId);
            }
            textView.setLayoutParams(layoutParams);
            final int position = i;
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tabClickListener == null || tabClickListener.onTabClick(position)) {
                        viewPager.setCurrentItem(position);
                    }
                }
            });

            //  springView.setOnTouchListener(new DragExperimentTouchListener(springView.getX(),springView.getY()));

            textView.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    final int X = (int) event.getRawX();
                    final int Y = (int) event.getRawY();
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            FrameLayout.LayoutParams lParams = (FrameLayout.LayoutParams) springView.getLayoutParams();
                            _xDelta = X - lParams.leftMargin;
                            _yDelta = Y - lParams.topMargin;
                            //x1 = event.getX();
                            break;
                        case MotionEvent.ACTION_UP:

                            for (int i = 0; i < tabXCords.size(); i++) {
                                if (springView.getX() > Float.parseFloat(tabXCords.get(i).toString()) && springView.getX() < Float.parseFloat(tabXCords.get(i + 1).toString())) {
                                    if (springView.getX()-Float.parseFloat(tabXCords.get(i).toString()) > Float.parseFloat(tabXCords.get(i+1).toString())-springView.getX()){
                                        springView.getCoordinates().setLeft(tabXCords.get(i+1)-springView.getX());
                                        springView.getCoordinates().setRight((tabXCords.get(i+1)-springView.getX())+screenWidth/tabs.size());
                                        springView.postInvalidate();
                                        Log.d("coordPoint", "" + tabXCords.get(i+1));
                                        Log.d("coordPoint", "" + tabXCords.get(i+2));
                                        break;
                                    }else {
                                        springView.getCoordinates().setLeft(springView.getX()-tabXCords.get(i));
                                        springView.getCoordinates().setRight((springView.getX()-tabXCords.get(i))+screenWidth/tabs.size());
                                        springView.postInvalidate();
                                        Log.d("coordPoint", "" + tabXCords.get(i));
                                        Log.d("coordPoint", "" + tabXCords.get(i+1));
                                        break;
                                    }
                                }
                            }

                            break;
                        case MotionEvent.ACTION_MOVE:
                            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) springView.getLayoutParams();
                            layoutParams.leftMargin = X - _xDelta;
                            layoutParams.rightMargin = -250;
                            springView.setLayoutParams(layoutParams);
                            break;
                    }
                    return TabLayout.super.onTouchEvent(event);
                }
            });

            tabs.add(textView);
            tabContainer.addView(textView);
        }
    }

    /**
     * Set current point position.
     */
    private void createPoints() {
        View view = tabs.get(viewPager.getCurrentItem());
        springView.getCoordinates().setLeft(view.getX());
        springView.getCoordinates().setTop(view.getY());
        springView.getCoordinates().setBottom(view.getY() + view.getHeight());
        springView.getCoordinates().setRight(view.getX() + view.getWidth());

        springView.animCreate();
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            createPoints();
            setSelectedTextColor(viewPager.getCurrentItem());
        }
    }


    private void setUpListener() {
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setSelectedTextColor(position);
                if (delegateListener != null) {
                    delegateListener.onPageSelected(position);
                }
                springView.animateView();
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position < tabs.size() - 1) {
                    // x
                    float headX = 1f;
                    if (positionOffset < headMoveOffset) {
                        float positionOffsetTemp = positionOffset / headMoveOffset;
                        headX = (float) ((Math.atan(positionOffsetTemp * acceleration * 2 - acceleration) + (Math.atan(acceleration))) / (2 * (Math.atan(acceleration))));
                    }
                    springView.getCoordinates().setLeft(tabs.get(position).getX() - headX * getPositionDistance(position));
                    springView.getCoordinates().setRight((tabs.get(position).getX() - headX * getPositionDistance(position)) + tabs.get(position).getWidth());
                } else {
                    springView.getCoordinates().setLeft(tabs.get(position).getX());
                    springView.getCoordinates().setRight(tabs.get(position).getX() + tabs.get(position).getWidth());
                }

                if (indicatorColorsId != 0) {
                    float length = (position + positionOffset) / viewPager.getAdapter().getCount();
                    int progress = (int) (length * INDICATOR_ANIM_DURATION);
                    seek(progress);
                }

                springView.postInvalidate();
                if (delegateListener != null) {
                    delegateListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                if (delegateListener != null) {
                    delegateListener.onPageScrollStateChanged(state);
                }
            }
        });
    }


    private float getPositionDistance(int position) {
        float tarX = tabs.get(position + 1).getX();
        float oriX = tabs.get(position).getX();
        return oriX - tarX;
    }

    private void setSelectedTextColor(int position) {
        for (TextView tab : tabs) {
            tab.setTextColor(getResources().getColor(textColorId));
            for (Drawable drawable : tab.getCompoundDrawables()) {
                if (drawable != null) {
                    drawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(tab.getContext(), textColorId), PorterDuff.Mode.SRC_IN));
                }
            }
        }
        tabs.get(position).setTextColor(getResources().getColor(selectedTextColorId));
        for (Drawable drawable : tabs.get(position).getCompoundDrawables()) {
            if (drawable != null) {
                drawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(tabs.get(position).getContext(), selectedTextColorId), PorterDuff.Mode.SRC_IN));
            }
        }
    }

    private void createIndicatorColorAnim() {
        indicatorColorAnim = ObjectAnimator.ofInt(springView, "indicatorColor", indicatorColorArray);
        indicatorColorAnim.setEvaluator(new ArgbEvaluator());
        indicatorColorAnim.setDuration(INDICATOR_ANIM_DURATION);
    }

    private void seek(long seekTime) {
        if (indicatorColorAnim == null) {
            createIndicatorColorAnim();
        }
        indicatorColorAnim.setCurrentPlayTime(seekTime);
    }

    public List<TextView> getTabs() {
        return tabs;
    }

    public TextView getTabAt(int pos) {
        return tabs.get(pos);
    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        this.delegateListener = listener;
    }

    public void setOnTabClickListener(TabClickListener listener) {
        this.tabClickListener = listener;
    }

}
