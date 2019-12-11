package com.rocksang.study.sharing.demo.designDode;

/**
 * 简单工厂模式
 *
 * 简单工厂模式又称静态工厂方法模式。重命名上就可以看出这个模式一定很简单。它存在的目的很简单：定义一个用于创建对象的接口。
 *
 * 在简单工厂模式中,一个工厂类处于对产品类实例化调用的中心位置上,它决定那一个产品类应当被实例化, 如同一个交通警察站在来往的车辆流中,决定放行那一个方向的车辆向那一个方向流动一样。
 *
 * 先来看看它的组成：
 *
 * 工厂类角色：这是本模式的核心，含有一定的商业逻辑和判断逻辑。在java中它往往由一个具体类实现。
 * 抽象产品角色：它一般是具体产品继承的父类或者实现的接口。在java中由接口或者抽象类来实现。
 * 具体产品角色：工厂类所创建的对象就是此角色的实例。在java中由一个具体类实现。
 */
public class FactorySimpleMode {
    public static void main(String[] args) {
        SampleFactory.creator(1).run();
        SampleFactory.creator(2).run();
    }

}

class SampleFactory{

    public static Sample creator(int which){
        if (which==1) {
            return new SampleA();
        } else if (which==2) {
            return new SampleB();
        }
        return null;
    }
}

interface Sample{
    void run();
}

class SampleA implements Sample{
    @Override
    public void run() {
        System.out.println("this is sample a");
    }
}

class SampleB implements Sample{
    @Override
    public void run() {
        System.out.println("this is sample b");
    }
}
