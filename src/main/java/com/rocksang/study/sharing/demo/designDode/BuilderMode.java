package com.rocksang.study.sharing.demo.designDode;

import com.alibaba.fastjson.JSON;
import lombok.Data;

/**
 * @Desc: 构造者模式
 * @Author: Rock.Sang
 * @Date: 2019/12/11 4:34 PM
 * @blame Personal
 */
public class BuilderMode {

    public static void main(String[] args) {
        Product product1 = Director.construct("one").retrievePro();
        Product product2 = Director.construct("two").retrievePro();
    }
}

@Data
class Director{
    //这里可以结合工厂模式
    protected static Builder construct(String type){
        Builder builder = null;
        if("one".equals(type)){
            builder=new ConcreteBuilderOne();
            builder.buildOne();
            builder.buildTwo();
        }else if("two".equals(type)){
            builder=new ConcreteBuilderTwo();
            builder.buildOne();
            builder.buildTwo();
        }
        return builder;
    }
}

// ************************
interface Builder{

    void buildOne();

    void buildTwo();

    Product retrievePro();
}

class ConcreteBuilderOne implements Builder{

    private Product product=new ProductOne();

    @Override
    public void buildOne(){
        //打造第一个零件
        product.setPart1("产品1-零件1");
    }
    @Override
    public void buildTwo(){
        //打造第二个零件
        product.setPart2("产品1-零件2");
    }

    @Override
    public Product retrievePro(){
        //组装出产品1
        System.out.println("this is a product, json is "+ JSON.toJSON(product));
        return product;
    }
}

class ConcreteBuilderTwo implements Builder{

    private Product product=new ProductTwo();

    @Override
    public void buildOne(){
        //打造第一个零件
        product.setPart1("产品2-零件1");
    }
    @Override
    public void buildTwo(){
        //打造第二个零件
        product.setPart2("产品2-零件2");
    }

    @Override
    public Product retrievePro(){
        //组装出产品2
        System.out.println("this is a product, json is "+ JSON.toJSON(product));
        return product;
    }
}

// ************************
@Data
abstract class Product{

    //产品分为三部分

    String part1;

    String part2;



}
class ProductOne extends Product{
    //具体第一个产品相关属性方法
}

class ProductTwo extends Product{
    //具体第二个产品相关属性方法
}