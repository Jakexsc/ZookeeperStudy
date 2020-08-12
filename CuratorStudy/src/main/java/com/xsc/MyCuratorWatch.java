package com.xsc;

import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.zookeeper.WatchedEvent;

/**
 * @author JakeXsc
 * @version 1.0
 * @date 2020/8/13 0:09
 */
public class MyCuratorWatch implements CuratorWatcher {
    @Override
    public void process(WatchedEvent watchedEvent) {
        System.out.println("触发watch路径为 :" + watchedEvent.getPath());
    }
}
