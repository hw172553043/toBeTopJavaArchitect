package com.rocksang.study.sharing.demo.leetcode;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.Map;

/**
 * @Desc:
 * @Author: Rock.Sang
 * @Date: 2020/1/17 11:21 AM
 * @blame Personal
 */
public class 两数之和 {

    public static void main(String[] args) {
        int[] data = {2,7,11,15};
        System.out.println(JSON.toJSONString(twoSum1(data,18)));

        int[] data2 = {2,5,5,15};
        System.out.println(JSON.toJSONString(twoSum2(data2,10)));

        int[] data3 = {2,4,4,15};
        System.out.println(JSON.toJSONString(twoSum3(data3,8)));
    }

    /**
     * 方法一：暴力法
     * 暴力法很简单，遍历每个元素 x，并查找是否存在一个值与target−x 相等的目标元素
     *
     * 复杂度分析：
     * 时间复杂度：O(n^2))
     * 对于每个元素，我们试图通过遍历数组的其余部分来寻找它所对应的目标元素，这将耗费 O(n) 的时间。因此时间复杂度为 O(n^2)
     * 空间复杂度：O(1)
     */
    private static int[] twoSum1(int[] nums, int target) {
        for(int i=0;i<nums.length;i++){
            int firstNum = nums[i];
            for(int j=i+1;j<nums.length;j++){
                int secondNum = nums[j];
                if(firstNum+secondNum==target){
                    return new int[]{i,j};
                }
            }
        }
        throw new IllegalArgumentException("No two sum solution");
    }

    /**
     *
     */
    private static int[] twoSum2(int[] nums, int target){
        Map<Integer,Integer> initData = new HashMap<>();
        for(int i=0;i<nums.length;i++){
            initData.put(nums[i],i);
        }

        for(int i=0;i<nums.length;i++){
            int leftNum = target-nums[i];
            if(initData.containsKey(leftNum) && initData.get(leftNum)!=i){
                // 此处已经解决了重复数字问题
                return new int[]{i,initData.get(leftNum)};
            }
        }
        throw new IllegalArgumentException("No two sum solution");
    }


    private static int[] twoSum3(int[] nums, int target){
        Map<Integer,Integer> initData = new HashMap<>();
        for(int i=0;i<nums.length;i++){
            int leftNum = target-nums[i];
            if(initData.containsKey(leftNum) && initData.get(leftNum)!=i){
                return new int[]{i,initData.get(leftNum)};
            }
            initData.put(nums[i],i);
        }
        throw new IllegalArgumentException("No two sum solution");
    }

}
