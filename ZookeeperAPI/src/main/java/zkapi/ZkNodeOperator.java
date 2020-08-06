package zkapi;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.zookeeper.ZooDefs.Ids;

import javax.security.auth.callback.Callback;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author JakeXsc
 * @version 1.0
 * @date 2020/8/6 23:00
 */
public class ZkNodeOperator implements Watcher {
    private ZooKeeper zooKeeper = null;
    private static final String zkServerPath = "192.168.37.128:2181";
    private static final Integer timeout = 10000;
    private static final Logger logger = LoggerFactory.getLogger(ZkNodeOperator.class);
    public ZkNodeOperator() {

    }

    public ZkNodeOperator(String zkServerPath) {
        try {
            this.zooKeeper = new ZooKeeper(zkServerPath, timeout, new ZkNodeOperator());
        } catch (IOException e) {
            e.printStackTrace();
            if (this.zooKeeper != null) {
                try {
                    this.zooKeeper.close();
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException, KeeperException {
        ZkNodeOperator zkNodeOperator = new ZkNodeOperator(zkServerPath);
        // 创建zk节点
//        zkNodeOperator.createZkNode("/testnode", "testnode".getBytes(), Ids.OPEN_ACL_UNSAFE);
        // 修改节点
        Stat stat = zkNodeOperator.getZooKeeper().setData("/testnode", "xsc".getBytes(), 0);
        logger.warn("版本号为: {}", stat.getVersion());
    }

    private void createZkNode(String path, byte[] data, List<ACL> openAclUnsafe) throws InterruptedException, KeeperException {
        /**
         * 同步或异步创建节点，都不支持子节点的递归创建，异步有一个Callback函数
         * 参数：
         * path: 创建的路径
         * data: 存储的数据 -> Byte[]
         * acl: 权限策略 -> Ids.OPEN.ACL.UNSAFE --> world:anyone:cdrwa
         *                 CREATOR_ALL_ACL --> auth:user:password:cdrwa
         * CreateMode: 节点类型，是个枚举 -> PERSISTENT: 持久节点
         *                             -> PERSISTENT_SEQUENTIAL: 持久顺序节点
         *                             -> EPHEMERAL: 临时节点
         *                             -> EPHEMERAL_SEQUENTIAL: 临时顺序节点
         */
//        String result = null;
//        // 同步创建节点
//        result = zooKeeper.create(path, data, openAclUnsafe, CreateMode.EPHEMERAL);
//        logger.warn("创建节点: {} 成功, {}", result, new Date());
//        Thread.sleep(10000);
        String ctx = "{'create':'success'}";
        // 异步创建节点
        zooKeeper.create(path, data, openAclUnsafe, CreateMode.PERSISTENT, new CreateCallBack(), ctx);
        Thread.sleep(5000);
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        logger.warn("收到通知: {}", watchedEvent.getState());
    }

    public ZooKeeper getZooKeeper() {
        return zooKeeper;
    }

    public void setZooKeeper(ZooKeeper zooKeeper) {
        this.zooKeeper = zooKeeper;
    }
}
