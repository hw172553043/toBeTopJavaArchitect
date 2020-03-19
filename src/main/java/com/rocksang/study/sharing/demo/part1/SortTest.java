package com.rocksang.study.sharing.demo.part1;

public class SortTest {


    public static void main(String[] args) {
        findMaxIndex();
    }

    private static int findMaxIndex(){

        int[] data = {1,3,5,7,9,8,4,2};
        int min = 0;
        int max = data.length-1;
        for(;min<max;){
            int middle = (max+min)/2;

            if(data[middle]>data[middle+1]){
                // 峰值在左侧
                max = middle;
            }else{
                // 峰值在右侧
                min = middle+1;
            }
        }
        System.out.println(min);
        System.out.println(max);
        return min;
    }
}




