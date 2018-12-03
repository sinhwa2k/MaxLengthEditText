package com.sinhwa2k.maxlengthedittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputFilter;
import android.util.AttributeSet;

public class MaxLengthEditText extends AppCompatEditText {
    private Paint paint;
    private Paint paintCorrect;
    private Paint paintIncorrect;
    private int maxLength;
    private int maxDrawTextWidth;
    private int yPos;
    private String drawText;

    public MaxLengthEditText(Context context) {
        super(context);
        initViews();
        initAttrs(context, null);
    }

    public MaxLengthEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
        initAttrs(context, attrs);
    }

    public MaxLengthEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews();
        initAttrs(context, attrs);
    }

    private void initViews() {
        paintCorrect = new Paint();
        paintIncorrect = new Paint();
        paintCorrect.setTextAlign(Paint.Align.RIGHT);
        paintCorrect.setAntiAlias(true);
        paintIncorrect.setTextAlign(Paint.Align.RIGHT);
        paintIncorrect.setAntiAlias(true);
        maxLength = getMaxLength();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.getTheme()
            .obtainStyledAttributes(attrs,
                        R.styleable.MaxLengthEditText,
                        0,
                        0);

        int maxLengthTextSize = typedArray.getDimensionPixelSize(R.styleable.MaxLengthEditText_maxLengthTextSize, (int) (getTextSize() / 2));
        paintCorrect.setTextSize(maxLengthTextSize);
        paintIncorrect.setTextSize(maxLengthTextSize);

        int correctColor = typedArray.getColor(R.styleable.MaxLengthEditText_maxLengthCorrectColor, Color.BLUE);
        int incorrectColor = typedArray.getColor(R.styleable.MaxLengthEditText_maxLengthIncorrectColor, Color.RED);
        paintCorrect.setColor(correctColor);
        paintIncorrect.setColor(incorrectColor);

        maxDrawTextWidth = (int) paintCorrect.measureText(maxLength + "/" + maxLength);
        yPos = (int) Math.abs((paintCorrect.descent() + paintCorrect.ascent()) / 2);
        setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight() + maxDrawTextWidth, getPaddingBottom());
        processData();
    }

    private int getMaxLength() {
        int maxLength = 0;
        for (InputFilter inputFilter : getFilters()) {
            if (inputFilter instanceof InputFilter.LengthFilter) {
                maxLength = ((InputFilter.LengthFilter) inputFilter).getMax();
            }
        }
        return maxLength;
    }

    private void processData() {
        int currentLength = 0;

        if (getText() != null) {
            currentLength = getText().length();
        }

        if (paintCorrect != null && paintIncorrect != null) {
            paint = (currentLength == maxLength) ? paintCorrect : paintIncorrect;
            drawText = currentLength + "/" + maxLength;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawText(drawText, getScrollX() + getWidth(), getHeight() / 2 + yPos, paint);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        processData();
    }
}
