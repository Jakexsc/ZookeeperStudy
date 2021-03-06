package countdown;

import java.util.concurrent.CountDownLatch;

/**
 * @author JakeXsc
 * @version 1.0
 * @date 2020/8/9 21:42
 */
public class StationXscCenter extends DangerCenter {

    public StationXscCenter(CountDownLatch countDownLatch) {
        super(countDownLatch, "xsc调度中心");
    }

    @Override
    public void check() {
        System.out.println("正在检查[" + this.getStation() + "]...");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("检查[" + this.getStation() + "]完毕,可以发车~...");
    }
}
