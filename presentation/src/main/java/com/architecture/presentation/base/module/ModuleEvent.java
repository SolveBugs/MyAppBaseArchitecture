package com.architecture.presentation.base.module;

import android.content.Context;

import org.json.JSONObject;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class ModuleEvent {
    private static final String TAG = "ModuleEvent";
    public static final int APP_INIT_BEGIN = 1;
    public static final int APP_INIT_FINISH = 2;

    public Observer<Integer> getAppInitObserver() {
        return new AppInitObserver();
    }

    /**
     * 处理 app 初始化
     */
    private class AppInitObserver implements Observer<Integer> {
        @Override
        public void onSubscribe(Disposable disposable) {
            // 不处理
        }

        @Override
        public void onNext(Integer integer) {
            if (integer == APP_INIT_BEGIN) {
                // 第一阶段
                onAppInit();
            } else if (integer == APP_INIT_FINISH) {
                // 第二阶段
                onAppAfterInit();
            }
        }

        @Override
        public void onError(Throwable throwable) {
            // 不会有错误
        }

        @Override
        public void onComplete() {
            // 不处理
        }
    }

    /**
     * 有推送时的回调 返回true 其它模块不再处理
     *
     * @param context     上下文件
     * @param name        推送名
     * @param jsonMessage 数据
     * @return 是否处理
     */
    protected abstract boolean onReceivePushMessage(Context context,
                                                    String name, JSONObject jsonMessage);

    /**
     * 第一阶段初始化 在 application 中onCreate中调用
     */
    protected abstract void onAppInit();

    /**
     * 第二阶段初始化 在 application 中onCreate中调用
     */
    protected abstract void onAppAfterInit();
}
