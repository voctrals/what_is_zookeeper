package voctrals.study.zookeeper.clastermanager;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author lei.liu
 * @since 19-3-25
 */
public class AppRegister {
    private CountDownLatch connectedSemaphore = new CountDownLatch(1);
    private ZooKeeper zooKeeper;
    private Object lock = new Object();
    private String ip;

    private static String rootConfig = "/AppCluster";

    public AppRegister(String ip) {
        this.ip = ip;
        this.zooKeeper = connectZookeeper();
    }

    public ZooKeeper connectZookeeper() {
        synchronized (lock) {
            if (zooKeeper == null) {
                try {
                    zooKeeper = new ZooKeeper(
                            "localhost:2181",
                            5000,
                            new Watcher() {
                                public void process(WatchedEvent event) {
                                    if (Event.KeeperState.SyncConnected == event.getState()) {
                                        // 连接成功了，创建成功了，异步的
                                        if (Event.EventType.None == event.getType() && null == event.getPath()) {
                                            try {
                                                connectedSemaphore.countDown();
                                                zooKeeper.getChildren(rootConfig, true);
                                                // ACL：Access Control List，访问控制列表
                                                // CreateMode.EPHEMERAL是必须的，一旦断开连接，就删除掉这个节点
                                                zooKeeper.create(rootConfig + "/" + ip, ip.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
                                            } catch (KeeperException e) {
                                                e.printStackTrace();
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        } else if (Event.EventType.NodeChildrenChanged == event.getType()) {
                                            try {
                                                List<String> childrenList = zooKeeper.getChildren(rootConfig, true);
                                                System.out.println("节点变化了：" + childrenList);
                                                Collections.sort(childrenList);
                                                System.out.println("我是master了：" + childrenList.get(0));
                                            } catch (KeeperException e) {
                                                e.printStackTrace();
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    } else {
                                        System.out.println(event);
                                    }
                                }
                            });
                    connectedSemaphore.await();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
        return zooKeeper;
    }
}
