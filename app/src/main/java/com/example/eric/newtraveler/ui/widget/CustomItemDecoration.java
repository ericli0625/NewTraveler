package com.example.eric.newtraveler.ui.widget;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class CustomItemDecoration extends RecyclerView.ItemDecoration {

    protected Drawable mDivider;
    int leftRight = 10;
    int topBottom = 10;

    public CustomItemDecoration(int color) {
        mDivider = new ColorDrawable(color);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

        final GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
        final GridLayoutManager.SpanSizeLookup lookup = layoutManager.getSpanSizeLookup();

        if (mDivider == null || layoutManager.getChildCount() == 0) {
            return;
        }

        int spanCount = layoutManager.getSpanCount();
        int left, right, top, bottom;
        final int childCount = parent.getChildCount();
        if (layoutManager.getOrientation() == GridLayoutManager.VERTICAL) {
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                final int position = parent.getChildAdapterPosition(child);
                final int spanSize = lookup.getSpanSize(position);
                final int spanIndex = lookup.getSpanIndex(position, layoutManager.getSpanCount());
                boolean isFirst = layoutManager.getSpanSizeLookup().getSpanGroupIndex(position, spanCount) == 0;

                if (!isFirst && spanIndex == 0) {
                    left = layoutManager.getLeftDecorationWidth(child);
                    right = parent.getWidth();
                    top = child.getTop() - topBottom;
                    bottom = top + topBottom;
                    mDivider.setBounds(left, top, right, bottom);
                    mDivider.draw(c);
                }

                boolean isRight = spanIndex + spanSize == spanCount;
                if (!isRight) {

                    left = child.getRight();
                    right = left + leftRight;
                    top = child.getTop();
                    bottom = child.getBottom();
                    mDivider.setBounds(left, top, right, bottom);
                    mDivider.draw(c);
                }
            }
        }

    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(leftRight, topBottom, 0, 0);
    }
}
