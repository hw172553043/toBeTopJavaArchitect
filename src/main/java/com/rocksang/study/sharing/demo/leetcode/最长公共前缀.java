package com.rocksang.study.sharing.demo.leetcode;

/**
 * @Desc:
 * @Author: Rock.Sang
 * @Date: 2020/1/19 2:28 PM
 * @blame Personal
 */
public class 最长公共前缀 {

    public static void main(String[] args) {

        String[] data = {"flower", "flow", "flight"};
        longestCommonPrefix(data);
    }

    private static String longestCommonPrefix(String[] args) {

        if (args.length == 0) {
            return "";
        }

        String prefix = args[0];
        for (int i = 1; i < args.length; i++) {
            while (args[i].indexOf(prefix) != 0) {
                prefix = prefix.substring(0, prefix.length() - 1);
                if (prefix.isEmpty()) {
                    return "";
                }
            }
        }
        System.out.println(prefix);
        return prefix;
    }


}
