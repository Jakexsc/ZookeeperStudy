package zkapi;

import org.apache.zookeeper.AsyncCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * @author JakeXsc
 * @version 1.0
 * @date 2020/8/6 23:28
 */
public class CreateCallBack implements AsyncCallback.StringCallback {
    private static final Logger logger = LoggerFactory.getLogger(CreateCallBack.class);
    @Override
    public void processResult(int rc, String path, Object ctx, String name) {
        logger.warn("创建节点: {} 成功, {}", path, new Date());
        logger.warn((String) ctx);
    }
}
