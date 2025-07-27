package com.chennian.storytelling.common.sharding;

import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 分片键生成器
 * 基于雪花算法生成分布式唯一ID
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Component
public class ShardingKeyGenerator {

    // 起始时间戳 (2024-01-01 00:00:00)
    private static final long START_TIMESTAMP = 1704067200000L;
    
    // 机器ID位数
    private static final long WORKER_ID_BITS = 5L;
    
    // 数据中心ID位数
    private static final long DATACENTER_ID_BITS = 5L;
    
    // 序列号位数
    private static final long SEQUENCE_BITS = 12L;
    
    // 最大机器ID
    private static final long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);
    
    // 最大数据中心ID
    private static final long MAX_DATACENTER_ID = ~(-1L << DATACENTER_ID_BITS);
    
    // 序列号掩码
    private static final long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);
    
    // 机器ID左移位数
    private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;
    
    // 数据中心ID左移位数
    private static final long DATACENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;
    
    // 时间戳左移位数
    private static final long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATACENTER_ID_BITS;
    
    private final long workerId;
    private final long datacenterId;
    private long sequence = 0L;
    private long lastTimestamp = -1L;
    
    public ShardingKeyGenerator() {
        this.workerId = getWorkerId();
        this.datacenterId = getDatacenterId();
    }
    
    public ShardingKeyGenerator(long workerId, long datacenterId) {
        if (workerId > MAX_WORKER_ID || workerId < 0) {
            throw new IllegalArgumentException(String.format("Worker ID can't be greater than %d or less than 0", MAX_WORKER_ID));
        }
        if (datacenterId > MAX_DATACENTER_ID || datacenterId < 0) {
            throw new IllegalArgumentException(String.format("Datacenter ID can't be greater than %d or less than 0", MAX_DATACENTER_ID));
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }
    
    /**
     * 生成下一个ID
     */
    public synchronized long nextId() {
        long timestamp = timeGen();
        
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(String.format("Clock moved backwards. Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }
        
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & SEQUENCE_MASK;
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }
        
        lastTimestamp = timestamp;
        
        return ((timestamp - START_TIMESTAMP) << TIMESTAMP_LEFT_SHIFT)
                | (datacenterId << DATACENTER_ID_SHIFT)
                | (workerId << WORKER_ID_SHIFT)
                | sequence;
    }
    
    /**
     * 生成字符串格式的ID
     */
    public String nextStringId() {
        return String.valueOf(nextId());
    }
    
    /**
     * 生成用户ID
     */
    public Long generateUserId() {
        return nextId();
    }
    
    /**
     * 生成订单ID
     */
    public Long generateOrderId() {
        return nextId();
    }
    
    /**
     * 生成支付订单ID
     */
    public Long generatePaymentOrderId() {
        return nextId();
    }
    
    /**
     * 根据用户ID计算分库索引
     */
    public int getDatabaseIndex(Long userId, int databaseCount) {
        return (int) (userId % databaseCount);
    }
    
    /**
     * 根据ID计算分表索引
     */
    public int getTableIndex(Long id, int tableCount) {
        return (int) (id % tableCount);
    }
    
    /**
     * 获取分库分表后缀
     */
    public String getShardingSuffix(Long id, int shardCount) {
        return String.valueOf(id % shardCount);
    }
    
    /**
     * 阻塞到下一个毫秒
     */
    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }
    
    /**
     * 返回当前时间戳
     */
    protected long timeGen() {
        return System.currentTimeMillis();
    }
    
    /**
     * 获取机器ID
     */
    private long getWorkerId() {
        try {
            InetAddress address = InetAddress.getLocalHost();
            byte[] ipAddressBytes = address.getAddress();
            return ((0x000000FF & (long) ipAddressBytes[ipAddressBytes.length - 1])
                    | (0x0000FF00 & (((long) ipAddressBytes[ipAddressBytes.length - 2]) << 8))) >> 6;
        } catch (UnknownHostException e) {
            return ThreadLocalRandom.current().nextLong(0, MAX_WORKER_ID + 1);
        }
    }
    
    /**
     * 获取数据中心ID
     */
    private long getDatacenterId() {
        try {
            InetAddress address = InetAddress.getLocalHost();
            byte[] ipAddressBytes = address.getAddress();
            return ((0x000000FF & (long) ipAddressBytes[ipAddressBytes.length - 1])
                    | (0x0000FF00 & (((long) ipAddressBytes[ipAddressBytes.length - 2]) << 8))) >> 1;
        } catch (UnknownHostException e) {
            return ThreadLocalRandom.current().nextLong(0, MAX_DATACENTER_ID + 1);
        }
    }
    
    /**
     * 解析ID中的时间戳
     */
    public long parseTimestamp(long id) {
        return (id >> TIMESTAMP_LEFT_SHIFT) + START_TIMESTAMP;
    }
    
    /**
     * 解析ID中的机器ID
     */
    public long parseWorkerId(long id) {
        return (id >> WORKER_ID_SHIFT) & MAX_WORKER_ID;
    }
    
    /**
     * 解析ID中的数据中心ID
     */
    public long parseDatacenterId(long id) {
        return (id >> DATACENTER_ID_SHIFT) & MAX_DATACENTER_ID;
    }
    
    /**
     * 解析ID中的序列号
     */
    public long parseSequence(long id) {
        return id & SEQUENCE_MASK;
    }
}