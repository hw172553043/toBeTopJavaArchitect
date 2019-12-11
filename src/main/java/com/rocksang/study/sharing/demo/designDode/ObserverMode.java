package com.rocksang.study.sharing.demo.designDode;

import java.util.Vector;

/**
 * @Desc: 观察者模式
 * @Author: Rock.Sang
 * @Date: 2019/12/11 5:39 PM
 * @blame Personal
 */
public class ObserverMode {
    public static void main(String[] args) {
        Vector students = new Vector();
        Teacher teacher = new Teacher();
        for(int i= 0;i<10;i++){
            Student st = new Student("学生-"+i,teacher);
            students.add(st);
            teacher.attach(st);
        }

        System.out.println("观察者模式 -- 老师更换手机号");
        teacher.setPhone("老师更换手机号一");
        for(int i=0;i<10;i++) {
            ((Student)students.get(i)).show();
        }

        System.out.println("观察者模式 -- 老师再次更换手机号");
        teacher.setPhone("老师更换手机号二");
        for(int i=0;i<10;i++) {
            ((Student)students.get(i)).show();
        }
    }
}

/**
 * Subject(目标，Subject)：
 * 目标知道它的观察者。可以有任意多个观察者观察同一个目标。
 * 提供注册和删除观察者对象的接口。
 */
interface Subject {
    void attach(Observer mObserver);
    void detach(Observer mObserver);
    void notice();
}

/**
 * ConcreteSubject(具体目标，Teacher)
 * 将有关状态存入各ConcreteObserve对象。
 * 当他的状态发生改变时，向他的各个观察者发出通知。
 */
class Teacher implements Subject{

    private String phone;
    private Vector students;

    public Teacher(){
        phone = "";
        students = new Vector();
    }

    @Override
    public void attach(Observer mObserver) {
        students.add(mObserver);
    }

    @Override
    public void detach(Observer mObserver) {
        students.remove(mObserver);
    }

    @Override
    public void notice() {
        for(int i=0;i<students.size();i++){
            ((Observer)students.get(i)).update();
        }
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        notice();
    }
}


/**
 * Observer(观察者，Observer)：
 * 为那些在目标发生改变时需要获得通知的对象定义一个更新接口。
 */
interface Observer {
    void update();
}

/**
 * ConcreteObserver(具体观察者, Student)：
 * 维护一个指向ConcreteSubject对象的引用。
 * 存储有关状态，这些状态应与目标的状态保持一致。
 * 实现Observer的更新接口以使自身状态与目标的状态保持一致。
 */
class Student implements Observer{

    private String name;
    private String phone;
    private Teacher mTeacher;

    public Student(String name,Teacher t){
        this.name = name;
        mTeacher = t;
    }

    public void show(){
        System.out.println("学生姓名:"+name+"，老师电话:" + phone);
    }

    @Override
    public void update() {
        phone = mTeacher.getPhone();
    }
}

