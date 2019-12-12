package com.rocksang.study.sharing.demo.designDode;

/**
 * @Desc: 适配器模式
 * @Author: Rock.Sang
 * @Date: 2019/12/12 10:13 AM
 * @blame Personal
 */
public class AdapterMode {


}


interface Target {
    /**
     * 这是源类Adaptee也有的方法
     */
    void sampleOperation1();
    /**
     * 这是源类Adapteee没有的方法
     */
    void sampleOperation2();
}

class Adaptee {
    public void sampleOperation1(){}
}

class Adapter implements Target{

    private Adaptee adaptee;
    public Adapter(Adaptee adaptee){
        this.adaptee = adaptee;
    }
    /**
     * 源类Adaptee有方法sampleOperation1
     * 因此适配器类直接委派即可
     */
    @Override
    public void sampleOperation1(){
        this.adaptee.sampleOperation1();
    }
    /**
     * 源类Adaptee没有方法sampleOperation2
     * 因此由适配器类需要补充此方法
     */
    @Override
    public void sampleOperation2(){
        //写相关的代码
    }
}