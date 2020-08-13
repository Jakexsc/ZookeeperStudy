package com.xsc;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.recipes.cache.*;
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
//        curatorStudy.client.getData().usingWatcher(new MyCuratorWatch()).forPath("/super/xsc");

        // 为节点添加watch
//        final NodeCache nodeCache = new NodeCache(curatorStudy.client, "/super/xsc");
//        // 如果buildinitial为false 则初始化数据为空
//        nodeCache.start(true);
//        if (nodeCache.getCurrentData() != null) {
//            System.out.println("NodeCache初始化的数据为:" + new String(nodeCache.getCurrentData().getData()));
//        } else {
//            System.out.println("初始化数据为空");
//        }
//
//        nodeCache.getListenable().addListener(new NodeCacheListener() {
//            @Override
//            public void nodeChanged() throws Exception {
//                String data = new String(nodeCache.getCurrentData().getData());
//                System.out.println("节点路径为:" + nodeCache.getCurrentData().getPath() + ", 数据为:" + data);
//            }
//        });

        // cacheData: 设置缓存节点状态
        final PathChildrenCache pathChildrenCache = new PathChildrenCache(curatorStudy.client, "/super/xsc", true);
        /**
         * StartMode: 初始化的方式
         * NORMAL -> 异步初始化
         * POST_INITIALIZED_EVENT -> 异步初始化, 触发初始化事件
         * BUILD_INITIAL_CACHE -> 同步初始化
         */
        pathChildrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        System.out.println("子节点数据为:");
        pathChildrenCache.getCurrentData().forEach((ChildData childdata) -> {
            System.out.println(new String(childdata.getData()));
        });
        pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                // 初始化
                if (pathChildrenCacheEvent.getType().equals(PathChildrenCacheEvent.Type.INITIALIZED)) {
                    System.out.println("子节点初始化OK...");
                // 添加操作
                } else if (pathChildrenCacheEvent.getType().equals(PathChildrenCacheEvent.Type.CHILD_ADDED)) {
                    System.out.println("子节点添加数据:" + new String(pathChildrenCacheEvent.getData().getData()));
                    System.out.println("子节点路径为:" + pathChildrenCacheEvent.getData().getPath());
                // 更新操作
                } else if (pathChildrenCacheEvent.getType().equals(PathChildrenCacheEvent.Type.CHILD_UPDATED)) {
                    System.out.println("子节点已更新:" + new String(pathChildrenCacheEvent.getData().getData()));
                // 删除操作
                } else if (pathChildrenCacheEvent.getType().equals(PathChildrenCacheEvent.Type.CHILD_REMOVED)) {
                    System.out.println("子节点已经删除:" + pathChildrenCacheEvent.getData().getPath());
                }
            }
        });

        Thread.sleep(30000);
        curatorStudy.closeClient();
        boolean isStarted2 = curatorStudy.client.isStarted();
        System.out.println("当前客户端状态为: " + (isStarted2 ? "连接中" : "已关闭"));
    }
}
