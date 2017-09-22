package com.architecture.presentation.event;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;


public class EventReceiver extends BroadcastReceiver {
    public static final String ACTION_KEY = "action_key";
    public static final int BROADCAST_ACTION_NONE = 0;

    private OnReceiveListener onReceiveListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            int action = intent.getIntExtra(ACTION_KEY, BROADCAST_ACTION_NONE);

            if (action != BROADCAST_ACTION_NONE && onReceiveListener != null
                    && action > EventID.BASE && action < EventID.MAX) {
                Bundle bundle = intent.getExtras();
                onReceiveListener.onReceive(action, bundle);
            }
        }
    }

    public void setOnReceiveListener(OnReceiveListener onReceiveListener) {
        this.onReceiveListener = onReceiveListener;
    }

    public static void send(@NonNull Context context, int action, Bundle bundle) {
        Intent intent = new Intent();
        intent.putExtra(ACTION_KEY, action);
        if (bundle != null) {
            intent.putExtras(bundle);
        }

        intent.setAction(EventReceiver.class.getName());

        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(context);
        manager.sendBroadcast(intent);
    }

    public void register(@NonNull Context context) {
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(context);
        IntentFilter filter = new IntentFilter();
        filter.addAction(EventReceiver.class.getName());
        manager.registerReceiver(this, filter);
    }
}
