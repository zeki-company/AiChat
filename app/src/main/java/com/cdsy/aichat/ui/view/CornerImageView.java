package com.cdsy.aichat.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import com.cdsy.aichat.R;

public class CornerImageView extends androidx.appcompat.widget.AppCompatImageView {
    /**
     * 圆形模式
     */
    private static final int MODE_CIRCLE = 1;
    /**
     * 普通模式
     */
    private static final int MODE_NONE = 0;
    /**
     * 圆角模式
     */
    private static final int MODE_ROUND = 2;
    /**
     * 顶部圆角模式
     */
    private static final int MODE_HALF_TOP_ROUND = 3;
    /**
     * 底部圆角模式
     */
    private static final int MODE_HALF_BOTTOM_ROUND = 4;
    private Paint mPaint;
    private int currMode = 0;
    /**
     * 圆角半径
     */
    private int currRound = dp2px(10);

    public CornerImageView(Context context) {
        super(context);
        initViews();
    }

    public CornerImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CornerImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        obtainStyledAttrs(context, attrs, defStyleAttr);
        initViews();
    }

    private void obtainStyledAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView, defStyleAttr, 0);
        currMode = a.hasValue(R.styleable.RoundImageView_type) ? a.getInt(R.styleable.RoundImageView_type, MODE_NONE) : MODE_NONE;
        currRound = a.hasValue(R.styleable.RoundImageView_radius) ? a.getDimensionPixelSize(R.styleable.RoundImageView_radius, currRound) : currRound;
        a.recycle();
    }

    private void initViews() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /*
         * 当模式为圆形模式的时候，我们强制让宽高一致
         */
        if (currMode == MODE_CIRCLE) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            int result = Math.min(getMeasuredHeight(), getMeasuredWidth());
            setMeasuredDimension(result, result);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable mDrawable = getDrawable();
        Matrix mDrawMatrix = getImageMatrix();
        if (mDrawable == null) {
            return; // couldn't resolve the URI
        }

        if (mDrawable.getIntrinsicWidth() == 0 || mDrawable.getIntrinsicHeight() == 0) {
            return;     // nothing to draw (empty bounds)
        }

        if (mDrawMatrix == null && getPaddingTop() == 0 && getPaddingLeft() == 0) {
            mDrawable.draw(canvas);
        } else {
            final int saveCount = canvas.getSaveCount();
            canvas.save();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                if (getCropToPadding()) {
                    final int scrollX = getScrollX();
                    final int scrollY = getScrollY();
                    canvas.clipRect(scrollX + getPaddingLeft(), scrollY + getPaddingTop(),
                            scrollX + getRight() - getLeft() - getPaddingRight(),
                            scrollY + getBottom() - getTop() - getPaddingBottom());
                }
            }
            canvas.translate(getPaddingLeft(), getPaddingTop());
            if (currMode == MODE_CIRCLE) {//当为圆形模式的时候
                Bitmap bitmap = drawable2Bitmap(mDrawable);
                mPaint.setShader(new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2, mPaint);
            } else if (currMode == MODE_ROUND) {//当为圆角模式的时候
                Bitmap bitmap = drawable2Bitmap(mDrawable);
                mPaint.setShader(new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                canvas.drawRoundRect(new RectF(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(), getHeight() - getPaddingBottom()),
                        currRound, currRound, mPaint);
            } else if (currMode == MODE_HALF_TOP_ROUND) {
                //此处处理的PaddingLeft有问，不使用Padding即可，否则需自行实现padding后的效果
                Bitmap bitmap = drawable2Bitmap(mDrawable);
                mPaint.setShader(new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                Path mPath = new Path();

                mPath.moveTo(getPaddingLeft(), getPaddingTop() + currRound);
                RectF rectTopLeft = new RectF(getPaddingLeft(), getPaddingTop(), getPaddingLeft() + currRound * 2, getPaddingTop() + currRound * 2);
                mPath.arcTo(rectTopLeft, 180, 90);

                mPath.lineTo(getPaddingLeft() + getWidth() - getPaddingRight() - currRound, getPaddingTop());
                RectF rectTopRight = new RectF(getPaddingLeft() + getWidth() - getPaddingRight() - 2 * currRound, getPaddingTop(), getPaddingLeft() + getWidth() - getPaddingRight(), getPaddingTop() + currRound * 2);
                mPath.arcTo(rectTopRight, 270, 90);
                mPath.lineTo(getPaddingLeft() + getWidth() - getPaddingRight(), getPaddingTop() + getHeight());
                mPath.lineTo(getPaddingLeft(), getPaddingTop() + getHeight());
                mPath.close();
                canvas.drawPath(mPath, mPaint);
            } else if (currMode == MODE_HALF_BOTTOM_ROUND) {
                //此处处理的PaddingLeft有问，不使用Padding即可，否则需自行实现padding后的效果 todo
                Bitmap bitmap = drawable2Bitmap(mDrawable);
                mPaint.setShader(new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                Path mPath = new Path();

                mPath.moveTo(getPaddingLeft(), getPaddingTop());
                mPath.lineTo(getPaddingLeft() + getWidth() - getPaddingRight(), getPaddingTop());
                mPath.lineTo(getPaddingLeft() + getWidth() - getPaddingRight(), getPaddingTop() + getHeight() - getPaddingBottom() - currRound);

                //右下角圆角
                RectF rectBottomRight = new RectF(getPaddingLeft() + getWidth() - getPaddingRight() - 2 * currRound, getPaddingTop() + getHeight() - getPaddingBottom() - 2 * currRound, getPaddingLeft() + getWidth() - getPaddingRight(), getPaddingTop() + getHeight() - getPaddingBottom());
                mPath.arcTo(rectBottomRight, 0, 90);

                //左下角圆角
                RectF rectBottomLeft = new RectF(getPaddingLeft(), getPaddingTop() + getHeight() - getPaddingBottom() - 2 * currRound, getPaddingLeft() + 2 * currRound, getPaddingTop() + getHeight() - getPaddingBottom());
                mPath.arcTo(rectBottomLeft, 90, 90);

                mPath.close();
                canvas.drawPath(mPath, mPaint);
            } else {
                if (mDrawMatrix != null) {
                    canvas.concat(mDrawMatrix);
                }
                mDrawable.draw(canvas);
            }
            canvas.restoreToCount(saveCount);
        }
    }

    /**
     * drawable转换成bitmap
     */
    private Bitmap drawable2Bitmap(Drawable drawable) {
        if (drawable == null) {
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        //根据传递的scaleType获取matrix对象，设置给bitmap
        Matrix matrix = getImageMatrix();
        if (matrix != null) {
            canvas.concat(matrix);
        }
        drawable.draw(canvas);
        return bitmap;
    }

    private int dp2px(float value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, getResources().getDisplayMetrics());
    }
}

