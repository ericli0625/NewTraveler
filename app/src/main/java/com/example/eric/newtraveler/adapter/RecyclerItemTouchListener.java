package com.example.eric.newtraveler.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;


public class RecyclerItemTouchListener implements RecyclerView.OnItemTouchListener {

    private OnItemClickListener mListener;
    private GestureDetector gestureDetector;

    @Nullable
    private View mChildView;

    private int mChildViewPosition;

    public RecyclerItemTouchListener(Context context, OnItemClickListener listener) {
        this.gestureDetector = new GestureDetector(context, new GestureListener());
        this.mListener = listener;
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent event) {
        mChildView = recyclerView.findChildViewUnder(event.getX(), event.getY());
        if (mChildView != null) {
            mChildViewPosition = recyclerView.getChildAdapterPosition(mChildView);
        }
        return mChildView != null && gestureDetector.onTouchEvent(event);
    }

    @Override
    public void onTouchEvent(RecyclerView view, MotionEvent event) {
        // Not needed.
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    /**
     * A click mListener for items.
     */
    public interface OnItemClickListener {
        void onItemClick(int position);

        void onItemLongPress(int position);
    }

    protected class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            if (mChildView != null) {
                mListener.onItemClick(mChildViewPosition);
            }
            return true;
        }

        @Override
        public void onLongPress(MotionEvent event) {
            if (mChildView != null) {
                mListener.onItemLongPress(mChildViewPosition);
            }
        }

        @Override
        public boolean onDown(MotionEvent event) {
            // Best practice to always return true here.
            // http://developer.android.com/training/gestures/detector.html#detect
            return true;
        }
    }
}