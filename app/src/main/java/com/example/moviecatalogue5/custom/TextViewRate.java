package com.example.moviecatalogue5.custom;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import androidx.appcompat.widget.AppCompatTextView;

public class TextViewRate extends AppCompatTextView {

    private static final String TAG = TextViewRate.class.getSimpleName();
    private int
            colorGood = Color.parseColor("#8BC34A"),
            colorNormal = Color.parseColor("#FFC107"),
            colorBad = Color.parseColor("#F44336");

    private float good = (float) 7.0;
    private float bad = (float) 4.0;

    public TextViewRate(Context context) {
        super(context);
    }

    public TextViewRate(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextViewRate(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setTextWithColor(String text) {
        super.setText(text);
        System.out.println("setTextWithColor: " + text);
        try {
            Float rate = Float.parseFloat(text.toString());
            super.setTextColor(colorGood);
            if (rate < good) {
                if (rate < bad) {
                    super.setTextColor(colorBad);
                }
                else {
                    super.setTextColor(colorNormal);
                }
            }
        } catch(Exception e) {
            Log.d(TAG + "-Exception", e.getMessage());
        }
    }
}
