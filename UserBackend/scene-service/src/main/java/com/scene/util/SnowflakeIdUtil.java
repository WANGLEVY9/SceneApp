package com.scene.util;

/**
 * 雪花ID生成工具类
 */
public class SnowflakeIdUtil {
    
    private static final long START_TIMESTAMP = 1609459200000L; // 2021-01-01 00:00:00
    private static final long SEQUENCE_BIT = 12L;
    private static final long MACHINE_BIT = 5L;
    private static final long DATACENTER_BIT = 5L;
    
    private static final long MAX_DATACENTER_NUM = ~(-1L << DATACENTER_BIT);
    private static final long MAX_MACHINE_NUM = ~(-1L << MACHINE_BIT);
    private static final long MAX_SEQUENCE = ~(-1L << SEQUENCE_BIT);
    
    private static final long MACHINE_LEFT = SEQUENCE_BIT;
    private static final long DATACENTER_LEFT = SEQUENCE_BIT + MACHINE_BIT;
    private static final long TIMESTAMP_LEFT = DATACENTER_LEFT + DATACENTER_BIT;
    
    private static long datacenterId = 1L;
    private static long machineId = 1L;
    private static long sequence = 0L;
    private static long lastTimestamp = -1L;
    
    /**
     * 生成雪花ID
     * @return 雪花ID
     */
    public static synchronized String generateId() {
        long currentTimeMillis = System.currentTimeMillis();
        
        if (currentTimeMillis < lastTimestamp) {
            throw new RuntimeException("时钟回拨异常");
        }
        
        if (currentTimeMillis == lastTimestamp) {
            sequence = (sequence + 1) & MAX_SEQUENCE;
            if (sequence == 0L) {
                currentTimeMillis = getNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }
        
        lastTimestamp = currentTimeMillis;
        
        long id = ((currentTimeMillis - START_TIMESTAMP) << TIMESTAMP_LEFT)
                | (datacenterId << DATACENTER_LEFT)
                | (machineId << MACHINE_LEFT)
                | sequence;
        
        return String.valueOf(id);
    }
    
    private static long getNextMillis(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }
}