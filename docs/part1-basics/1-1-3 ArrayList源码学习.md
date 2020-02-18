## 文章目录

- [一、ArrayList概述](#一、arraylist概述)
    - [1.1 ArrayList简介](#1.1-arraylist简介)
    - [1.2 ArrayList数据结构](#1.2-arraylist数据结构)
- [二、ArrayList源码分析](#二、arraylist源码分析)
    - [2.1 ArrayList继承结构和层次关系](#2.1-arraylist继承结构和层次关系)
    - [2.2 类的属性](#2.2-类的属性)
    - [2.3 构造方法](#2.3-构造方法)
    - [2.4 主要方法](#2.4-主要方法)
         - [2.4.1 get()方法](#2.4.1-get()方法)
         - [2.4.1 get()方法](#2.4.2-set()方法)
         - [2.4.1 get()方法](#2.4.3-add()方法)
         - [2.4.1 get()方法](#2.4.4-remove()方法)
         - [2.4.1 get()方法](#2.4.5-indexof()和lastindexof()方法)
         - [2.4.1 get()方法](#2.4.6-clear()方法)
- [三、总结](#三、总结)

----

### 一、ArrayList概述

#### 1.1 ArrayList简介
ArrayList经常用，今天对它的源码探究一二。
源码顶部有一大串注释，顶部注释参考了其他博客内容如下：

`
List接口的大小可变数组的实现。实现了所有可选列表操作，并允许包括null在内的所有元素。除了实现List接口外，此类还提供一些方法来操作内部用来存储列表的数组的大小。（此类大致上等同于Vector类，除了此类是不同步的。）
size、isEmpty、get、set、iterator和listIterator操作都以固定时间运行。add操作以分摊的固定时间运行，也就是说，添加n个元素需要O(n)时间。其他所有操作都以线性时间运行（大体上讲）。与用于LinkedList实现的常数因子相比，此实现的常数因子较低。
每个ArrayList实例都有一个容量。该容量是指用来存储列表元素的数组的大小。它总是至少等于列表的大小。随着向ArrayList中不断添加元素，其容量也自动增长。并未指定增长策略的细节，因为这不只是添加元素会带来分摊固定时间开销那样简单。
在添加大量元素前，应用程序可以使用ensureCapacity操作来增加ArrayList实例的容量。这可以减少递增式再分配的数量。
注意，此实现不是同步的。如果多个线程同时访问一个ArrayList实例，而其中至少一个线程从结构上修改了列表，那么它必须保持外部同步。（结构上的修改是指任何添加或删除一个或多个元素的操作，或者显式调整底层数组的大小；仅仅设置元素的值不是结构上的修改。）这一般通过对自然封装该列表的对象进行同步操作来完成。如果不存在这样的对象，则应该使用Collections.synchronizedList方法将该列表“包装”起来。这最好在创建时完成，以防止意外对列表进行不同步的访问：
List list = Collections.synchronizedList(new ArrayList(…));
此类的iterator和listIterator方法返回的迭代器是快速失败的：在创建迭代器之后，除非通过迭代器自身的remove或add方法从结构上对列表进行修改，否则在任何时间以任何方式对列表进行修改，迭代器都会抛出ConcurrentModificationException。因此，面对并发的修改，迭代器很快就会完全失败，而不是冒着在将来某个不确定时间发生任意不确定行为的风险。
注意，迭代器的快速失败行为无法得到保证，因为一般来说，不可能对是否出现不同步并发修改做出任何硬性保证。快速失败迭代器会尽最大努力抛出ConcurrentModificationException。因此，为提高这类迭代器的正确性而编写一个依赖于此异常的程序是错误的做法：迭代器的快速失败行为应该仅用于检测bug。
此类是Java Collections Framework的成员。
`

顶部的注释给我们透露了几点重要信息：
- ArrayList是List接口的大小可变数组的实现；
- ArrayList允许null元素；ArrayList的容量可以自动增长；
- ArrayList不是同步的；
- ArrayList的iterator和listIterator方法返回的迭代器是fail-fast的

#### 1.2 ArrayList数据结构
结构图：
![ArrayList数据结构](https://img-blog.csdnimg.cn/20190502161202647.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3UwMTAyNTAyNDA=,size_16,color_FFFFFF,t_70)

容量：CAPACITY ； 实际大小：size；

ArrayList底层的数据结构就是数组，数组元素类型为Object类型，即可以存放所有类型数据。我们对ArrayList类的实例的所有的操作底层都是基于数组的。


----

### 二、ArrayList源码分析

#### 2.1 ArrayList继承结构和层次关系

#### 2.2 类的属性

#### 2.3 构造方法

#### 2.4 主要方法

##### 2.4.1 get()方法

##### 2.4.2 set()方法

##### 2.4.3 add()方法

##### 2.4.4 remove()方法

##### 2.4.5 indexOf()和lastIndexOf()方法

##### 2.4.6 clear()方法

----

### 三、总结







