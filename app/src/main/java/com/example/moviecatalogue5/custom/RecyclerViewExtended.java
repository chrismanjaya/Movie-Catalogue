package com.example.moviecatalogue5.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.AbsListView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewExtended extends RecyclerView {

    private GridLayoutManager manager;
    private int columnWidth = -1;
    private int spanCount;

    public RecyclerViewExtended(Context context) {
        super(context);
        initial(context, null);
    }

    public RecyclerViewExtended(Context context, AttributeSet attrs) {
        super(context, attrs);
        initial(context, attrs);
    }

    public RecyclerViewExtended(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initial(context, attrs);
    }

    private void initial(Context context, AttributeSet attrs) {
        if (attrs != null) {
            int[] attrsArray = {
                    android.R.attr.columnWidth
            };
            TypedArray array = context.obtainStyledAttributes(attrs, attrsArray);
            columnWidth = array.getDimensionPixelSize(0,-1);
            array.recycle();
        }
        manager = new GridLayoutManager(context, 1);
        setLayoutManager(manager);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);
        if (columnWidth > 0) {
            spanCount= Math.max(1, getMeasuredWidth() / columnWidth);
            manager.setSpanCount(spanCount);
        }
    }

    public int getSpanCount() {
        return spanCount;
    }

    public void setOnScrollListener(AbsListView.OnScrollListener onScrollListener) {
    }
}
