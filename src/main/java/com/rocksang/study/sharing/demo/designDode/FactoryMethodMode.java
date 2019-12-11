package com.rocksang.study.sharing.demo.designDode;

/**
 * 工厂方式模式
 * 工厂方法模式是简单工厂模式的进一步抽象化和推广，工厂方法模式里不再只由一个工厂类决定那一个产品类应当被实例化,这个决定被交给抽象工厂的子类去做。
 * 来看下它的组成：
 * <p>
 * 抽象工厂角色：这是工厂方法模式的核心，它与应用程序无关。是具体工厂角色必须实现的接口或者必须继承的父类。在java中它由抽象类或者接口来实现。
 * 具体工厂角色：它含有和具体业务逻辑有关的代码。由应用程序调用以创建对应的具体产品的对象
 * 抽象产品角色：它是具体产品继承的父类或者是实现的接口。在java中一般有抽象类或者接口来实现。
 * 具体产品角色：具体工厂角色所创建的对象就是此角色的实例。在java中由具体的类来实现。
 * 工厂方法模式使用继承自抽象工厂角色的多个子类来代替简单工厂模式中的“上帝类”。
 * 正如上面所说，这样便分担了对象承受的压力；而且这样使得结构变得灵活 起来——当有新的产品（即暴发户的汽车）产生时，只要按照抽象产品角色、抽象工厂角色提供的合同来生成，那么就可以被客户使用，而不必去修改任何已有的代 码。
 * 可以看出工厂角色的结构也是符合开闭原则的！
 *
 * @blame Android Team
 */
public class FactoryMethodMode {
    public static void main(String[] args) {
        VehicleFactory planeFactory = new PlaneFactory();
        VehicleFactory carFactory = new CarFactory();
        planeFactory.create().run();
        carFactory.create().run();
    }
}


//抽象产品角色
interface Moveable {
    void run();
}
//具体产品角色
class Plane implements Moveable {
    @Override
    public void run() {
        System.out.println("plane....");
    }
}
//具体产品角色
class Car implements Moveable {
    @Override
    public void run() {
        System.out.println("car.....");
    }
}

//抽象工厂
abstract class VehicleFactory {
    abstract Moveable create();
}

//具体工厂
class PlaneFactory extends VehicleFactory{
    @Override
    public Moveable create() {
        return new Plane();
    }
}

//具体工厂
class CarFactory extends VehicleFactory{
    @Override
    public Moveable create() {
        return new Car();
    }
}


