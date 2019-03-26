package voctrals.study.zookeeper.asynczk;

import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @author lei.liu
 * @since 19-3-26
 */
public class ZKStarter {


    public static void main(String[] args) throws IOException, InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        ZooKeeper zk = new ZooKeeper("localhost:2181", 5000, (e) -> {
            if (e.getState() == Watcher.Event.KeeperState.SyncConnected) {
                countDownLatch.countDown();
                System.out.println("回调：");
            }
        });
        // 连接中
        System.out.println(zk.getState());
        countDownLatch.await();
        // 已连接
        System.out.println(zk.getState());
    }

}
