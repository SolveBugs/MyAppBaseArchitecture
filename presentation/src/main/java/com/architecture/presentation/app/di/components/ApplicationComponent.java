package com.architecture.presentation.app.di.components;

import android.content.Context;

import com.architecture.data.common.net.ApiConnection;
import com.architecture.domain.executor.PostExecutionThread;
import com.architecture.domain.executor.ThreadExecutor;
import com.architecture.presentation.app.di.modules.ApplicationModule;
import com.architecture.presentation.base.ui.BaseActivity;
import com.architecture.presentation.navigation.Navigator;

import javax.inject.Singleton;

import dagger.Component;


@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    void inject(BaseActivity baseActivity);

    //Exposed to sub-graphs.
    Context context();

    Navigator navigator();

    ThreadExecutor threadExecutor();

    PostExecutionThread postExecutionThread();

    ApiConnection apiConnection();

}


