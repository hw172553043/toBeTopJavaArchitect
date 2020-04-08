package com.rocksang.study.sharing.demo.part1;

public class StringEqualsTest {

    public static void main(String[] args) {


        String aa = new String("111");
        String bb = new String("111");
        System.out.println(aa==bb);
        System.out.println(aa.equals(bb));
    }
}
