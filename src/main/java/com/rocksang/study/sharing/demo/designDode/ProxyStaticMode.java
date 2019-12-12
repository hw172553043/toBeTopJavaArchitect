package com.rocksang.study.sharing.demo.designDode;

/**
 * @Desc: 静态代理
 * @Author: Rock.Sang
 * @Date: 2019/12/12 2:07 PM
 * @blame Personal
 */
public class ProxyStaticMode {
    public static void main(String[] args) {
        BuyHouse buyHouse = new BuyHouseImpl();
        buyHouse.buyHouse();
        BuyHouseProxy buyHouseProxy = new BuyHouseProxy(buyHouse);
        buyHouseProxy.buyHouse();
    }
}

/**
 * 第一步：创建服务类接口
 */
interface BuyHouse {
    void buyHouse();
}

/**
 * 第二步：实现服务接口
 */
class BuyHouseImpl implements BuyHouse {
    @Override
    public void buyHouse() {
        System.out.println("我要买房");
    }
}

/**
 * 第三步：创建代理类
 */
class BuyHouseProxy implements BuyHouse {

    private BuyHouse buyHouse;

    public BuyHouseProxy(final BuyHouse buyHouse) {
        this.buyHouse = buyHouse;
    }

    @Override
    public void buyHouse() {
        System.out.println("买房前准备");
        buyHouse.buyHouse();
        System.out.println("买房后装修");
    }
}


