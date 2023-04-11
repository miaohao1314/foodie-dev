package com.example.distributezklock.lock;


import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

// Watcher: zookeeper的观察器

@Slf4j
public class ZkLock  implements  AutoCloseable, Watcher {


    private ZooKeeper zooKeeper;

    private String znode;

    // 构造函数，连接 zookeeper的服务时使用的
    public  ZkLock() throws IOException {
        // this代表上面已经继承
        this.zooKeeper=new ZooKeeper("localhost:2181",10000,this);
    }

    // 获取锁
    public  boolean getlock(String businessCode){
        // businessCode每个业务创建的锁是不一样的,false代表不需要观察
        try {
            // 创建业务根节点
            Stat exists = zooKeeper.exists("/"+ businessCode, false);
            if(exists==null){
                // 不存在就创建节点
                zooKeeper.create("/"+businessCode,businessCode.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                        CreateMode.PERSISTENT);
            }

            // 创建瞬时有序节点(/order/order_0000001)
             znode = zooKeeper.create("/" + businessCode + "/" + businessCode + "_", businessCode.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                    CreateMode.EPHEMERAL_SEQUENTIAL);

            // 判断znode是否是节点最小的那一个
            List<String> childrenNodes = zooKeeper.getChildren("/" + businessCode, false);
            // 将集合序列进行升序
            Collections.sort(childrenNodes);
            // 取出第一个节点，序号最小的这个节点
            String firstNode = childrenNodes.get(0);
            // 判断的是/order_0000001 然后获得锁 ，所以不能用equals
            if(znode.endsWith(firstNode)){
                return true;
            }

            // 不是第一个节点则监听前一个节点
            String lastNode=firstNode;
            for (String childrenNode : childrenNodes) {
                if(znode.endsWith(childrenNode)){
                    // 判断这个节点是否存在
                    zooKeeper.exists("/" + businessCode + "/" + lastNode ,true);
                    break;
                }else{
                    lastNode=childrenNode;
                }
            }

            // 等待的时候需要上锁,防止并发时候的混乱
            synchronized (this){
                wait();
            }
            return  true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }






    @Override
    public void close() throws Exception {
        zooKeeper.delete(znode,-1);
        zooKeeper.close();
        log.info("我已经释放了锁");
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        if(watchedEvent.getType()== Event.EventType.NodeDeleted){
            // 需要唤起这个锁
            synchronized (this){
                notify();
            }
        }
    }
}
