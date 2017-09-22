package com.architecture.presentation.app;

import android.app.admin.DeviceAdminReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Wei on 2017/5/8.
 */

public class MyDeviceAdminReceiver extends DeviceAdminReceiver {
    public static ComponentName getCn(Context context){
        return new ComponentName(context, MyDeviceAdminReceiver.class);
    }

    @Override
    public void onEnabled(Context context, Intent intent) {
        // TODO Auto-generated method stub
        super.onEnabled(context, intent);
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        // TODO Auto-generated method stub
        super.onDisabled(context, intent);
    }
}
