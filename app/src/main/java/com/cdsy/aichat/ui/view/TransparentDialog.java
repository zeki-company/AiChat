package com.cdsy.aichat.ui.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowManager;
import androidx.annotation.NonNull;
import com.cdsy.aichat.R;
import java.util.Objects;

//透明Dialog
public abstract class TransparentDialog extends Dialog {
    int resId;

    public TransparentDialog(@NonNull Context context, int resId) {
        super(context, R.style.FullScreenDialog);
        this.resId = resId;
        setOwnerActivity((Activity) context);
    }

    public TransparentDialog(@NonNull Context context, int resId, int themeResId) {
        super(context, themeResId);
        this.resId = resId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(resId);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(true);
        //设置window背景，默认的背景会有Padding值，不能全屏。当然不一定要是透明，你可以设置其他背景，替换默认的背景即可。
        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //一定要在setContentView之后调用，否则无效
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getWindow().setGravity(Gravity.CENTER);
    }


    @Override
    public void show() {
        super.show();
    }

}