package com.xsc.cluster;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.RetryNTimes;

/**
 * @author JakeXsc
 * @version 1.0
 * @date 2020/8/13 22:45
 */
public class Client2 {
    /**
     * 服务器路径
     */
    private static final String serverPath = "192.168.37.128:2181";
    private CuratorFramework curatorFramework = null;
    private static String NODE = "/super/xsc";
    private static String CHILD_NODE = "/redis-conf";

    public Client2() {
        /**
         * RetryNTimes连接策略
         * 重连3次，每次间隔5秒
         */
        RetryNTimes retryNTimes = new RetryNTimes(3, 5000);

        curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(serverPath)
                .sessionTimeoutMs(10000)
                .retryPolicy(retryNTimes)
                .namespace("workspace")
                .build();
        curatorFramework.start();
    }

    /**
     * 关闭客户端
     */
    public void closeClient() {
        if (this.curatorFramework != null) {
            this.curatorFramework.close();
        }
    }

    public static void main(String[] args) throws Exception {
        Client2 client1 = new Client2();
        boolean started = client1.curatorFramework.isStarted();
        System.out.println("客户端状态为:" + (started ? "连接中" : "已经关闭"));

        // 建立watch
        PathChildrenCache pathChildrenCache = new PathChildrenCache(client1.curatorFramework, NODE, true);
        // 开启异步初始化
        pathChildrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        pathChildrenCache.getCurrentData().forEach((ChildData childdata) -> {
            System.out.println("子节点的路径为：" + new String(childdata.getPath()));
        });

        pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                if (pathChildrenCacheEvent.getType().equals(PathChildrenCacheEvent.Type.INITIALIZED)) {
                    System.out.println("子节点初始化OK...");
                } else if (pathChildrenCacheEvent.getType().equals(PathChildrenCacheEvent.Type.CHILD_UPDATED)) {
                    if(pathChildrenCacheEvent.getData().getPath().equals(NODE + CHILD_NODE)) {
                        System.out.println("监听到配置发生变化，节点路径为：" + pathChildrenCacheEvent.getData().getPath());

                        // 读取节点数据
                        String jsonConfig = new String(pathChildrenCacheEvent.getData().getData());
                        System.out.println("节点:" + NODE + "的数据为:" + jsonConfig);

                        // 从json转换配置
                        RedisConf redisConf = null;
                        if (!StringUtils.isBlank(jsonConfig)) {
                            redisConf = JSON.parseObject(jsonConfig, RedisConf.class);
                        }

                        if (redisConf != null) {
                            String type = redisConf.getType();
                            String url = redisConf.getUrl();
                            String remark = redisConf.getRemark();
                            if ("add".equals(type)) {
                                System.out.println("监听到新增的配置，准备下载");
                                Thread.sleep(500);
                                System.out.println("开始下载新的配置文件，路径为:" + url);
                                Thread.sleep(1000);
                                System.out.println("下载成功，准备添加到项目");
                                // TODO 拷贝到项目...
                                Thread.sleep(500);
                                System.out.println("已经添加到项目");
                            } else if ("update".equals(type)) {
                                System.out.println("监听到更新的配置，准备下载");
                                Thread.sleep(500);
                                System.out.println("开始下载新的配置文件，路径为:" + url);
                                Thread.sleep(1000);
                                System.out.println("下载成功，准备添加到项目");
                                Thread.sleep(100);
                                System.out.println("删除项目原有的配置文件...");
                                // TODO 删除原来的配置文件
                                Thread.sleep(100);
                                System.out.println("准备拷贝到项目目录");
                                // TODO 拷贝到项目...
                                Thread.sleep(500);
                                System.out.println("已经添加到项目");
                            } else if (type.equals("delete")) {
                                System.out.println("监听到需要删除的配置文件");
                                System.out.println("删除项目原来的配置文件");
                            }
                            // TODO 看情况重启服务
                        }
                    }
                }
            }
        });


        Thread.sleep(30000);
        client1.closeClient();
        boolean started2 = client1.curatorFramework.isStarted();
        System.out.println("客户端状态为:" + (started2 ? "连接中" : "已经关闭"));

    }
}
