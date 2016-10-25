package com.sunqb.transaction.zookeeper.curator;

import com.sunqb.transaction.config.Config;
import com.sunqb.transaction.zookeeper.ZookeeperClient;
import com.sunqb.transaction.zookeeper.listenter.ChildrenListener;
import com.sunqb.transaction.zookeeper.listenter.StateListener;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.CuratorFrameworkFactory.Builder;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * ZookeeperClient的Curator实现类 Created by sunqingbiao on 2016/10/19.
 */
public class CuratorZookeeperClient implements ZookeeperClient {

    protected static final Logger logger = LoggerFactory.getLogger(CuratorZookeeperClient.class);

    private final CuratorFramework client;

    public CuratorZookeeperClient(String address) {
        // 重试策略
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(Config.ZK_BASESLEEP_TIME_MS, Config.ZK_MAX_RETRIES);

        // 构建器
        Builder builder =
                CuratorFrameworkFactory.builder().connectString(address).sessionTimeoutMs(Config.ZK_SESSION_TIMEOUT_MS)
                        .retryPolicy(retryPolicy).namespace(Config.ZK_DEFAULT_ROOT);

        // 授权信息
        // String authority = "authority";
        // if (null != authority && authority.length() > 0) {
        // builder.authorization("digest", authority.getBytes());

        client = builder.build();
        client.getConnectionStateListenable().addListener(new StateListener() {
            @Override
            public void stateChanged(CuratorFramework curatorFramework, ConnectionState connectionState) {
                if (connectionState == ConnectionState.LOST) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("断开连接");
                    }
                    // 重试策略重试后任然断开的情况，这里根据业务考虑是否需要重连
                    // while (true) {
                    // try {
                    // if (curatorFramework.getZookeeperClient().blockUntilConnectedOrTimedOut()) {
                    // curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                    // .forPath("/"+Config.ZK_DEFAULT_ROOT, "".getBytes("UTF-8"));
                    // break;
                    // }
                    // }catch (Exception e){
                    // logger.debug("zk重连异常，异常信息:"+e.getLocalizedMessage());
                    // }
                    // }
                }
                else if (connectionState == ConnectionState.CONNECTED) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("已连接");
                    }
                }
                else if (connectionState == ConnectionState.RECONNECTED) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("重新连接");
                    }
                }
            }
        });
        client.start();
    }

    /**
     * 返回zk连接客户端，用户调用方自定义处理
     * 
     * @return
     */
    public CuratorFramework getClient() {
        if (client != null) {
            return client;
        }
        return null;
    }

    /**
     * 新增节点(该方法只支持创建永久节点和临时节点)，并递归创建父节点
     * 
     * @param path 节点路径
     * @param ephemeral 是否为临时节点
     * @param data 数据
     */
    public void create(String path, boolean ephemeral, String data) {
        try {
            if (data == null || data.length() <= 0) {
                data = Config.ZK_DEFAULT_DATA;
            }
            client.create().creatingParentsIfNeeded()
                    .withMode(ephemeral ? CreateMode.EPHEMERAL : CreateMode.PERSISTENT).forPath(path, data.getBytes());
        }
        catch (Exception e) {
            logger.debug("zk创建节点异常，异常信息：" + e.getLocalizedMessage());
        }
    }

    /**
     * 删除节点
     * 
     * @param path 节点路径
     * @param cascade 是否级联删除
     */
    public void delete(String path, boolean cascade) {
        try {
            if (cascade) {
                client.delete().deletingChildrenIfNeeded().forPath(path);
            }
            else {
                client.delete().forPath(path);
            }
        }
        catch (Exception e) {
            logger.debug("zk删除节点异常，异常信息：" + e.getLocalizedMessage());
        }
    }

    /**
     * 获取子节点
     * 
     * @param path 节点路径
     * @return
     */
    public List<String> getChildren(String path) {
        try {
            return client.getChildren().forPath(path);
        }
        catch (Exception e) {
            logger.debug("zk获取子节点异常，异常信息：" + e.getLocalizedMessage());
        }
    }

    /**
     * 在某个节点上增加子节点变更监听
     * @param path 节点路径
     * @param listener 子节点变更监听器
     * @return
     */
    public List<String> addChildrenListener(String path, ChildrenListener listener) {
        return null;
    }

    public void removeChildrenListener(String path, ChildrenListener listener) {

    }

    public void addStateListener(StateListener listener) {

    }

    public void removeStateListener(StateListener listener) {

    }

    /**
     * 是否连接
     * @return
     */
    public boolean isConnected() {
        return client.getZookeeperClient().isConnected();
    }

    public void close() {

    }

    public String getAddress() {
        return null;
    }
}
