package com.rocksang.study.sharing.demo.part1;

public class TestThreadLocal {
    static final String VALUE01 = "VALUE01";
    static final String VALUE02 = "VALUE02";

    public static void main(String[] args) throws InterruptedException {
        ThreadLocal<String> threadLocal = new ThreadLocal<String>();
        threadLocal.set(VALUE01);

        InheritableThreadLocal<String> inheritableThreadLocal = new InheritableThreadLocal<String>();
        inheritableThreadLocal.set(VALUE01);

        Thread thread_1 = new Thread_TestThreadLocal(threadLocal, inheritableThreadLocal);
        thread_1.setName("Thread01");
        thread_1.start();

        thread_1.join();

        System.out.println(Thread.currentThread().getName() + "******************************************");
        System.out.println(Thread.currentThread().getName() + "\tThreadLocal: " + threadLocal.get());
        System.out.println(Thread.currentThread().getName() + "\tInheritableThreadLocal: " + inheritableThreadLocal.get());
    }
}

class Thread_TestThreadLocal extends Thread {
    ThreadLocal<String> threadLocal;
    InheritableThreadLocal<String> inheritableThreadLocal;

    public Thread_TestThreadLocal(ThreadLocal<String> threadLocal, InheritableThreadLocal<String> inheritableThreadLocal) {
        super();
        this.threadLocal = threadLocal;
        this.inheritableThreadLocal = inheritableThreadLocal;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + "******************************************");
        System.out.println(Thread.currentThread().getName() + "\tThreadLocal: " + threadLocal.get());
        System.out.println(Thread.currentThread().getName() + "\tInheritableThreadLocal: " + inheritableThreadLocal.get());

        threadLocal.set(TestThreadLocal.VALUE02);
        inheritableThreadLocal.set(TestThreadLocal.VALUE02);

        System.out.println(Thread.currentThread().getName() + "(Reset Value)*****************************");
        System.out.println(Thread.currentThread().getName() + "\tThreadLocal: " + threadLocal.get());
        System.out.println(Thread.currentThread().getName() + "\tInheritableThreadLocal: " + inheritableThreadLocal.get());
    }
}