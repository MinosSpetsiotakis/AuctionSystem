import java.util.Timer;
import java.util.TimerTask;

public class AuctionTimer {
    static Timer timer;
    static int interval;

    public static void Timer(int secs) {
        timer = new Timer();
        int interval = secs;
        int delay = 1000;
        int period = 1000;
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                System.out.println(setInterval());
            }
        }, delay, period);
    }
    private static final int setInterval() {
        if (interval == 1)
            timer.cancel();
        return --interval;
    }
}
