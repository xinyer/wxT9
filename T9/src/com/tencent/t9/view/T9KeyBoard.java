package com.tencent.t9.view;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tencent.t9.R;

/**
 * Created by browserwang on 2014/11/14.
 */
public class T9KeyBoard extends LinearLayout implements View.OnClickListener, View.OnLongClickListener {

    private KeyboardButton btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9;
    private KeyboardButton btnStar, btnPound;
    private ImageView btnDial, btnDel;

    private StringBuilder sb = new StringBuilder();
    private onKeyClickListener mListener;
    private onDialBtnClickListener mDialBtnClickListener;


    public T9KeyBoard(Context context) {
        super(context);
        initView(context);
    }

    public T9KeyBoard(Context context, AttributeSet attrs) {
        super(context, attrs);

        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.t9_keyboard, null);

        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        addView(v, params);

        btn0 = (KeyboardButton) v.findViewById(R.id.btn0);
        btn1 = (KeyboardButton) v.findViewById(R.id.btn1);
        btn2 = (KeyboardButton) v.findViewById(R.id.btn2);
        btn3 = (KeyboardButton) v.findViewById(R.id.btn3);
        btn4 = (KeyboardButton) v.findViewById(R.id.btn4);
        btn5 = (KeyboardButton) v.findViewById(R.id.btn5);
        btn6 = (KeyboardButton) v.findViewById(R.id.btn6);
        btn7 = (KeyboardButton) v.findViewById(R.id.btn7);
        btn8 = (KeyboardButton) v.findViewById(R.id.btn8);
        btn9 = (KeyboardButton) v.findViewById(R.id.btn9);
        btnStar = (KeyboardButton) v.findViewById(R.id.btn_star);
        btnPound = (KeyboardButton) v.findViewById(R.id.btn_pound);
        btnDial = (ImageView) v.findViewById(R.id.btn_done);
        btnDel = (ImageView) v.findViewById(R.id.btn_del);

        btn0.setOnClickListener(this);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn8.setOnClickListener(this);
        btn9.setOnClickListener(this);
        btnStar.setOnClickListener(this);
        btnPound.setOnClickListener(this);
        btnDel.setOnClickListener(this);
        btnDial.setOnClickListener(this);

        btn0.setOnLongClickListener(this);
        btnDel.setOnTouchListener(new DeleteCharOneByOneListener());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_done:
                if (mDialBtnClickListener!=null) {
                    mDialBtnClickListener.onDialBtnClick(sb.toString());
                }
                break;

            default:
                click(v.getId());
        }

    }

    private void click(int btnId) {
        switch (btnId) {
            case R.id.btn0:
                sb.append("0");
                break;
            case R.id.btn1:
                sb.append("1");
                break;
            case R.id.btn2:
                sb.append("2");
                break;
            case R.id.btn3:
                sb.append("3");
                break;
            case R.id.btn4:
                sb.append("4");
                break;
            case R.id.btn5:
                sb.append("5");
                break;
            case R.id.btn6:
                sb.append("6");
                break;
            case R.id.btn7:
                sb.append("7");
                break;
            case R.id.btn8:
                sb.append("8");
                break;
            case R.id.btn9:
                sb.append("9");
                break;
            case R.id.btn_star:
                sb.append("*");
                break;
            case R.id.btn_pound:
                sb.append("#");
                break;
            case R.id.btn_del:
                delChar(true);
                break;
        }

        if (mListener != null) {
            mListener.onResult(sb.toString(), true);
            int i = sb.length()-1;
            if (i>=0) {
                mListener.onResultLastChar(sb.charAt(i));
            }

        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (v.getId() == R.id.btn0) { //button 0
            sb.append("+");
            if (mListener != null) {
                mListener.onResult(sb.toString(), true);
                int index = sb.length()-1;
                if (index>=0) {
                    mListener.onResultLastChar(sb.charAt(index));
                }
            }
            return true;
        }
        return false;
    }

    public void appendString(String str) {
        if (TextUtils.isEmpty(str)) return;
        sb.append(str);
        if (mListener != null) {
            mListener.onResult(sb.toString(), true);
            int index = sb.length()-1;
            if (index>=0) {
                mListener.onResultLastChar(sb.charAt(index));
            }
        }
    }

    public void setString(String str) {
        if (!TextUtils.isEmpty(str)) {
            sb.delete(0, sb.length());
            sb.append(str);
            if (mListener != null) {
                mListener.onResult(sb.toString(), true);
                int index = sb.length()-1;
                if (index>=0) {
                    mListener.onResultLastChar(sb.charAt(index));
                }
            }
        }
    }

    public void clearString() {
        sb.delete(0, sb.length());
        if (mListener!=null) {
            mListener.onResult("", true);
            mListener.onResultLastChar('\u0000');
        }
    }

    /**
     * 删除字符串
     * @param isShowResult 是否去搜索并显示结果
     *（产品逻辑：长按删除过程中不显示，长按结束才显示）
     * @return
     */
    public boolean delChar(boolean isShowResult) {
		int lastIndex = sb.length() - 1;
		if (lastIndex < 0) {
			return false;
		}
		sb.deleteCharAt(lastIndex);
		if (mListener != null) {
			mListener.onResult(sb.toString(), isShowResult);
            int index = sb.length()-1;
            if (index>=0)
            mListener.onResultLastChar(sb.charAt(index));
		}
        return true;
	}

    final Handler deleteCharHandler = new Handler();

    /**
     * 直到删除结束才通知mListener
     */
    private Runnable deleteCharThread = new Runnable() {

        @Override
        public void run() {
            delChar(false);
            deleteCharHandler.postDelayed(this, 100);
        }
    };

    private class DeleteCharOneByOneListener implements OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    deleteCharHandler.postDelayed(deleteCharThread, 500);
                    break;
                case MotionEvent.ACTION_UP:
                    deleteCharHandler.removeCallbacks(deleteCharThread);
                    if (mListener != null) {
                        mListener.onResult(sb.toString(), true);
                        int index = sb.length()-1;
                        if (index>=0) mListener.onResultLastChar(sb.charAt(index));
                    }
                    break;
            }
            return false;
        }
    }

	public void setOnKeyClickListener(onKeyClickListener listener) {
		mListener = listener;
	}

    public void setOnDialBtnClickListener(onDialBtnClickListener listener) {mDialBtnClickListener = listener;}

    /**
     * 按键点击事件监听，不包括通话键，通话键监听参考onDialBtnClickListener
     */
    public interface onKeyClickListener {
        /**
         * 点击键盘按键监听器
         * @param str           输出的字符串
         * @param isShowResult  是否需要去搜索
         * （蛋疼的逻辑，长按删除键，不是清空操作，为了模仿水果，100ms删除一个，删除过程不搜索数据，直至长按结束才去搜索）
         */
		public void onResult(String str, boolean isShowResult);

        /**
         * 最后一个字符
         * @param c 最后一个字符
         */
        public void onResultLastChar(char c);
	}

    public interface onDialBtnClickListener {
        /**
         * 拨号键点击事件监听器
         * @param str  当前输入的字符串
         */
        public void onDialBtnClick(String str);

    }

}
