package voctrals.study.zookeeper.basic;

import org.apache.zookeeper.*;

/**
 * getData，getChildren，exists方法调用的时候指定是否观察节点
 *
 * @author lei.liu
 * @since 19-3-25
 */
public class Basic {
    public static void main(String[] args) throws Exception {
        ZooKeeper zk = new ZooKeeper(
                "localhost:2181",
                5000,
                // 监控所有被触发的事件
                // 当对目录节点监控状态打开时，一旦目录节点的状态发生变化，Watcher 对象的 process 方法就会被调用。
                new Watcher() {
                    public void process(WatchedEvent event) {
                        System.out.println("已经触发了" + event.getType() + "事件～～～" + event);
                    }
                });

        // 创建一个目录节点
        zk.create("/testRootPath", "testRootData".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println(new String(zk.getData("/testRootPath", false, null)));
        System.out.println("===================================================================");
        // 创建一个子目录节点
        zk.create("/testRootPath/testChildPathOne", "testChildDataOne".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        // 取出子目录节点列表
        System.out.println("/testRootPath目录子节点：" + zk.getChildren("/testRootPath", false));
        System.out.println("===================================================================");


        zk.setData("/testRootPath/testChildPathOne", "modifyChildDataOne".getBytes(), -1);
        System.out.println("目录节点状态：[" + zk.exists("/testRootPath", true) + "]");
        System.out.println("===================================================================");
        zk.create("/testRootPath/testChildPathTwo", "testChildDataTwo".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println(new String(zk.getData("/testRootPath/testChildPathTwo", true, null)));
        System.out.println("===================================================================");


        zk.delete("/testRootPath/testChildPathTwo", -1);
        zk.delete("/testRootPath/testChildPathOne", -1);
        zk.delete("/testRootPath", -1);
        zk.close();
    }
}
