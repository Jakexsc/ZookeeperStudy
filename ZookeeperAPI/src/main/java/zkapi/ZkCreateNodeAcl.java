package zkapi;

import commons.AclUtils;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * 创建ACL节点
 *
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

    public static void main(String[] args) throws KeeperException, InterruptedException, NoSuchAlgorithmException {
        ZkCreateNodeAcl zkCreateNodeAcl = new ZkCreateNodeAcl(zkServerPath);
        // 所有人都可以访问
//        zkCreateNodeAcl.createNode("/test", "test".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE);

        /**
         * 自定义用户权限
         */
//        List<ACL> aclList = new ArrayList<>();
//        Id xscTest1 = new Id("digest", AclUtils.getDigestUserPwd("xsc1:xsc"));
//        Id xscTest2 = new Id("digest", AclUtils.getDigestUserPwd("xsc2:xsc"));
//        aclList.add(new ACL(ZooDefs.Perms.ALL, xscTest1));
//        aclList.add(new ACL(ZooDefs.Perms.READ, xscTest2));
//        aclList.add(new ACL(ZooDefs.Perms.DELETE | ZooDefs.Perms.CREATE, xscTest2));
//        zkCreateNodeAcl.createNode("/test/test1", "test".getBytes(), aclList);

        zkCreateNodeAcl.getZookeeper().addAuthInfo("digest", "xsc2:xsc".getBytes());
        zkCreateNodeAcl.createNode("/test/test1/test111", "test".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL);
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
