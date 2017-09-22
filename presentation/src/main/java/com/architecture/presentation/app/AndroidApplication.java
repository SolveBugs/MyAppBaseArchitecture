package com.architecture.presentation.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.StrictMode;

import com.architecture.data.common.logger.LogUtils;
import com.architecture.domain.utils.Logger;
import com.architecture.presentation.BuildConfig;
import com.architecture.presentation.app.di.components.ApplicationComponent;
import com.architecture.presentation.app.di.components.DaggerApplicationComponent;
import com.architecture.presentation.app.di.modules.ApplicationModule;
import com.architecture.presentation.base.module.ModuleBaseInterface;
import com.architecture.presentation.base.module.ModuleEvent;
import com.architecture.presentation.base.ui.BaseActivity;
import com.architecture.presentation.common.utils.SharePreferenceManager;
import com.architecture.presentation.event.EventID;
import com.architecture.presentation.event.EventReceiver;
import com.architecture.presentation.navigation.Navigator;
import com.squareup.leakcanary.LeakCanary;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.subjects.PublishSubject;

/**
 * Android Main Application
 */
public class AndroidApplication extends Application {
    private static final String TAG = "AndroidApplication";

    private List<ModuleBaseInterface> moduleList;

    private ApplicationComponent applicationComponent;
    private SharePreferenceManager sharePreferenceManager;
    private int activityCount = 0;
    AudioManager audioManager;
    int maxVolume;

    @Override
    public void onCreate() {
        super.onCreate();

        this.initializeStrictMode();
        this.initializeActivityCallback();

        this.initializeInjector();
        this.initializeModList();
        this.initializeNavigator();
        this.initializeModules();
        sharePreferenceManager = SharePreferenceManager.newInstance(this);
        Thread.setDefaultUncaughtExceptionHandler(AppException.getAppExceptionHandler());

        final IntentFilter filter = new IntentFilter();
        // 屏幕灭屏广播
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        // 屏幕亮屏广播
        filter.addAction(Intent.ACTION_SCREEN_ON);

        //添加电量监测
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    }


    private void initializeInjector() {
        if (this.applicationComponent != null) {
            return;
        }

        LogUtils.init(this);

        this.applicationComponent =
                DaggerApplicationComponent.builder()
                        .applicationModule(new ApplicationModule(this))
                        .build();

    }

    public ApplicationComponent getApplicationComponent() {
        if (this.applicationComponent == null) {
            initializeInjector();
            initializeModList();
        }
        return this.applicationComponent;
    }


    private void initializeModList() {
        synchronized (AndroidApplication.class) {
            if (moduleList == null) {
                if (this.applicationComponent == null) {
                    initializeInjector();
                }
                moduleList = new ArrayList<>();
            }
        }
    }

    public List<ModuleBaseInterface> getModuleList() {
        if (moduleList == null) {
            initializeModList();
        }

        return moduleList;
    }

    private void initializeLeakDetection() {
        if (BuildConfig.DEBUG) {
            LeakCanary.install(this);
        }
    }

    private void initializeModules() {
        PublishSubject<Integer> source = PublishSubject.create();
        List<ModuleBaseInterface> mods = getModuleList();
        for (ModuleBaseInterface mod : mods) {
            ModuleEvent event = mod.getDomain();
            if (event == null) {
                continue;
            }

            Observer<Integer> observer = event.getAppInitObserver();
            if (observer == null) {
                continue;
            }
            source.subscribe(observer);
        }

        // init after
        source.onNext(ModuleEvent.APP_INIT_BEGIN);

        source.onNext(ModuleEvent.APP_INIT_FINISH);
    }

    private void initializeStrictMode() {
        if (!BuildConfig.DEBUG) {
            return;
        }

        StrictMode.ThreadPolicy.Builder threadPolicyBuilder =
                new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog();
        StrictMode.VmPolicy.Builder vmPolicyBuilder =
                new StrictMode.VmPolicy.Builder().detectAll().penaltyLog();

        threadPolicyBuilder.penaltyFlashScreen();
        StrictMode.setThreadPolicy(threadPolicyBuilder.build());
        StrictMode.setVmPolicy(vmPolicyBuilder.build());
    }

    private void initializeNavigator() {
        Navigator navigator = applicationComponent.navigator();
        List<ModuleBaseInterface> mods = getModuleList();
        for (ModuleBaseInterface mod : mods) {
            Map<String, Class<? extends BaseActivity>> maps = mod.getRouters();
            if (maps == null) {
                continue;
            }

            navigator.add(maps);
        }
    }

    private void initializeActivityCallback() {
        registerActivityLifecycleCallbacks(new MyActivityLifecycleCallbacks());
    }

    private class MyActivityLifecycleCallbacks implements ActivityLifecycleCallbacks {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        }

        @Override
        public void onActivityStarted(Activity activity) {
            if (activityCount == 0) {
                EventReceiver.send(activity, EventID.APP_BECOME_FOREGROUND, null);
            }
            activityCount++;
            Logger.d(TAG, "onActivityStarted: " + activityCount);
        }

        @Override
        public void onActivityResumed(Activity activity) {
            activityCount++;
            Logger.d(TAG, "onActivityResumed: " + activityCount);
        }

        @Override
        public void onActivityPaused(Activity activity) {
            activityCount--;
            Logger.d(TAG, "onActivityPaused: " + activityCount);
        }

        @Override
        public void onActivityStopped(Activity activity) {
            activityCount--;
            Logger.d(TAG, "onActivityStopped: " + activityCount);
            if (activityCount == 0) {
                EventReceiver.send(activity, EventID.APP_INTO_BACKGROUND, null);
            }
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            // 不处理
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            // 不处理
        }

    }
}