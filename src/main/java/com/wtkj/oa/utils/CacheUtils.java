package com.wtkj.oa.utils;

import com.google.common.cache.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class CacheUtils {
    public static void main(String[] args) throws ExecutionException {
        LoadingCache<Integer, String> numberCache
                //CacheBuilder的构造函数是私有的，只能通过其静态方法newBuilder()来获得CacheBuilder的实例
                = CacheBuilder.newBuilder()
                //设置写缓存后8秒钟过期
                .expireAfterWrite(10, TimeUnit.SECONDS)
                //设置缓存的移除通知
                .removalListener(new RemovalListener<Object, Object>() {
                    @Override
                    public void onRemoval(RemovalNotification<Object, Object> notification) {
                        System.out.println(notification.getKey() + " was removed, cause is " + notification.getCause());
                    }
                })
                //build方法中可以指定CacheLoader，在缓存不存在时通过CacheLoader的实现自动加载缓存
                .build(
                        new CacheLoader<Integer, String>() {
                            @Override
                            public String load(Integer key) throws Exception {
                                System.out.println("key is " + key);
                                return getValue();
                            }

                            private String getValue() {
                                System.out.println("重新获取！");
                                return "hello!";
                            }
                        }
                );

        for (int i = 0; i < 20; i++) {
            //从缓存中得到数据，由于我们没有设置过缓存，所以需要通过CacheLoader加载缓存数据
            String value = numberCache.get(1);
            System.out.println(value);
            //休眠1秒
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("cache stats:");
        //最后打印缓存的命中率等 情况
        //System.out.println(numberCache.stats().toString());
    }
}
