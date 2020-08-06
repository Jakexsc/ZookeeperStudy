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
 * @date 2020/8/6 0:21
 */
public class ZkSession implements Watcher {
    private static final Logger logger = LoggerFactory.getLogger(ZkSession.class);
    private static final String zkServerPath = "192.168.37.128:2181";
    private static final Integer timeout = 10000;
    private static CountDownLatch countDownLatch = new CountDownLatch(2);

    public static void main(String[] args) {
        try {
            ZooKeeper zooKeeper = new ZooKeeper(zkServerPath, timeout, new ZkSession());
            logger.warn("开始连接Zookeeper服务器");
            logger.warn("连接状态: {}", zooKeeper.getState());
            Thread.sleep(10000);
            logger.warn("连接状态: {}", zooKeeper.getState());
            long sessionId = zooKeeper.getSessionId();
            byte[] sessionPasswd = zooKeeper.getSessionPasswd();
            Thread.sleep(200);
            logger.warn("开始会连重连...");
            ZooKeeper zkSession = new ZooKeeper(zkServerPath, timeout, new ZkSession(), sessionId, sessionPasswd);
            logger.warn("重新连接状态zkSession: {}", zkSession.getState());
            // 线程阻塞，等所有子线程执行完毕再执行
            countDownLatch.await();
            logger.warn("重新连接状态zkSession: {}", zkSession.getState());
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
