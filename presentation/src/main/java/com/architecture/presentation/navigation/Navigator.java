package com.architecture.presentation.navigation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.architecture.domain.utils.Logger;
import com.architecture.presentation.base.ui.BaseActivity;
import com.architecture.presentation.exception.RouterPathExistException;

import java.util.HashMap;
import java.util.Map;


public class Navigator implements Parcelable {
    private static final String TAG = "Navigator";

    private Context context;
    private HashMap<String, Class<? extends BaseActivity>> activityMaps = new HashMap<>();

    public Navigator(Context context) {
        this.context = context;
    }


    protected Navigator(Parcel in) {
    }

    public static final Creator<Navigator> CREATOR = new Creator<Navigator>() {
        @Override
        public Navigator createFromParcel(Parcel in) {
            return new Navigator(in);
        }

        @Override
        public Navigator[] newArray(int size) {
            return new Navigator[size];
        }
    };

    public boolean open(@NonNull String url, int flag) {
        return open(url, null, flag, null);
    }

    public boolean open(Activity parent, @NonNull String url, int flag) {
        return open(url, null, flag, parent);
    }

    public boolean open(@NonNull String url, Bundle bundle) {
        return open(url, bundle, 0, null);
    }

    public boolean open(Activity parent, @NonNull String url, Bundle bundle) {
        return open(url, bundle, 0, parent);
    }


    public boolean open(@NonNull String url) {
        return open(url, null, 0, null);
    }

    public boolean open(Activity parent, @NonNull String url) {
        return open(url, null, 0, parent);
    }

    public boolean open(@NonNull String url, Bundle bundle, int flags, Activity parent) {
        Class<? extends BaseActivity> lookClass = activityMaps.get(url);
        if (lookClass != null) {
            Intent intent = new Intent(context, lookClass);
            if (flags != 0) {
                intent.setFlags(flags);
            }

            if (bundle != null) {
                intent.putExtras(bundle);
            }

            Context startContext = parent;
            if (startContext == null) {
                startContext = context;
            }

            if (!(startContext instanceof Activity)) {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            startContext.startActivity(intent);
            return true;
        }

        Logger.d(TAG, "open failure: " + url);
        return false;
    }

    public boolean openActivityForResult(
            @NonNull Activity parentActivity, String url, int requestCode, Bundle param) {
        Class<? extends BaseActivity> lookClass = activityMaps.get(url);
        if (lookClass != null) {
            Intent intent = new Intent(context, lookClass);
            if (param != null) {
                intent.putExtras(param);
            }
            parentActivity.startActivityForResult(intent, requestCode);
            return true;
        }

        return false;
    }

    public boolean openActivityForResult(
            @NonNull Fragment fragment, String url, int requestCode, Bundle param) {
        Class<? extends BaseActivity> lookClass = activityMaps.get(url);
        if (lookClass != null) {
            Intent intent = new Intent(context, lookClass);
            if (param != null) {
                intent.putExtras(param);
            }
            fragment.startActivityForResult(intent, requestCode);
            return true;
        }

        return false;
    }

    /**
     * 添加导航的路径信息
     *
     * @param maps 其它模块的信息
     */
    public void add(@NonNull Map<String, Class<? extends BaseActivity>> maps) {
        for (Map.Entry<String, Class<? extends BaseActivity>> entry : maps.entrySet()) {
            if (activityMaps.containsKey(entry.getKey())) {
                throw new RouterPathExistException("router " + entry.getValue() + " exist");
            }
            activityMaps.put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }
}
