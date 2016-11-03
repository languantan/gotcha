package com.languantan.gotcha.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by ryantan on 3/11/16.
 */
public class CameraIntent extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_USER_PRESENT) || intent.getAction().equals(Intent.ACTION_POWER_DISCONNECTED)){
            Intent mainActivity = new Intent(context, MainActivity.class);
            mainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mainActivity);
        }
    }
}
