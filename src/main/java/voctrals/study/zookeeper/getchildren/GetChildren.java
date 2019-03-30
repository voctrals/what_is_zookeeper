package voctrals.study.zookeeper.getchildren;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;

/**
 * @author lei.liu
 * @since 19-3-28
 */
public class GetChildren {

    public static void main(String[] args) throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        ZooKeeper zk = new ZooKeeper("localhost:2181", 5000, (e) -> {
            if (e.getState() == Watcher.Event.KeeperState.SyncConnected) {
                countDownLatch.countDown();
            }

            if (e.getType() == Watcher.Event.EventType.NodeChildrenChanged) {
                System.out.println(e.getPath() + " changed~");
            }

            System.out.println(e.getType());
        });
        countDownLatch.await();

        zk.create("/GetChildren", null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

        zk.getChildren("/GetChildren", true);

        zk.create("/GetChildren/a", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);

        zk.getChildren("/GetChildren", true); // 客户端需要重复注册watcher，watcher是一次性的呢

        zk.create("/GetChildren/b", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);

        Thread.sleep(Integer.MAX_VALUE);
    }

}
