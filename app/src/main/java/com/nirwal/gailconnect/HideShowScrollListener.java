package com.nirwal.gailconnect;

import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

public abstract class HideShowScrollListener extends RecyclerView.OnScrollListener {
    private static final String TAG = "HideShowScrollListener";
    private static final int HIDE_THRESHOLD = 40;
    private int scrolledDistance = 0;
    private boolean controlsVisible = true;

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        onScrolled();

        if (scrolledDistance > HIDE_THRESHOLD && controlsVisible) {
            Log.d(TAG, "onScrolled: hide");
            onHide();
            controlsVisible = false;
            scrolledDistance = 0;
        } else if (scrolledDistance < -HIDE_THRESHOLD && !controlsVisible) {
            Log.d(TAG, "onScrolled: show");
            onShow();
            controlsVisible = true;
            scrolledDistance = 0;
        }

        if((controlsVisible && dy>0) || (!controlsVisible && dy<0)) {
            scrolledDistance += dy;
        }
    }

    public abstract void onHide();
    public abstract void onShow();
    public abstract void onScrolled();
}