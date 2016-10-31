package com.sunqb.transaction.mq;

/**
 * 消息队列接口
 * Created by sunqingbiao on 2016/10/27.
 */
public interface MessageQueue {

    /**
     * 发送消息（这里只需要支持string）
     * @param data
     */
    void send(String data);
}
