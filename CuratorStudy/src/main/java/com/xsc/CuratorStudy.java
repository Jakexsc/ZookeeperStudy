package com.xsc;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.retry.*;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Stat;

import java.util.List;

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
//        curatorStudy.client.create()
//                // 递归创建
//                .creatingParentsIfNeeded()
//                // 创建节点类型
//                .withMode(CreateMode.PERSISTENT)
//                // acl权限
//                .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
//                // 遍历去创建
//                .forPath("/super/xsc", "xsc".getBytes());

        // 修改节点
//        curatorStudy.client.setData().withVersion(0).forPath("/super/xsc", "xsc1".getBytes());

        // 删除节点
//        curatorStudy.client.delete()
//                // 如果删除失败，会在后台继续删除，直到删除成功
//                .guaranteed()
//                // 如果有子节点则删除
//                .deletingChildrenIfNeeded()
//                .withVersion(1)
//                .forPath("/super/xsc");

        // 查询节点数据
//        Stat stat = new Stat();
//        byte[] bytes = curatorStudy.client.getData().storingStatIn(stat).forPath("/super/xsc");
//        System.out.println("节点/super/xsc的数据为: " + new String(bytes));
//        System.out.println("该节点的版本号为: " + stat.getVersion());

        // 查询子节点
//        System.out.print("开始打印子节点:");
//        curatorStudy.client.getChildren().forPath("/super")
//                .forEach((String children) -> {
//                    System.out.println(children);
//                });

        // 判断节点是否存在 不存在则为空
//        Stat stat = curatorStudy.client.checkExists().forPath("/super/xsc1");
//        System.out.println(stat);

        // usingWatcher 监听只会触发一次，监听完毕后就销毁
        curatorStudy.client.getData().usingWatcher(new MyCuratorWatch()).forPath("/super/xsc");


        Thread.sleep(10000);
        curatorStudy.closeClient();
        boolean isStarted2 = curatorStudy.client.isStarted();
        System.out.println("当前客户端状态为: " + (isStarted2 ? "连接中" : "已关闭"));
    }
}
