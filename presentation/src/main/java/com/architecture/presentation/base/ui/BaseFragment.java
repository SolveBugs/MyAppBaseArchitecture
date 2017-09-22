/**
 * Copyright (C) 2014 android10.org. All rights reserved.
 *
 * @author Fernando Cejas (the android10 coder)
 */
package com.architecture.presentation.base.ui;

import android.app.Activity;
import android.app.Fragment;

import com.architecture.presentation.app.AndroidApplication;
import com.architecture.presentation.app.di.HasComponent;
import com.architecture.presentation.common.utils.analytics.EventStatistics;
import com.architecture.presentation.navigation.Navigator;

public abstract class BaseFragment extends Fragment {

    protected Navigator navigator;

    @SuppressWarnings("unchecked")
    protected <C> C getComponent(Class<C> componentType) {
        return componentType.cast(((HasComponent<C>) getActivity()).getComponent());
    }

    protected Navigator getNavigator() {
        Activity activity = getActivity();
        if (activity instanceof BaseActivity) {
            return ((BaseActivity) activity).navigator;
        }
        return null;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        navigator = getNavigator();
    }

    @Override
    public void onStart() {
        super.onStart();

        EventStatistics.onPageStart(getClass().getName());
    }

    @Override
    public void onStop() {
        super.onStop();

        EventStatistics.onPageEnd(getClass().getName());
    }


    public AndroidApplication getApp() {
        final AndroidApplication app = (AndroidApplication) getActivity().getApplication();
        return app;
    }

}

