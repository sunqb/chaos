package com.sunqb.transaction.config;

import org.apache.commons.lang.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by sunqingbiao on 2016/8/24.
 */
public class Config {

    private final static Logger logger = LoggerFactory.getLogger(Config.class);

    private final static String DEFAULT_ROOT = "distributed-transaction";

    private final String root;

    // private final Set<String> anyServices = new ConcurrentHashSet<String>();
    //
    // private final ConcurrentMap<URL, ConcurrentMap<NotifyListener, ChildListener>> zkListeners = new
    // ConcurrentHashMap<URL, ConcurrentMap<NotifyListener, ChildListener>>();

    private final CuratorFramework zkClient;

    public Config(String connectionString, String rootPath) {
        if(StringUtils.isBlank(rootPath)){
            root = DEFAULT_ROOT;
        }else{
            root = rootPath;
        }

        zkClient = CuratorFrameworkFactory.builder().connectString(connectionString)
                .sessionTimeoutMs(5000).retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
    }


}
