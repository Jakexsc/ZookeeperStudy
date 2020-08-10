package com.xsc;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.retry.*;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;

/**
 * @author JakeXsc
 * @version 1.0
 * @date 2020/8/10 22:48
 */
public class CuratorStudy {
    public static final String zkServerPath = "192.168.37.128:2181";
    public CuratorFramework client = null;

    public CuratorStudy() {
        /**
         * curator连接zookeeper策略 -> ExponentialBackoffRetry
         * baseSleepTimeMs: 初始化sleep时间
         * maxRetries: 最大重试次数
         * maxSleepMs: 最大重试时间
         */
        ExponentialBackoffRetry retry = new ExponentialBackoffRetry(1000, 5);

        /**
         * curator连接zookeeper策略 -> RetryNTimes
         * n重试的次数
         * sleepMsBetweenRetries: 每次重试间隔的时间
         */
//        new RetryNTimes(3, 5000);

        /**
         * curator连接zookeeper的策略 -> RetryOneTime
         * sleepMsBetweenRetries: 每次重试间隔
         */
//        new RetryOneTime(3000);

        /**
         * 永久重试 -> 不推荐使用
         */
//        new RetryForever(retryintervalMs);

        /**
         * curator连接zookeeper的策略 -> RetryUntilElapsed
         * maxElapsedTimeMs: 最大重试时间
         * sleepMsBetweenRetries: 每次重试间隔
         */
//        new RetryUntilElapsed(5000, 3000);

        client = CuratorFrameworkFactory.builder()
                .connectString(zkServerPath)
                .sessionTimeoutMs(10000)
                .retryPolicy(retry)
                // 命名空间
                .namespace("workspace")
                .build();
        client.start();
    }

    public void closeClient() {
        if(client != null) {
            this.client.close();
        }
    }

    public static void main(String[] args) throws Exception {
        CuratorStudy curatorStudy = new CuratorStudy();
        boolean isStarted1 = curatorStudy.client.isStarted();
        System.out.println("当前客户端状态为: " + (isStarted1 ? "连接中" : "已关闭"));

        // 创建节点
        curatorStudy.client.create()
                // 递归创建
                .creatingParentsIfNeeded()
                // 创建节点类型
                .withMode(CreateMode.PERSISTENT)
                // acl权限
                .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                // 遍历去创建
                .forPath("/super/xsc", "xsc".getBytes());

        Thread.sleep(3000);
        curatorStudy.closeClient();
        boolean isStarted2 = curatorStudy.client.isStarted();
        System.out.println("当前客户端状态为: " + (isStarted2 ? "连接中" : "已关闭"));
    }
}
