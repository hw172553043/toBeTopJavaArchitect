package com.rocksang.study.sharing.demo.leetcode;

/**
 * @Desc: 判断是否为回文数
 * @Author: Rock.Sang
 * @Date: 2020/1/17 4:58 PM
 * @blame Personal
 */
public class 回文数 {

    public static void main(String[] args) {

        int num1 = -12312;
        System.out.println(num1 + " is "+isPalindromicNumberForInt(num1));

        int num2 = 12344321;
        System.out.println(num2 + " is "+isPalindromicNumberForInt(num2));

        String str1 = "";
        System.out.println(str1 + " is "+isPalindromicNumberForString(str1));

        String str2 = "1";
        System.out.println(str2 + " is "+isPalindromicNumberForString(str2));

        String str3 = "asasdasd";
        System.out.println(str3 + " is "+isPalindromicNumberForString(str3));

        String str4 = "asdffdsa";
        System.out.println(str4 + " is "+isPalindromicNumberForString(str4));

        String str5 = "asdfWfdsa";
        System.out.println(str5 + " is "+isPalindromicNumberForString(str5));
    }

    private static boolean isPalindromicNumberForInt(int value){
        if(value<0){
            return false;
        }

        String valueStr = String.valueOf(value);
        return isPalindromicNumberForString(valueStr);
    }

    private static boolean isPalindromicNumberForString(String value){

        StringBuffer valueBuf = new StringBuffer(value);
        int length = valueBuf.length();
        if(length==0){
            return false;
        }

        if(length==1){
            return true;
        }

        for(int i=0;i<length/2;i++) {
            String firstStr = valueBuf.substring(i,i+1);
            String secondStr = valueBuf.substring(length-i-1,length-i);
            if(!firstStr.equals(secondStr)){
                return false;
            }
        }

        return true;
    }

    public boolean IsPalindrome(int x) {
        // 特殊情况：
        // 如上所述，当 x < 0 时，x 不是回文数。
        // 同样地，如果数字的最后一位是 0，为了使该数字为回文，
        // 则其第一位数字也应该是 0
        // 只有 0 满足这一属性
        if(x < 0 || (x % 10 == 0 && x != 0)) {
            return false;
        }

        int revertedNumber = 0;
        while(x > revertedNumber) {
            revertedNumber = revertedNumber * 10 + x % 10;
            x /= 10;
        }

        // 当数字长度为奇数时，我们可以通过 revertedNumber/10 去除处于中位的数字。
        // 例如，当输入为 12321 时，在 while 循环的末尾我们可以得到 x = 12，revertedNumber = 123，
        // 由于处于中位的数字不影响回文（它总是与自己相等），所以我们可以简单地将其去除。
        return x == revertedNumber || x == revertedNumber/10;
    }


}
