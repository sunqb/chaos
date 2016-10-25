package com.sunqb.transaction.config;

import org.apache.commons.lang.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 配置信息和常量
 * Created by sunqingbiao on 2016/8/24.
 */
public class Config {

    /**
     * zk重试策略的间隔时间(ms)
     */
    public final static int ZK_BASESLEEP_TIME_MS = 1000;

    /**
     * zk最大重试次数
     */
    public final static int ZK_MAX_RETRIES = 3;

    /**
     * zk的session超时时间(ms)
     */
    public final static int ZK_SESSION_TIMEOUT_MS = 5000;

    /**
     * 根节点
     */
    public final static String ZK_DEFAULT_ROOT = "distributed-transaction";

    /**
     * 默认数据
     */
    public final static String ZK_DEFAULT_DATA = "init";

}
