package com.huantansheng.easyphotos;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

@SuppressLint("AppCompatCustomView")
public class HeldTextView extends TextView {
    public HeldTextView(Context context) {
        super(context);
        setTypeface(context);
    }

    public HeldTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setTypeface(context);
    }

    public HeldTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTypeface(context);
    }

    private void setTypeface(Context context) {
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "font/UTM Bebas.ttf"));
    }
}
