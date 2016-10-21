package com.sunqb.transaction.zookeeper.curator;

import com.sunqb.transaction.zookeeper.ZookeeperClient;
import com.sunqb.transaction.zookeeper.listenter.ChildrenListener;
import com.sunqb.transaction.zookeeper.listenter.StateListener;

import java.net.URL;
import java.util.List;

/**
 * ZookeeperClient的Curator实现类
 * Created by sunqingbiao on 2016/10/19.
 */
public class CuratorZookeeperClient implements ZookeeperClient{

    public void create(String path, boolean ephemeral) {

    }

    public void delete(String path, boolean cascade) {

    }

    public List<String> getChildren(String path) {
        return null;
    }

    public List<String> addChildrenListener(String path, ChildrenListener listener) {
        return null;
    }

    public void removeChildrenListener(String path, ChildrenListener listener) {

    }

    public void addStateListener(StateListener listener) {

    }

    public void removeStateListener(StateListener listener) {

    }

    public boolean isConnected() {
        return false;
    }

    public void close() {

    }

    public URL getURL() {
        return null;
    }
}
