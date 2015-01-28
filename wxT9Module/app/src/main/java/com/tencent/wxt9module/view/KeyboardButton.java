package com.tencent.wxt9module.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tencent.wxt9module.R;

/**
 * Description:
 * Created by browserwang on 2014/12/1.
 */
public class KeyboardButton extends RelativeLayout{

    private ImageView bgView;
    private TextView numberTv, letterTv;
    //private Animation bgHideAnimation;

    private String numberText, letterText;
    private float numberTextSize, letterTextSize;
    private int numberTextColor, letterTextColor;
    private int bgNormalDrawable, bgPressedDrawable;
    private int bgDismissTime;

    public KeyboardButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        View.inflate(context, R.layout.keyboard_btn, this);
        bgView = (ImageView) findViewById(R.id.kb_bg);
        numberTv = (TextView) findViewById(R.id.kb_number_tv);
        letterTv = (TextView) findViewById(R.id.kb_letter_tv);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.KeyboardButton);
        numberText = typedArray.getString(R.styleable.KeyboardButton_number_text);
        letterText = typedArray.getString(R.styleable.KeyboardButton_letter_text);
        numberTextSize = typedArray.getDimension(R.styleable.KeyboardButton_number_text_size, 28);
        letterTextSize = typedArray.getDimension(R.styleable.KeyboardButton_letter_text_size, 9);
        numberTextColor = typedArray.getColor(R.styleable.KeyboardButton_number_text_color, R.color.black);
        letterTextColor = typedArray.getColor(R.styleable.KeyboardButton_letter_text_color, R.color.keyboard_text_gray);
        bgNormalDrawable = typedArray.getResourceId(R.styleable.KeyboardButton_normal_bg_drawable, R.drawable.trans);
        bgPressedDrawable = typedArray.getResourceId(R.styleable.KeyboardButton_pressed_bg_drawable, R.drawable.dial_keyboard_pressed);
        bgDismissTime = typedArray.getInt(R.styleable.KeyboardButton_bg_dismiss_time, 500);

        typedArray.recycle();

        numberTv.setText(numberText);
        numberTv.setTextSize(numberTextSize);
        numberTv.setTextColor(numberTextColor);
        letterTv.setText(letterText);
        letterTv.setTextSize(letterTextSize);
        letterTv.setTextColor(letterTextColor);
        bgView.setBackgroundResource(bgNormalDrawable);

    }

}
