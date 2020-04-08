package com.rocksang.study.sharing.demo.part1;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolTest {
    public static void main(String[] args) {

        ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 200, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<Runnable>(5));

        for(int i=0;i<15;i++){
            MyTask myTask = new MyTask(i);
            // 设置线程默认的异常捕获方法
            myTask.setUncaughtExceptionHandler((Thread t, Throwable e) -> {System.out.println(t.getName() + ": " + e.getMessage());});
            Future future = executor.submit(myTask);
//            myTask.start();
            System.out.println("线程池中核心线程数目："+executor.getPoolSize()+"，队列中等待执行的任务数目："+
                    executor.getQueue().size()+"，已执行完的任务数目："+executor.getCompletedTaskCount());
        }

        executor.shutdown();
    }
}


class MyTask extends Thread {
    private int taskNum;
    private String taskName;

    public MyTask(int num) {
        this.taskNum = num;
        this.taskName = "任务 "+num;
    }

    @Override
    public void run() {
        System.out.println("正在执行task "+taskNum);
        try {
            Thread.sleep(1000);
            int i = 1/0;
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        } catch (RuntimeException e){
            throw new RuntimeException("run time exception");
        }
        setUncaughtExceptionHandler((Thread t, Throwable e) -> {System.out.println(t.getName() + ": " + e.getMessage());});

        System.out.println("task "+taskNum+"执行完毕");
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
}