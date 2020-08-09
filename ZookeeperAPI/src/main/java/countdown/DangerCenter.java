package countdown;

import java.util.concurrent.CountDownLatch;

/**
 * @author JakeXsc
 * @version 1.0
 * @date 2020/8/9 21:35
 */
public abstract class DangerCenter implements Runnable {
    private CountDownLatch countDownLatch;
    private String station;
    private boolean ok;

    public DangerCenter(CountDownLatch countDownLatch, String station) {
        this.countDownLatch = countDownLatch;
        this.station = station;
    }

    @Override
    public void run() {
        try {
            check();
            this.ok = true;
        } catch (Exception e) {
            e.printStackTrace();
            this.ok = false;
        } finally {
            if (this.countDownLatch != null) {
                countDownLatch.countDown();
            }
        }
    }

    public CountDownLatch getCountDownLatch() {
        return countDownLatch;
    }

    public void setCountDownLatch(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    /**
     * 检查危化品车
     * 蒸罐
     * 汽油
     * 轮胎
     * gps
     * ...
     */
    public abstract void check();
}
