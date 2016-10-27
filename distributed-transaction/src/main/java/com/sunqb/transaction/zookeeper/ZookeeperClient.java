package com.sunqb.transaction.zookeeper;

import com.sunqb.transaction.zookeeper.listenter.ChildrenListener;
import com.sunqb.transaction.zookeeper.listenter.StateListener;

import java.net.URL;
import java.util.List;

/**
 * zookeeper操作的接口
 * Created by sunqingbiao on 2016/10/18.
 */
public interface ZookeeperClient {

    /**
     * 新增节点
     * @param path 节点路径
     * @param ephemeral 是否为临时节点
     * @param data 数据
     */
    void create(String path, boolean ephemeral, String data) throws Exception;

    /**
     * 删除节点
     * @param path 节点路径
     * @param cascade 是否级联删除
     */
    void delete(String path, boolean cascade) throws Exception;

    /**
     * 获取指定路径下的子节点列表
     * @param path 节点路径
     * @return List<String>
     */
    List<String> getChildren(String path) throws Exception;

    /**
     * 为指定路径添加子节点变更监听器
     * @param path 节点路径
     * @param listener 子节点变更监听器
     * @return
     */
    void addChildrenListener(String path, ChildrenListener listener);

    /**
     * 移除指定节点的指定监听器
     * @param path 节点路径
     * @param listener 监听器
     */
    void removeChildrenListener(String path, ChildrenListener listener);

    /**
     * 添加ZooKeeper状态监听器
     * @param listener 状态监听器
     */
    void addStateListener(StateListener listener);

    /**
     * 移除ZooKeeper状态监听器
     * @param listener
     */
    void removeStateListener(StateListener listener);

    /**
     * 查看是否已连接ZooKeeper
     * @return
     */
    boolean isConnected();

    /**
     * 关闭ZooKeeper连接
     */
    void close();

    /**
     * 获取ZooKeeper连接信息
     * @return
     */
    String getAddress();

    /**
     * 查看节点是否存在
     * @param path 节点路径
     * @return
     */
    boolean isExistsNode(String path) throws Exception;

    /**
     * 获取节点的值
     * @param path 节点路径
     * @param codec 编码
     * @return
     */
    String getNodeValue(String path, String codec) throws Exception;

    /**
     * 更新节点的值
     * @param path 节点路径
     * @param newData 新的数据
     */
    void updateNodeValue(String path, String newData) throws Exception;
}
