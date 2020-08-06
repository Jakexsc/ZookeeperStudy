package zkapi;

import org.apache.zookeeper.AsyncCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 异步删除节点 - 推荐使用
 *
 * @author JakeXsc
 * @version 1.0
 * @date 2020/8/7 0:29
 */
public class DeleteCallback implements AsyncCallback.VoidCallback {
    private static final Logger logger = LoggerFactory.getLogger(DeleteCallback.class);

    @Override
    public void processResult(int rc, String path, Object ctx) {
        logger.warn("删除节点: {} 成功", path);
        logger.warn(ctx.toString());
    }
}
