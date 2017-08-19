package com.mitkoindo.smartcollection.utils;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by ericwijaya on 8/17/17.
 */

public class SimpleListItemDecoration extends RecyclerView.ItemDecoration {

    private int space;
    private int sideSpace;

    private int orientation;
    private Drawable divider;

    // those margins will be used in case divider drawable has undefined with or height. ex vertical divider with only 1dp height.
    private final int dividerMarginStart;
    private final int dividerMarginEnd;

    private GridLayoutManager gridLayoutManager;

    public SimpleListItemDecoration(int space, int sideSpace, GridLayoutManager gridLayoutManager) {
        this(space, sideSpace, gridLayoutManager.getOrientation(), null, 0, 0, gridLayoutManager);
    }

    public SimpleListItemDecoration(int space, int orientation) {
        this(space, 0, orientation, null, 0, 0, null);
    }

    public SimpleListItemDecoration(int orientation, Drawable divider, int dividerMarginStart, int dividerMarginEnd) {
        this(0, 0, orientation, divider, dividerMarginStart, dividerMarginEnd, null);
    }

    public SimpleListItemDecoration(int space, int sideSpace, int orientation, Drawable divider, int dividerMarginStart, int dividerMarginEnd, GridLayoutManager gridLayoutManager) {
        this.space = space;
        this.sideSpace = sideSpace;
        this.orientation = orientation;
        this.divider = divider;
        this.dividerMarginStart = dividerMarginStart;
        this.dividerMarginEnd = dividerMarginEnd;
        this.gridLayoutManager = gridLayoutManager;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int itemAdapterPosition = parent.getChildAdapterPosition(view);
        switch (orientation) {
            case RecyclerView.HORIZONTAL:
                outRect.right = space;
                if (itemAdapterPosition == 0) {
                    outRect.left = space;
                } else if (itemAdapterPosition != parent.getAdapter().getItemCount() - 1) {
                    outRect.right += getDividerWidth();
                }

                break;
            case RecyclerView.VERTICAL:
                outRect.bottom = space;
                if (itemAdapterPosition == 0) {
                    outRect.top = space;
                } else if (itemAdapterPosition != parent.getAdapter().getItemCount() - 1) {
                    outRect.bottom += getDividerHeight();
                }

                break;
            default:
                super.getItemOffsets(outRect, view, parent, state);
                break;
        }

        setupGridLayoutItemOffset(itemAdapterPosition, outRect);
    }

    private void setupGridLayoutItemOffset(int itemAdapterPosition, Rect outRect) {
        if (gridLayoutManager == null) {
            return;
        }

        GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();
        final int spanCount = gridLayoutManager.getSpanCount();
        if (spanSizeLookup.getSpanGroupIndex(itemAdapterPosition, spanCount) == 0) {
            switch (orientation) {
                case RecyclerView.VERTICAL: {
                    outRect.top = space;
                    break;
                }

                case RecyclerView.HORIZONTAL: {
                    outRect.left = space;
                    break;
                }
            }
        }

        if (spanSizeLookup.getSpanIndex(itemAdapterPosition, spanCount) == 0) {
            switch (orientation) {
                case RecyclerView.VERTICAL: {
                    outRect.left = space;
                    break;
                }

                case RecyclerView.HORIZONTAL: {
                    outRect.top = space;
                    break;
                }
            }
        }

        switch (orientation) {
            case RecyclerView.VERTICAL: {
                outRect.right = space;
                break;
            }

            case RecyclerView.HORIZONTAL: {
                outRect.bottom = space;
                break;
            }
        }
    }

    private int getDividerWidth() {
        return Math.max(divider != null ? divider.getIntrinsicWidth() : 0, 0);
    }

    private int getDividerHeight() {
        return Math.max(divider != null ? divider.getIntrinsicHeight() : 0, 0);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (this.divider == null) {
            return;
        }

        final int originLeft = parent.getPaddingLeft();
        final int originRight = parent.getWidth() - parent.getPaddingRight();
        final int originTop = parent.getPaddingTop();
        final int originBottom = parent.getHeight() - parent.getPaddingBottom();
        final int childCount = parent.getChildCount();
        final int dividerWidth = getDividerWidth();
        final int dividerHeight = getDividerHeight();

        for (int i = 0; i < childCount - 1; ++i) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int left = originLeft;
            int top = originTop;
            int right = originRight;
            int bottom = originBottom;
            if (orientation == RecyclerView.HORIZONTAL) {
                if (dividerHeight > 0) {
                    top = Math.max((originBottom - originTop - dividerHeight) / 2, originTop);
                    bottom = Math.min(top + dividerHeight, originBottom);
                } else {
                    top = originTop + dividerMarginStart;
                    bottom = originBottom - dividerMarginEnd;
                }

                left = child.getRight() + params.rightMargin + space / 2;
                right = left + dividerWidth;
            } else if (orientation == RecyclerView.VERTICAL) {
                if (dividerWidth > 0) {
                    left = Math.max((originRight - originLeft - dividerWidth) / 2, originLeft);
                    right = Math.min(left + dividerWidth, originRight);
                } else {
                    left = originLeft + dividerMarginStart;
                    right = originRight - dividerMarginEnd;
                }

                top = child.getBottom() + params.bottomMargin + space / 2;
                bottom = top + dividerHeight;
            }

            this.divider.setBounds(left, top, right, bottom);
            this.divider.draw(c);
        }
    }
}
