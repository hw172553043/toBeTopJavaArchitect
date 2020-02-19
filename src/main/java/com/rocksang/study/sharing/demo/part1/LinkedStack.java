package com.rocksang.study.sharing.demo.part1;

import java.util.LinkedList;

/**
 * @Desc:
 * @Author: Rock.Sang
 * @Date: 2020/2/19 5:04 PM
 * @blame Personal
 */
public class LinkedStack {

    private LinkedList<Object> linkedList;

    public LinkedStack() {
        linkedList = new LinkedList<Object>();
    }

    //压入数据
    public void push(Object e) {
        linkedList.push(e);
    }

    //弹出数据，在Stack为空时将抛出异常
    public Object pop() {
        return linkedList.pop();
    }

    //检索栈顶数据，但是不删除
    public Object peek() {
        return linkedList.peek();
    }

}
