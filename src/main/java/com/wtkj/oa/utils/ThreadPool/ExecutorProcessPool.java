package com.wtkj.oa.utils.ThreadPool;


import java.util.concurrent.ExecutorService;

public class ExecutorProcessPool {

    private ExecutorService executor;
    private static ExecutorProcessPool pool = new ExecutorProcessPool();
    private final int threadMax = 10;

    private ExecutorProcessPool() {
         ExecutorServiceFactory.getInstance();
    }


}
