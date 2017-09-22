package com.architecture.presentation.base.module;

import com.architecture.presentation.base.ui.BaseActivity;

import java.util.Map;



public interface ModuleBaseInterface {

    /**
     * 模块名。 用于数据库中记录版本信息
     *
     * @return 字符串
     */
    String getName();

    /**
     * 模块事件相关处理功能
     *
     * @return ModuleEvent
     */
    ModuleEvent getDomain();

    /**
     * 模块界面信息 换成字符串为了可以通过网络找开任意界面
     *
     * @return 键值对
     */
    Map<String, Class<? extends BaseActivity>> getRouters();

    /**
     * 判断该模块是否需要同步数据
     * @param syncType 同步模块
     * @return true/false
     */
    int needSync(int syncType);

}
