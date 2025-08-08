package com.cdsy.aichat.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.cdsy.aichat.R;

import java.util.List;

/**
 * 渐变色控件，可设置四色位置
 */
public class GradientCard extends FrameLayout {
    private float round;

    private int[] mColors;
    private float[] mPositions;
    private RectF mBackGroundRect;
    private LinearGradient backGradient;
    private int mOrientation;
    //默认画笔
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mPaintText = new Paint();
    List<Integer> colorlist;

    public GradientCard(Context context, AttributeSet attrs) {
        super(context, attrs);
        //获取自定义属性（可先设置默认四种颜色）
        /*colorStart = Color.parseColor("#FFDDBB");
        colorAfterStart = Color.parseColor("#FFC0CD");
        colorBeforeEnd = Color.parseColor("#B5BBFF");
        colorEnd = Color.parseColor("#AADCFF");*/
        //也可通过attr进行xml文件内的自定义设置
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.GradientCard);
            int colorsResId = typedArray.getResourceId(R.styleable.GradientCard_gradient_card_colors, R.array.bonus_gradient_colors);
            mColors = getColorsFromResource(context, colorsResId);

            int positionsResId = typedArray.getResourceId(R.styleable.GradientCard_gradient_card_positions, R.array.bonus_gradient_positions);
            mPositions = getPositionsFromResource(context, positionsResId);
            round = dp2Px(typedArray.getInt(R.styleable.GradientCard_gradient_card_round, 12));

            mOrientation = typedArray.getColor(R.styleable.GradientCard_gradient_card_orientation, 0);
        }
        //设置抗锯齿
        mPaint.setAntiAlias(true);
        //设置防抖动
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaintText.setAntiAlias(true);
        mPaintText.setDither(true);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBackGroundRect = new RectF(0, 0, w, h);
        if (mOrientation == 0) {
            backGradient = new LinearGradient(0, (float) getHeight() / 2, w, (float) getHeight() / 2, mColors, mPositions, Shader.TileMode.CLAMP);
        } else {
            backGradient = new LinearGradient((float) getWidth() / 2, 0, (float) getWidth() / 2, getHeight(), mColors, mPositions, Shader.TileMode.CLAMP);
        }

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
                    Color.argb(255, 168, 153, 243),
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

    @Override
    protected void dispatchDraw(Canvas canvas) {
        // 先绘制渐变背景
        if (mBackGroundRect != null && backGradient != null) {
            mPaint.setShader(backGradient);
            //绘制背景 圆角矩形
            canvas.drawRoundRect(mBackGroundRect, round, round, mPaint);
        }
        // 然后绘制子视图
        super.dispatchDraw(canvas);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 不在这里绘制，因为我们需要在子视图之前绘制背景
        // 所以使用 dispatchDraw 方法
    }

    /**
     * 将dp值转换为px值
     *
     * @param dpValue dp值
     * @return px值
     */
    private float dp2Px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return dpValue * scale + 0.5f;
    }
}
