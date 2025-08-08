package com.cdsy.aichat.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.cdsy.aichat.R;

public class GradientTextView extends AppCompatTextView {
    private int[] mColors;
    private float[] mPositions;
    private boolean mBold;
    private int mOrientation;
    private boolean mSelectMode;
    private int mNormalTextColor;

    public GradientTextView(@NonNull Context context) {
        this(context, null);
    }

    public GradientTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GradientTextView(@NonNull Context context, @Nullable AttributeSet attrs,
                            int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(@NonNull Context context, @Nullable AttributeSet attrs) {
        TypedArray typedArray =
                context.obtainStyledAttributes(attrs, R.styleable.GradientTextView);
        if (typedArray == null) {
            return;
        }

        mBold = typedArray.getBoolean(R.styleable.GradientTextView_gradient_bold,
                false);

        int colorsResId = typedArray.getResourceId(R.styleable.GradientTextView_gradient_colors, R.array.bonus_gradient_colors);
        mColors = getColorsFromResource(context, colorsResId);

        int positionsResId = typedArray.getResourceId(R.styleable.GradientTextView_gradient_positions, R.array.bonus_gradient_positions);
        mPositions = getPositionsFromResource(context, positionsResId);
        mOrientation = typedArray.getColor(R.styleable.GradientTextView_gradient_orientation, 0);

        mSelectMode = typedArray.getBoolean(R.styleable.GradientTextView_gradient_selectMode, false);

        // Save the normal text color
        mNormalTextColor = getCurrentTextColor();

        typedArray.recycle();
        if (mBold) {
            getPaint().setFakeBoldText(true);
        }
        updateTextAppearance();
    }

    private int[] getColorsFromResource(Context context, int resId) {
        try {
            String[] colorStrings = context.getResources().getStringArray(resId);
            int[] colors = new int[colorStrings.length];
            for (int i = 0; i < colorStrings.length; i++) {
                colors[i] = Color.parseColor(colorStrings[i]);
            }
            return colors;
        } catch (Exception e) {
            // 如果解析失败，返回默认颜色
            return new int[]{
                    Color.argb(255, 168, 153,243),
                    Color.argb(255, 250, 200, 228)
            };
        }
    }

    private float[] getPositionsFromResource(Context context, int resId) {
        try {
            String[] positionStrings = context.getResources().getStringArray(resId);
            float[] positions = new float[positionStrings.length];
            for (int i = 0; i < positionStrings.length; i++) {
                positions[i] = Float.parseFloat(positionStrings[i]);
            }
            return positions;
        } catch (Exception e) {
            // 如果解析失败，返回默认位置
            return new float[]{0.0f, 0.3f};
        }
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            updateTextAppearance();
        }
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        if (mSelectMode) {
            updateTextAppearance();
        }
    }

    private void updateTextAppearance() {
        if (mSelectMode) {
            if (isSelected()) {
                applyGradient();
            } else {
                // Use normal text color when not selected
                getPaint().setShader(null);
                setTextColor(mNormalTextColor);
            }
        } else {
            // Always apply gradient when not in select mode
            applyGradient();
        }
    }

    private void applyGradient() {
        if (mOrientation == 0) {
            //横向
            getPaint().setShader(new LinearGradient(0, (float) getHeight() / 2, getWidth(), (float) getHeight() / 2,
                    mColors,
                    mPositions,
                    Shader.TileMode.CLAMP));
        } else {
            //竖向
            getPaint().setShader(new LinearGradient((float) getWidth() / 2, 0, (float) getWidth() / 2, getHeight(),
                    mColors,
                    mPositions,
                    Shader.TileMode.CLAMP));
        }
    }
}