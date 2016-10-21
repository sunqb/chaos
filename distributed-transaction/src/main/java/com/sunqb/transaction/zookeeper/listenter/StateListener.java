package com.sunqb.transaction.zookeeper.listenter;

import java.util.List;

/**
 * 状态变化监听器
 * Created by sunqingbiao on 2016/10/19.
 */
public interface StateListener {

    /**
     * 已断开连接状态
     */
    int DISCONNECTED = 0;
    /**
     * 已连接状态
     */
    int CONNECTED = 1;
    /**
     * 重新连接状态
     */
    int RECONNECTED = 2;

    void changed(int connected);
}
