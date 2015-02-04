package com.tencent.t9.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tencent.t9.R;

/**
 * Description:T9键盘按键
 * Created by browserwang on 2014/12/1.
 */
public class KeyboardButton extends RelativeLayout{

    private TextView numberTv, letterTv;

    private String numberText, letterText;
    private float numberTextSize, letterTextSize;
    private int numberTextColor, letterTextColor;

    public KeyboardButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        View.inflate(context, R.layout.keyboard_btn, this);
        numberTv = (TextView) findViewById(R.id.kb_number_tv);
        letterTv = (TextView) findViewById(R.id.kb_letter_tv);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.KeyboardButton);
        numberText = typedArray.getString(R.styleable.KeyboardButton_number_text);
        letterText = typedArray.getString(R.styleable.KeyboardButton_letter_text);
        numberTextSize = typedArray.getDimension(R.styleable.KeyboardButton_number_text_size, 28);
        letterTextSize = typedArray.getDimension(R.styleable.KeyboardButton_letter_text_size, 9);
        numberTextColor = typedArray.getColor(R.styleable.KeyboardButton_number_text_color, R.color.keyboard_btn_num_color);
        letterTextColor = typedArray.getColor(R.styleable.KeyboardButton_letter_text_color, R.color.keyboard_btn_text_color);

        typedArray.recycle();

        numberTv.setText(numberText);
        numberTv.setTextSize(numberTextSize);
        numberTv.setTextColor(numberTextColor);
        letterTv.setText(letterText);
        letterTv.setTextSize(letterTextSize);
        letterTv.setTextColor(letterTextColor);

    }

}
