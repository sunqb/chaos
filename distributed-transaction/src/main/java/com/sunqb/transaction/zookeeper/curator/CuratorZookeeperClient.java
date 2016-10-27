package com.sunqb.transaction.zookeeper.curator;

import com.sunqb.transaction.config.Config;
import com.sunqb.transaction.zookeeper.ZookeeperClient;
import com.sunqb.transaction.zookeeper.listenter.ChildrenListener;
import com.sunqb.transaction.zookeeper.listenter.StateListener;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.CuratorFrameworkFactory.Builder;
import org.apache.curator.framework.api.ExistsBuilder;
import org.apache.curator.framework.api.GetDataBuilder;
import org.apache.curator.framework.api.SetDataBuilder;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
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

        client = builder.build();
        client.start();
    }

    /**
     * 新增节点(该方法只支持创建永久节点和临时节点)，并递归创建父节点
     * 
     * @param path 节点路径
     * @param ephemeral 是否为临时节点
     * @param data 数据
     */
    @Override
    public void create(String path, boolean ephemeral, String data) throws Exception {
        try {
            if (data == null || data.length() <= 0) {
                data = Config.ZK_DEFAULT_DATA;
            }
            client.create().creatingParentsIfNeeded()
                    .withMode(ephemeral ? CreateMode.EPHEMERAL : CreateMode.PERSISTENT).forPath(path, data.getBytes());
        }
        catch (Exception e) {
            logger.debug("zk创建节点异常，异常信息：" + e.getLocalizedMessage());
            throw e;
        }
    }

    /**
     * 删除节点
     * 
     * @param path 节点路径
     * @param cascade 是否级联删除
     */
    @Override
    public void delete(String path, boolean cascade) throws Exception {
        try {
            if (cascade) {
                client.delete().deletingChildrenIfNeeded().withVersion(-1).forPath(path);
            }
            else {
                client.delete().withVersion(-1).forPath(path);
            }
        }
        catch (Exception e) {
            logger.debug("zk删除节点异常，异常信息：" + e.getLocalizedMessage());
            throw e;
        }
    }

    /**
     * 获取子节点
     * 
     * @param path 节点路径
     * @return
     */
    @Override
    public List<String> getChildren(String path) throws Exception {
        try {
            return client.getChildren().forPath(path);
        }
        catch (Exception e) {
            logger.debug("zk获取子节点异常，异常信息：" + e.getLocalizedMessage());
            throw e;
        }
    }

    /**
     * 在某个节点上增加子节点变更监听
     * 
     * @param path 节点路径
     * @param listener 子节点变更监听器
     * @return
     */
    @Override
    public void addChildrenListener(String path, ChildrenListener listener) {
        PathChildrenCache cache = new PathChildrenCache(client, path, true);
        cache.getListenable().addListener(listener);
    }

    /**
     * 移除某个节点上面的子节点监听器
     * 
     * @param path 节点路径
     * @param listener 监听器
     */
    @Override
    public void removeChildrenListener(String path, ChildrenListener listener) {
        PathChildrenCache cache = new PathChildrenCache(client, path, true);
        cache.getListenable().removeListener(listener);
    }

    /**
     * 添加状态监听器（需要测试是否必须在client.start()之前添加）
     * 
     * @param listener 状态监听器
     */
    @Override
    public void addStateListener(StateListener listener) {
        client.getConnectionStateListenable().addListener(listener);
    }

    /**
     * 移除状态监听器
     * 
     * @param listener
     */
    @Override
    public void removeStateListener(StateListener listener) {
        client.getConnectionStateListenable().removeListener(listener);
    }

    /**
     * 是否连接
     * 
     * @return
     */
    @Override
    public boolean isConnected() {
        return client.getZookeeperClient().isConnected();
    }

    /**
     * 关闭客户端
     */
    @Override
    public void close() {
        client.close();
    }

    /**
     * 返回client的连接信息
     * 
     * @return
     */
    @Override
    public String getAddress() {
        return client.getZookeeperClient().getCurrentConnectionString();
    }

    /**
     * 查看节点是否存在
     * 
     * @param path 节点路径
     * @return
     */
    @Override
    public boolean isExistsNode(String path) throws Exception {
        ExistsBuilder existsBuilder = client.checkExists();
        Stat stat = null;
        try {
            stat = existsBuilder.forPath(path);
            if (stat != null) {
                return true;
            }
            return false;
        }
        catch (Exception e) {
            logger.debug("zk查看节点是否存在异常，异常信息：" + e.getLocalizedMessage());
            throw e;
        }
    }

    /**
     * 获取节点的数据
     * 
     * @param path 节点路径
     * @param codec 编码
     * @return
     */
    @Override
    public String getNodeValue(String path, String codec) throws Exception {
        GetDataBuilder dataBuilder = client.getData();
        Stat stat = new Stat();
        dataBuilder.storingStatIn(stat);
        byte[] bytes = null;
        try {
            bytes = dataBuilder.forPath(path);
            if (codec == null || codec.length() <= 0) {
                return new String(bytes);
            }
            return new String(bytes, codec);
        }
        catch (Exception e) {
            logger.debug("zk获取节点的数据异常，异常信息：" + e.getLocalizedMessage());
            throw e;
        }
    }

    /**
     * 修改节点的数据
     * 
     * @param path 节点路径
     * @param newData 新的数据
     */
    @Override
    public void updateNodeValue(String path, String newData) throws Exception {
        SetDataBuilder setDataBuilder = client.setData();
        try {
            setDataBuilder.withVersion(-1).forPath(path, newData.getBytes());
        }
        catch (Exception e) {
            logger.debug("zk修改节点的数据异常，异常信息：" + e.getLocalizedMessage());
            throw e;
        }
    }

    /**
     * 返回zk连接客户端，用户调用方自定义处理(可能为空)
     * 
     * @return
     */
    public CuratorFramework getClient() {
        return client;
    }

}
