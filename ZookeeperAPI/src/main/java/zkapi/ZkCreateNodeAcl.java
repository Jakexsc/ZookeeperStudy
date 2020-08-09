package zkapi;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author JakeXsc
 * @version 1.0
 * @date 2020/8/9 23:35
 */
public class ZkCreateNodeAcl implements Watcher {
    private static final String zkServerPath = "192.168.37.128:2181";
    public ZooKeeper zookeeper = null;
    private static final Integer timeout = 15000;

    public ZkCreateNodeAcl() {
    }

    public ZkCreateNodeAcl(String connection) {
        try {
            this.zookeeper = new ZooKeeper(connection, timeout, new ZkCreateNodeAcl());
        } catch (IOException e) {
            e.printStackTrace();
            if (this.zookeeper != null) {
                try {
                    this.zookeeper.close();
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws KeeperException, InterruptedException {
        ZkCreateNodeAcl zkCreateNodeAcl = new ZkCreateNodeAcl(zkServerPath);
        zkCreateNodeAcl.createNode("/test", "test".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE);
    }

    private void createNode(String path, byte[] bytes, ArrayList<ACL> acls) throws KeeperException, InterruptedException {
        String result = zookeeper.create(path, bytes, acls, CreateMode.PERSISTENT);
        System.out.println("节点创建成功:" + result);
    }

    @Override
    public void process(WatchedEvent watchedEvent) {

    }

    public ZooKeeper getZookeeper() {
        return zookeeper;
    }

    public void setZookeeper(ZooKeeper zookeeper) {
        this.zookeeper = zookeeper;
    }
}
