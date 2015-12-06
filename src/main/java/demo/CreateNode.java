package demo;

import org.apache.zookeeper.*;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Created by guanshiming on 2015/12/6.
 */
public class CreateNode implements Watcher {
    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    public static void main(String[] args) throws IOException, InterruptedException {
        ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:2181", 5000, new CreateNode());
        System.out.println(zooKeeper.getState());
        countDownLatch.await();
    }

    // 使用同步API创建节点
    public static  void createNodeSync(ZooKeeper zooKeeper)throws KeeperException, InterruptedException {
        String path1 = zooKeeper.create("/zk-ephemeral", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        System.out.println("成功创建临时节点:" + path1);
        String path2 = zooKeeper.create("/zk-ephemeral", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println("成功创建临时顺序节点:" + path2);
    }

    // 使用异步api创建节点
    public static  void createNodeAsync(ZooKeeper zooKeeper)throws KeeperException, InterruptedException {
        zooKeeper.create("/zk-ephemeral",
                "".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL,
                new IStringCallBack(), "I am context.");
    }

    @Override
    public void process(WatchedEvent event) {
        System.out.println("Receive watched event:" + event);
        if(Event.KeeperState.SyncConnected == event.getState()){
            countDownLatch.countDown();
        }
    }
}

class IStringCallBack implements AsyncCallback.StringCallback{
    @Override
    public void processResult(int rc, String path, Object ctx, String name) {
        System.out.println("Create path result:[" + rc + "," + path + "," + ctx + ", real path name:" + name);
    }
}