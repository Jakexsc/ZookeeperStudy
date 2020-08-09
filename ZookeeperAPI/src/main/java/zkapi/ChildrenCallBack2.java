package zkapi;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.data.Stat;

import java.util.List;

/**
 * @author JakeXsc
 * @version 1.0
 * @date 2020/8/9 23:14
 */
public class ChildrenCallBack2 implements AsyncCallback.Children2Callback {
    @Override
    public void processResult(int i, String path, Object ctx, List<String> list, Stat stat) {
        for (String children : list) {
            System.out.println("子节点数据:" + children);
        }
        System.out.println("子节点路径:" + path);
        System.out.println("子节点结果:" + ctx);
        System.out.println(stat.toString());
    }
}
