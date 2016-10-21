package com.sunqb.transaction.zookeeper.listenter;

import java.util.List;

/**
 * 子节点监听器接口
 * Created by sunqingbiao on 2016/10/19.
 */
public interface ChildrenListener {

    void changed(String path, List<String> children);
}
