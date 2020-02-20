package com.pixerapps.tabzie;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.OvershootInterpolator;

public class IndicatorView extends View {

    private Paint paint;

    private Coordinates coordinates;


    public IndicatorView(Context context) {
        this(context, null);
    }

    public IndicatorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setAlpha(0);
        coordinates = new Coordinates();

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(1);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRoundRect(new RectF(coordinates.getLeft(),coordinates.getTop(),coordinates.getRight(),coordinates.getBottom()),100,100,paint);
        super.onDraw(canvas);
    }

    public void animCreate() {
        setPivotX(getCoordinates().getLeft());
        setPivotY(getCoordinates().getTop());
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator oaX = ObjectAnimator.ofFloat(this, "scaleX", 0.3f, 1f);
        ObjectAnimator oaY = ObjectAnimator.ofFloat(this, "scaleY", 0.3f, 1f);
        ObjectAnimator oaA = ObjectAnimator.ofFloat(this, "alpha", 0.0f, 1f);
        animatorSet.play(oaX).with(oaY).with(oaA);
        animatorSet.setDuration(600);
        animatorSet.setInterpolator(new OvershootInterpolator());
        animatorSet.setStartDelay(300);
        animatorSet.start();
    }

    public void animateView() {
        setPivotX(getCoordinates().getLeft());
        setPivotY(getCoordinates().getTop());
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator oaX = ObjectAnimator.ofFloat(this, "scaleX", 0.9f, 1f);
        ObjectAnimator oaY = ObjectAnimator.ofFloat(this, "scaleY", 0.9f, 1f);
        animatorSet.play(oaX).with(oaY);
        animatorSet.setDuration(600);
        animatorSet.setInterpolator(new OvershootInterpolator());
        animatorSet.setStartDelay(100);
        animatorSet.start();
    }

    public Coordinates getCoordinates(){
        return coordinates;
    }

    public void setIndicatorColor(int color) {
        paint.setColor(color);
    }
}
