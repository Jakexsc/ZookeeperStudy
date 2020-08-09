package zkapi;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author JakeXsc
 * @version 1.0
 * @date 2020/8/9 22:24
 */
public class ZkGetChildrenNodeData implements Watcher {
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

    public ZkGetChildrenNodeData() {
    }

    public ZkGetChildrenNodeData(String connectString) {
        try {
            zooKeeper = new ZooKeeper(connectString, timeout, new ZkGetChildrenNodeData());
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
        ZkGetChildrenNodeData zkGetChildrenNodeData = new ZkGetChildrenNodeData(zkServerPath);
//        zkGetChildrenNodeData.getZooKeeper().getChildren("/xsc", true).forEach((String result) -> {
//            System.out.println("子节点数据:" + result);
//        });

        String ctx = "{'callback':'ChildrenCallback'}";
        /**
         * ChildrenCallBack2比ChildrenCallBack多了一个Stat
         */
//        zkGetChildrenNodeData.getZooKeeper().getChildren("/xsc", true, new ChildrenCallBack(), ctx);
        zkGetChildrenNodeData.getZooKeeper().getChildren("/xsc", true, new ChildrenCallBack2(), ctx);

        countDown.await();
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        try {
            if (watchedEvent.getType() == Event.EventType.NodeDataChanged) {
                System.out.println("NodeDataChanged");
            } else if (watchedEvent.getType() == Event.EventType.NodeCreated) {
                System.out.println("NodeCreated");
            } else if (watchedEvent.getType() == Event.EventType.NodeChildrenChanged) {
                System.out.println("NodeChildrenChanged");
                ZkGetChildrenNodeData zkGetChildrenNodeData = new ZkGetChildrenNodeData(zkServerPath);
                zkGetChildrenNodeData.getZooKeeper().getChildren(watchedEvent.getPath(), false).forEach(System.out::println);
                countDown.countDown();
            } else if (watchedEvent.getType() == Event.EventType.NodeDeleted) {
                System.out.println("NodeDeleted");
            }
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
