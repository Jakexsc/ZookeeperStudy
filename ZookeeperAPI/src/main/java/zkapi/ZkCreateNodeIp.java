package zkapi;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * 测试ACL ip
 *
 * @author JakeXsc
 * @version 1.0
 * @date 2020/8/9 23:35
 */
public class ZkCreateNodeIp implements Watcher {
    private static final String zkServerPath = "192.168.37.128:2181";
    public ZooKeeper zookeeper = null;
    private static final Integer timeout = 15000;

    public ZkCreateNodeIp() {
    }

    public ZkCreateNodeIp(String connection) {
        try {
            this.zookeeper = new ZooKeeper(connection, timeout, new ZkCreateNodeIp());
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

    public static void main(String[] args) throws KeeperException, InterruptedException, NoSuchAlgorithmException {
        ZkCreateNodeIp zkCreateNodeIp = new ZkCreateNodeIp(zkServerPath);
        List<ACL> aclIp = new ArrayList<>();
        Id id1 = new Id("ip", "192.168.1.103");
        aclIp.add(new ACL(ZooDefs.Perms.ALL, id1));
        zkCreateNodeIp.createNode("/test/ip2", "iptest".getBytes(), aclIp);
        zkCreateNodeIp.getZookeeper().setData("/test/ip2", "123456".getBytes(), 0);
    }

    private void createNode(String path, byte[] bytes, List<ACL> acls) throws KeeperException, InterruptedException {
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
