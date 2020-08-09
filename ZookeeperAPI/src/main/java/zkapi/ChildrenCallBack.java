package zkapi;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.data.Stat;

import java.util.List;

/**
 * @author JakeXsc
 * @version 1.0
 * @date 2020/8/9 22:58
 */
public class ChildrenCallBack implements AsyncCallback.ChildrenCallback {
    @Override
    public void processResult(int rc, String path, Object ctx, List<String> list) {
        for (String children : list) {
            System.out.println("子节点数据:" + children);
        }
        System.out.println("子节点路径:" + path);
        System.out.println("子节点结果:" + ctx);
    }
}
