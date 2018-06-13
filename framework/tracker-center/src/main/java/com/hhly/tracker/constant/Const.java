package com.hhly.tracker.constant;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author pengchao
 * @create 2017-11-24
 * @desc 常量
 */
public class Const {
    //访问计数器，压测用
    public static AtomicInteger REQUEST_COUNT = new AtomicInteger(0);
}