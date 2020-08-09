package countdown;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * 检查调度中心
 *
 * @author JakeXsc
 * @version 1.0
 * @date 2020/8/9 21:53
 */
public class CheckStartUp {
    private static List<DangerCenter> stationList;
    private static CountDownLatch countDownLatch;

    public CheckStartUp() {
    }

    public static boolean checkAllStations() throws InterruptedException {
        // 初始化三个调度站
        countDownLatch = new CountDownLatch(3);

        // 把三个站点添加到List
        stationList = new ArrayList<>();
        stationList.add(new StationXscCenter(countDownLatch));
        stationList.add(new StationCxbCenter(countDownLatch));
        stationList.add(new StationLjCenter(countDownLatch));
        Executor executor = Executors.newFixedThreadPool(stationList.size());
        for (DangerCenter dangerCenter : stationList) {
            executor.execute(dangerCenter);
        }

        countDownLatch.await();
        for (DangerCenter dangerCenter : stationList) {
            if (!dangerCenter.isOk()) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) throws InterruptedException {
        boolean result = checkAllStations();
        System.out.println("监控中心检查结果为:" + result);
    }
}
