package voctrals.study.zookeeper.asyncznode;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @author lei.liu
 * @since 19-3-26
 */
public class ZkNodeStarter {

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        ZooKeeper zk = new ZooKeeper("localhost:2181", 5000, (e) -> {
            if (e.getState() == Watcher.Event.KeeperState.SyncConnected) {
                countDownLatch.countDown();
            }
        });
        countDownLatch.await();

        AsyncCallback.StringCallback stringCallback = new AsyncCallback.StringCallback() {
            @Override
            public void processResult(int i, String s, Object o, String s1) {
                // i 状态码，0(OK), -4(ConnectionLos), -110(NodeExists), -112(SessionExpired)
                // s 路径参数值
                // o 传递的context
                // s1 在服务器端形成的具体路径
                System.out.println("i = " + i + ", s = " + s + ", o = " + o + ", s1 = " + s1);
            }
        };

        zk.create("/ZkNodeStarter", null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL, stringCallback, "abc");
        zk.create("/ZkNodeStarter", null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL, stringCallback, "abc");
        zk.create("/ZkNodeStarter", null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL, stringCallback, "abc");

        Thread.sleep(Integer.MAX_VALUE);

    }

}
