package com.cdsy.aichat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Objects;

public class BootCompletedReceiver extends BroadcastReceiver {

    private static final String ACTION_BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Objects.requireNonNull(intent.getAction()).equals(ACTION_BOOT_COMPLETED)) {
            //Intent newIntent = new Intent(context, SplashActivity.class);
            //newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //context.startActivity(newIntent);
        }
    }

}