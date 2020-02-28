package com.pixerapps.tabzie;

import android.view.MotionEvent;
import android.view.View;

public class DragExperimentTouchListener implements View.OnTouchListener {

    public DragExperimentTouchListener(float initalX, float initialY) {
        lastX = initalX;
        lastY = initialY;
    }

    boolean isDragging = false;
    float lastX;
    float lastY;
    float deltaX;

    @Override
    public boolean onTouch(View arg0, MotionEvent arg1) {

        int action = arg1.getAction();

        if (action == MotionEvent.ACTION_DOWN && !isDragging) {
            isDragging = true;
            deltaX = arg1.getX();
            return true;
        } else if (isDragging) {
            if (action == MotionEvent.ACTION_MOVE) {

                arg0.setX(arg0.getX() + arg1.getX() - deltaX);
                arg0.setY(arg0.getY());
                return true;
            } else if (action == MotionEvent.ACTION_UP) {
                isDragging = false;
                lastX = arg1.getX();
                lastY = arg1.getY();
                return true;
            } else if (action == MotionEvent.ACTION_CANCEL) {
                arg0.setX(lastX);
                arg0.setY(lastY);
                isDragging = false;
                return true;
            }
        }

        return false;
    }
}