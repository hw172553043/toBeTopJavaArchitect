package com.rocksang.study.sharing.demo.designDode;

/**
 * Singleton
 *
 * @blame Android Team
 */
public class SingletonDemo {

    // 饿汉式
    private static SingletonDemo hangerSingleton = new SingletonDemo();
    private SingletonDemo() {
    }
    protected static SingletonDemo getHungerInstance() {
        return hangerSingleton;
    }


    // 懒汉式
    // 优点：延迟加载（需要的时候才去加载）,适合单线程操作
    // 缺点： 线程不安全，在多线程中很容易出现不同步的情况，如在数据库对象进行的频繁读写操作时。
    private static SingletonDemo lazySingleton = null;
    //    private Singleton() {
    //    }
    public static SingletonDemo getLazyInstance() {
        if (null == lazySingleton) {
            lazySingleton = new SingletonDemo();
        }
        return lazySingleton;
    }

    //  双重线程检查模式
    //  优点：线程安全，支持延时加载，调用效率高
    //  缺点： 写法复杂，不简洁
    private static SingletonDemo doubleCheckSingleton = null;
    //    private Singleton() {
    //    }
    public static SingletonDemo getDoubleCheckInstance() {
        if (null == doubleCheckSingleton) {
            synchronized (SingletonDemo.class) {
                if (null == doubleCheckSingleton) {
                    doubleCheckSingleton = new SingletonDemo();
                }
            }
        }
        return doubleCheckSingleton;
    }

    // 内部类实现单例模式
    // 延迟加载，减少内存开销
    // 延迟加载，线程安全（java中class加载时互斥的），也减少了内存消耗，推荐使用内部类方式。
    private static class SingletonHolder {
        private static SingletonDemo instance = new SingletonDemo();
    }
    //    private Singleton() {
    //    }
    public static SingletonDemo getInnerClassInstance() {
        return SingletonHolder.instance;
    }

    public static void main(String[] args) {

        // 测试饿汉式
        SingletonDemo hungerSingleton = getHungerInstance();
        hungerSingleton.method();

        // 测试懒汉式
        SingletonDemo lazySingleton = getLazyInstance();
        lazySingleton.method();

        // 双重线程检查模式
        SingletonDemo doubleCheckSingleton = getDoubleCheckInstance();
        doubleCheckSingleton.method();

        // 内部类模式
        SingletonDemo innerSingleton = getInnerClassInstance();
        innerSingleton.method();

    }

    private void method() {
        System.out.println("this is a singleton method!");
    }
}
