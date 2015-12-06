package demo;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Created by guanshiming on 2015/12/6.
 */
public class Connect implements Watcher {
    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    public static void main(String[] args) throws IOException{
        ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:2181", 5000, new Connect());
        System.out.println(zooKeeper.getState());
        try {
            countDownLatch.await();
        }catch (InterruptedException e){
            e.getStackTrace();
        }
        System.out.println("Session established!");
    }

    @Override
    public void process(WatchedEvent event) {
        System.out.println("Receive watched event:" + event);
        if(Event.KeeperState.SyncConnected == event.getState()){
            countDownLatch.countDown();
        }
    }
}
