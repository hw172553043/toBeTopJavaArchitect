package com.rocksang.study.sharing.demo.designDode;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @Desc: 动态代理
 * @Author: Rock.Sang
 * @Date: 2019/12/12 2:07 PM
 * @blame Personal
 */
public class ProxyDynamicMode {
    public static void main(String[] args) {
        ClassLoader classLoader = BuyHouse2.class.getClassLoader();
        Class<?>[] interfaces = new Class[]{BuyHouse2.class};
        InvocationHandler dynamicProxyHandler = new DynamicProxyHandler(new ZhangSanBuyHouse());
        BuyHouse2 proxyBuyHouse = (BuyHouse2) Proxy.newProxyInstance(classLoader, interfaces, dynamicProxyHandler);
        proxyBuyHouse.buyHouse();
        System.out.println();
        InvocationHandler dynamicProxyHandler2 = new DynamicProxyHandler(new ZhangSan2BuyHouse());
        BuyHouse2 proxyBuyHouse2 = (BuyHouse2) Proxy.newProxyInstance(classLoader, interfaces, dynamicProxyHandler2);
        proxyBuyHouse2.buyHouse();
    }
}

/**
 * 第一步：创建服务类接口
 */
interface BuyHouse2 {
    void buyHouse();
}

/**
 * 第二步：实现服务接口
 */
class ZhangSanBuyHouse implements BuyHouse2 {
    @Override
    public void buyHouse() {
        System.out.println("张三1要买房");
    }
}

/**
 * 第二步：实现服务接口
 */
class ZhangSan2BuyHouse implements BuyHouse2 {
    @Override
    public void buyHouse() {
        System.out.println("张三2要买房");
    }
}

/**
 * 动态代理类
 */
class DynamicProxyHandler implements InvocationHandler {

    private Object object;

    public DynamicProxyHandler(final Object object) {
        this.object = object;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("买房前准备");
        Object result = method.invoke(object, args);
        System.out.println("买房后装修");
        return result;
    }
}
