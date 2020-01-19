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
}
