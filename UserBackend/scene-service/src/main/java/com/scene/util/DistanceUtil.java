package com.scene.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 距离计算工具类
 */
public class DistanceUtil {
    
    private static final double EARTH_RADIUS = 6371000; // 地球半径，单位：米
    
    /**
     * 计算两个经纬度点之间的距离（单位：米）
     * @param lat1 点1的纬度
     * @param lng1 点1的经度
     * @param lat2 点2的纬度
     * @param lng2 点2的经度
     * @return 距离，单位：米
     */
    public static double calculateDistance(BigDecimal lat1, BigDecimal lng1, BigDecimal lat2, BigDecimal lng2) {
        if (lat1 == null || lng1 == null || lat2 == null || lng2 == null) {
            return 0;
        }
        
        double lat1Rad = Math.toRadians(lat1.doubleValue());
        double lng1Rad = Math.toRadians(lng1.doubleValue());
        double lat2Rad = Math.toRadians(lat2.doubleValue());
        double lng2Rad = Math.toRadians(lng2.doubleValue());
        
        double a = Math.sin((lat2Rad - lat1Rad) / 2) * Math.sin((lat2Rad - lat1Rad) / 2) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                Math.sin((lng2Rad - lng1Rad) / 2) * Math.sin((lng2Rad - lng1Rad) / 2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        return EARTH_RADIUS * c;
    }
    
    /**
     * 将距离转换为友好的显示格式
     * @param distance 距离，单位：米
     * @return 格式化后的距离字符串
     */
    public static String formatDistance(double distance) {
        if (distance < 1000) {
            return BigDecimal.valueOf(distance).setScale(0, RoundingMode.HALF_UP) + "m";
        } else {
            return BigDecimal.valueOf(distance / 1000).setScale(1, RoundingMode.HALF_UP) + "km";
        }
    }
}