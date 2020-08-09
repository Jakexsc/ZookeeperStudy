package zkapi;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @author JakeXsc
 * @version 1.0
 * @date 2020/8/9 22:24
 */
public class ZkGetNodeData implements Watcher {
    private ZooKeeper zooKeeper = null;
    private static final String zkServerPath = "192.168.37.128:2181";
    private static final Integer timeout = 10000;
    private static Stat stat = new Stat();
    private static CountDownLatch countDown = new CountDownLatch(1);

    public ZooKeeper getZooKeeper() {
        return zooKeeper;
    }

    public void setZooKeeper(ZooKeeper zooKeeper) {
        this.zooKeeper = zooKeeper;
    }

    public ZkGetNodeData() {
    }

    public ZkGetNodeData(String connectString) {
        try {
            zooKeeper = new ZooKeeper(connectString, timeout, new ZkGetNodeData());
        } catch (IOException e) {
            e.printStackTrace();
            if (zooKeeper != null) {
                try {
                    zooKeeper.close();
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws KeeperException, InterruptedException {
        ZkGetNodeData zkGetNodeData = new ZkGetNodeData(zkServerPath);
        /**
         * 参数
         * path: 路径
         * watch: 是否创建watch -> boolean类型
         * stat: 状态
         */
        byte[] data = zkGetNodeData.getZooKeeper().getData("/xsc", true, stat);
        String result = new String(data);
        System.out.println("当前值:" + result);
        countDown.await();
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        try {
            if (watchedEvent.getType() == Event.EventType.NodeDataChanged) {
                ZkGetNodeData zkGetNodeData = new ZkGetNodeData(zkServerPath);
                byte[] data = zkGetNodeData.getZooKeeper().getData("/xsc", false, stat);
                String result = new String(data);
                System.out.println("更改后的值: " + result);
                System.out.println("版本号变化version: " + stat.getVersion());
                countDown.countDown();
            } else if (watchedEvent.getType() == Event.EventType.NodeCreated) {

            } else if (watchedEvent.getType() == Event.EventType.NodeChildrenChanged) {

            } else if (watchedEvent.getType() == Event.EventType.NodeDeleted) {

            }
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
