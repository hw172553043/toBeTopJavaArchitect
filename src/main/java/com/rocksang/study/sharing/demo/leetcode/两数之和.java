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
     * 方法二：两遍哈希表
     * 为了对运行时间复杂度进行优化，我们需要一种更有效的方法来检查数组中是否存在目标元素。如果存在，我们需要找出它的索引。保持数组中的每个元素与其索引相互对应的最好方法是什么？哈希表。
     *
     * 通过以空间换取速度的方式，我们可以将查找时间从 O(n) 降低到 O(1)。
     * 哈希表正是为此目的而构建的，它支持以 近似 恒定的时间进行快速查找。我用“近似”来描述，是因为一旦出现冲突，查找用时可能会退化到 O(n)。
     * 但只要你仔细地挑选哈希函数，在哈希表中进行查找的用时应当被摊销为 O(1)。
     *
     * 一个简单的实现使用了两次迭代。在第一次迭代中，我们将每个元素的值和它的索引添加到表中。然后，在第二次迭代中，我们将检查每个元素所对应的目标元素（target - nums[i]）是否存在于表中。
     * 注意，该目标元素不能是 nums[i] 本身！
     *
     * 时间复杂度：O(n)O
     * 我们只遍历了包含有 nn 个元素的列表一次。在表中进行的每次查找只花费 O(1)O(1) 的时间。
     *
     * 空间复杂度：O(n)
     * 所需的额外空间取决于哈希表中存储的元素数量，该表最多需要存储 nn 个元素。
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


    /**
     * 方法三：一遍哈希表
     * 事实证明，我们可以一次完成。在进行迭代并将元素插入到表中的同时，我们还会回过头来检查表中是否已经存在当前元素所对应的目标元素。如果它存在，那我们已经找到了对应解，并立即将其返回。
     *
     * 时间复杂度：O(n)
     * 我们只遍历了包含有 nn 个元素的列表一次。在表中进行的每次查找只花费 O(1)的时间。
     *
     * 空间复杂度：O(n)
     * 所需的额外空间取决于哈希表中存储的元素数量，该表最多需要存储 nn 个元素。
     */
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
