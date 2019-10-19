package com.example.moviecatalogue5.custom;

import android.content.Context;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatImageView;

public class GridItemImageView extends AppCompatImageView {

    private static final float IMAGE_RATIO = 2f/3;

    public GridItemImageView(Context context) {
        super(context);
    }

    public GridItemImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GridItemImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        float newMeasuredHeight = getMeasuredWidth() / IMAGE_RATIO;
        setMeasuredDimension(getMeasuredWidth(), (int)newMeasuredHeight);
    }

}
