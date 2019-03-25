package voctrals.study.zookeeper.clastermanager;

/**
 * @author lei.liu
 * @since 19-3-25
 */
public class AppMonitor {
    public static void main(String[] args) throws InterruptedException {
        addHost();
        Thread.sleep(Integer.MAX_VALUE);
    }

    private static void addHost() {
        new Thread(new Runnable() {
            public void run() {
                new AppRegister("192.168.1.1");
            }
        }).start();

        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(2000);
                    new AppRegister("192.168.1.2");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(4000);
                    new AppRegister("192.168.1.3");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
