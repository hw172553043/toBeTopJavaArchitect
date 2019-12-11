package com.rocksang.study.sharing.demo.designDode;

/**
 * 抽象工厂模式
 *
 * @blame Android Team
 */
public class FactoryAbstractMode {
    public static void main(String[] args) {
        AbstractFactory f = new DefaultFactory();
        Vehicle v = f.createVehicle();
        v.run();
        Weapon w = f.createWeapon();
        w.shoot();
        Food a = f.createFood();
        a.printName();
    }
}

//抽象工厂类
abstract class AbstractFactory {

    public abstract Vehicle createVehicle();

    public abstract Weapon createWeapon();

    public abstract Food createFood();
}

//具体工厂类，其中Food,Vehicle，Weapon是抽象类，
class DefaultFactory extends AbstractFactory {
    @Override
    public Food createFood() {
        return new Apple1();
    }

    @Override
    public Vehicle createVehicle() {
        return new Bus();
    }

    @Override
    public Weapon createWeapon() {
        return new AK47();
    }
}


interface Vehicle {
    void run();
}

interface Weapon {
    void shoot();
}

interface Food {
    void printName();
}

class Bus implements Vehicle{
    @Override
    public void run() {
        System.out.println("this is a bus!");
    }
}

class AK47 implements Weapon{
    @Override
    public void shoot() {
        System.out.println("this is a AK47!");
    }
}

class Apple1 implements Food{
    @Override
    public void printName() {
        System.out.println("this is a apple!");
    }
}