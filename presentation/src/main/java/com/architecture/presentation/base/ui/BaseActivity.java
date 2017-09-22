package com.architecture.presentation.base.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.architecture.domain.utils.Logger;
import com.architecture.presentation.app.AndroidApplication;
import com.architecture.presentation.app.di.components.ApplicationComponent;
import com.architecture.presentation.common.utils.analytics.EventStatistics;
import com.architecture.presentation.navigation.Navigator;

import javax.inject.Inject;


public class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";
    @Inject
    public Navigator navigator;

    private boolean show;
    private boolean active, created;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        AppManager.getAppManager().addActivity(this);
        created = true;
        getApplicationComponent().inject(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        show = true;
        active = true;
        EventStatistics.onResume(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        EventStatistics.onPageStart(getClass().getName());
    }

    @Override
    protected void onPause() {
        super.onPause();
        show = false;
        EventStatistics.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        active = false;
        EventStatistics.onPageEnd(getClass().getName());
    }

    protected ApplicationComponent getApplicationComponent() {
        return ((AndroidApplication) getApplication()).getApplicationComponent();
    }

    protected void addFragment(@IdRes int containerViewId, Fragment fragment) {
        final FragmentTransaction fragmentTransaction = this.getFragmentManager().beginTransaction();
        fragmentTransaction.add(containerViewId, fragment);
        fragmentTransaction.commit();
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                hideSoftInput();
            }
            return super.dispatchTouchEvent(ev);
        }

        try {
            // 必不可少，否则所有的组件都不会有TouchEvent了
            if (getWindow().superDispatchTouchEvent(ev)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return onTouchEvent(ev);
    }


    public void hideSoftInput() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null)
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 替换某个layout 的Fragment
     *
     * @param containerViewId layout的resId
     * @param fragment        要添加的fragment对象
     */
    protected void replaceFragment(@IdRes int containerViewId, Fragment fragment) {
        final FragmentTransaction fragmentTransaction = this.getFragmentManager().beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

        /**
         * 将当前fragment添加到返回栈，
         * tag 命名规则为 layout的resId+"_"+当前栈内fragment个数（即当前fragment入栈后的下标）
         */
        Logger.d(TAG, "栈内个数:" + this.getFragmentManager().getBackStackEntryCount());
        fragmentTransaction.addToBackStack(containerViewId + "_" + this.getFragmentManager().getBackStackEntryCount());

        fragmentTransaction.replace(containerViewId, fragment);

        fragmentTransaction.commitAllowingStateLoss();
    }

    /**
     * 返回到BackStack某个下标指定的Fragment
     *
     * @param containerViewId layout的resId
     * @param index           第几个Fragment
     */
    protected void popToFragmentIndex(@IdRes int containerViewId, @NonNull int index) {
        Logger.d(TAG, "回到第" + index + "个Fragment");
        this.getFragmentManager().popBackStack(containerViewId + "_" + index, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
        created = false;
    }


    public boolean isShow() {
        return show;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isCreated() {
        return created;
    }

    protected static Handler handler = new Handler();
    protected Runnable runnable;

}
