package com.rocksang.study.sharing.demo.designDode;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @Desc:
 * @Author: Rock.Sang
 * @Date: 2019/12/12 2:58 PM
 * @blame Personal
 */
public class ProxyCGLibMode {

    public static void main(String[] args) {

        CglibProxy cglibProxy = new CglibProxy();
        BuyHouse3 buyHouseCglibProxy = (BuyHouse3) cglibProxy.getInstance(new BuyHouse3Impl());
        buyHouseCglibProxy.confirmHouse();
        System.out.println();
        buyHouseCglibProxy.buyHouse();
        System.out.println();
        buyHouseCglibProxy.cleanHouse();
    }
}

interface BuyHouse3 {

    void confirmHouse();

    void buyHouse();

    void cleanHouse();
}

class BuyHouse3Impl implements BuyHouse3 {

    @Override
    public void confirmHouse() {
        System.out.println("确认房子是否完好!");
    }

    @Override
    public void buyHouse() {
        System.out.println("现在买房!");
    }

    @Override
    public void cleanHouse() {
        System.out.println("打扫房间!");
    }
}

/**
 * 第一步：创建CGLIB代理类
 */
class CglibProxy implements MethodInterceptor {

    private Object target;

    public Object getInstance(Object obj) {
        this.target = obj;
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(obj.getClass());
        enhancer.setCallback(this);
        return enhancer.create();
    }

    @Override
    public Object intercept(Object object, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        System.out.println("**** 买房前准备 ****");
        Object result = method.invoke(target, args);
        System.out.println("**** 买房后装修 ****");
        return result;
    }
}


