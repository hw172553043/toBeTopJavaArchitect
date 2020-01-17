package com.rocksang.study.sharing.demo.leetcode;

/**
 * @Desc: 整数反转
 * @Author: Rock.Sang
 * @Date: 2020/1/17 3:20 PM
 * @blame Personal
 */
public class 整数反转 {


    public static void main(String[] args) {

        int data = 2147483646;
        System.out.println(data);
        System.out.println(reverse(data));

        int data1 = -2134561231;
        System.out.println(data1);
        System.out.println(reverse(data1));

        int data2 = Integer.MAX_VALUE;
        System.out.println(data2);
        System.out.println(reverse(data2));

        int data3 = Integer.MIN_VALUE;
        System.out.println(data3);
        System.out.println(reverse(data3));
    }

    public static long reverse(int x) {
        Long newRevert = 0L;
        while (x != 0) {
            int pop = x % 10;
            x = x / 10;
            if (newRevert > Integer.MAX_VALUE / 10 || (newRevert >= Integer.MAX_VALUE / 10 && pop > 7)) {
                return 0;
            }
            if (newRevert < Integer.MIN_VALUE / 10 || (newRevert <= Integer.MIN_VALUE / 10 && pop < -8)) {
                return 0;
            }
            newRevert = newRevert * 10 + pop;
        }
        return newRevert;
    }


}
