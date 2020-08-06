package zkapi;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @author JakeXsc
 * @version 1.0
 * @date 2020/8/5 23:13
 */
public class ZkConnect implements Watcher {
    public static final Logger logger = LoggerFactory.getLogger(ZkConnect.class);
    public static final String zkServerPath = "192.168.37.128:2181";
    public static final String zkServerPathCluster = "192.168.37.128:2181,192.168.37.129:2181,192.168.37.130:2181";
    public static final Integer timeout = 5000;

    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String[] args) {
        try {
            /**
             * connectionString: 连接服务器的Ip地址，多个IP则是集群部署，可以在IP后加路径
             * sessionTimeout: 超时时间，心跳收不到则超时
             * watcher: 通知事件，如果有对应的事件触发则收到一个通知，如果不需要则设置为null
             * canBeReadOnly: 可读，当物理机关闭后，它还是可以收到数据（但是数据可能是老数据），但是不能写。不推荐使用
             * sessionId: 会话的Id
             * sessionPasswd: 会话密码
             * 当会话丢失后，可以根据会话Id和会话密码重新获取会话
             */
            ZooKeeper zooKeeper = new ZooKeeper(zkServerPathCluster, timeout, new ZkConnect());
            logger.warn("开始连接Zookeeper服务器");
            logger.warn("连接状态: {}", zooKeeper.getState());
            countDownLatch.await();
            logger.warn("连接状态: {}", zooKeeper.getState());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        logger.warn("接到Watch通知: {}", watchedEvent);
        if (Event.KeeperState.SyncConnected == watchedEvent.getState()) {
            countDownLatch.countDown();
        }
    }
}
