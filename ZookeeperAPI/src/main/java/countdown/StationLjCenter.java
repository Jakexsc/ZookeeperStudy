package countdown;

import java.util.concurrent.CountDownLatch;

/**
 * @author JakeXsc
 * @version 1.0
 * @date 2020/8/9 21:42
 */
public class StationLjCenter extends DangerCenter {

    public StationLjCenter(CountDownLatch countDownLatch) {
        super(countDownLatch, "Lj调度中心");
    }

    @Override
    public void check() {
        System.out.println("正在检查[" + this.getStation() + "]...");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("检查[" + this.getStation() + "]完毕,可以发车~...");
    }
}
