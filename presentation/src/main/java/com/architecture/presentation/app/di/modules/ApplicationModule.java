package com.architecture.presentation.app.di.modules;

import android.content.Context;

import com.architecture.data.common.net.ApiConnection;
import com.architecture.data.common.net.ApiConnectionImp;
import com.architecture.data.executor.JobExecutor;
import com.architecture.domain.executor.PostExecutionThread;
import com.architecture.domain.executor.ThreadExecutor;
import com.architecture.presentation.app.AndroidApplication;
import com.architecture.presentation.app.UIThread;
import com.architecture.presentation.navigation.Navigator;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {
    private final AndroidApplication application;

    public ApplicationModule(AndroidApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Context provideApplicationContext() {
        return this.application;
    }

    @Provides
    @Singleton
    ThreadExecutor provideThreadExecutor(JobExecutor jobExecutor) {
        return jobExecutor;
    }

    @Provides
    @Singleton
    PostExecutionThread providePostExecutionThread(UIThread uiThread) {
        return uiThread;
    }

    @Provides
    @Singleton
    Navigator provideNavigator(Context context) {
        return new Navigator(context);
    }

    @Provides
    @Singleton
    ApiConnection provideApiConnection(ApiConnectionImp apiConnectionImp) {
        return apiConnectionImp;
    }

}
