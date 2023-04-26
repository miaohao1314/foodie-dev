package com.example.service;

import com.example.dao.OrderItemMapper;
import com.example.dao.OrderMapper;
import com.example.dao.ProductMapper;
import com.example.model.Order;
import com.example.model.OrderItem;
import com.example.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
@Slf4j
public class TestService {

    @Resource
    private OrderMapper orderMapper;
    @Resource
    private OrderItemMapper orderItemMapper;
    @Resource
    private ProductMapper productMapper;
    //购买商品id
    private int purchaseProductId = 100100;
    //购买商品数量
    private int purchaseProductNum = 1;

    // 手动控制事务
    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    // 事务的定义
    @Autowired
    private TransactionDefinition transactionDefinition;

    private Lock lock = new ReentrantLock();


    //    @Transactional(rollbackFor = Exception.class)
    public synchronized Integer createOrder() throws Exception{
        Product product = null;

        lock.lock();
        try {
            // 手动获取事务
            TransactionStatus transaction1 = platformTransactionManager.getTransaction(transactionDefinition);
            // 检索商品是否存在
            product = productMapper.selectByPrimaryKey(purchaseProductId);
            if (product==null){
                // 事务回滚
                platformTransactionManager.rollback(transaction1);
                throw new Exception("购买商品："+purchaseProductId+"不存在");
            }

            //商品当前库存
            Integer currentCount = product.getCount();
            System.out.println(Thread.currentThread().getName()+"库存数："+currentCount);
            //校验库存（购买商品的数量 > 库存数）
            if (purchaseProductNum > currentCount){
                // 事务回滚
                platformTransactionManager.rollback(transaction1);
                throw
                        new Exception("商品"+purchaseProductId+"仅剩"+currentCount+"件，无法购买");
            }

            // 这里扣减库存不在程序中执行，使其在数据库中去执行，这里自定义sql，update有行锁，需要执行完一个再去执行其他的
            productMapper.updateProductCount(purchaseProductNum,"xxx",new Date(),product.getId());
            platformTransactionManager.commit(transaction1);
        }finally {
            lock.unlock();
        }

        TransactionStatus transaction = platformTransactionManager.getTransaction(transactionDefinition);
        Order order = new Order();
        order.setOrderAmount(product.getPrice().multiply(new BigDecimal(purchaseProductNum)));
        order.setOrderStatus(1);//待处理
        order.setReceiverName("xxx");
        order.setReceiverMobile("13311112222");
        order.setCreateTime(new Date());
        order.setCreateUser("xxx");
        order.setUpdateTime(new Date());
        order.setUpdateUser("xxx");
        orderMapper.insertSelective(order);

        OrderItem orderItem = new OrderItem();
        orderItem.setOrderId(order.getId());
        orderItem.setProductId(product.getId());
        orderItem.setPurchasePrice(product.getPrice());
        orderItem.setPurchaseNum(purchaseProductNum);
        orderItem.setCreateUser("xxx");
        orderItem.setCreateTime(new Date());
        orderItem.setUpdateTime(new Date());
        orderItem.setUpdateUser("xxx");
        orderItemMapper.insertSelective(orderItem);
        // 提交事务之后，才会去释放锁synchronized，然后其他的线程才可以去争抢这把锁，然后再去执行方法里面的内容
        platformTransactionManager.commit(transaction);
        return order.getId();
    }

}
