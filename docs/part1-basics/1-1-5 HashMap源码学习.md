
## 文章目录
- [一、前言](#一、前言)
- [二、Map 概述](#二、map-概述)
    - [2.1 map大家族](#2.1-map大家族)
    - [2.2 map大家族简版](#2.2-map大家族简版)
    - [2.3 map概述](#2.3-map概述)
    - [2.4 HashMap数据结构](#2.4-hashmap数据结构)
- [三、HashMap源码分析](#三、hashmap-源码分析)
    - [3.1 Demo测试](#3.1-demo-测试)
    - [3.2 认知HashMap](#3.2-认知-hashmap)
    - [3.3 源码分析](#3.3-代码分析)
        - [3.3.1 HashMap构造方法](#3.3.1-初始化——hashmap-的构造方法)
        - [3.3.2 插入put()方法](#3.3.2-插入——put()方法)
        - [3.3.3 遍历](#3.3.3-遍历)
        - [3.3.4 get()](#3.3.4-get())
        - [3.3.5 remove()](#3.3.5-remove())
        - [3.3.6 关于ConcurrentModificationxception](#3.3.6-关于-concurrentmodificationexception)
- [四、总结](#四、总结)

### 一、前言
在 Java 的数据结构基础里，HashMap 无疑是一个非常重要的数据结构。这一篇文章中我们来学习并分析一下其内部的实现原理。
文章将基于 JDK 1.8 进行分析，暂时不考虑不同版本之间的差异。

文章先是总结了 Java 中 Map 大家族的类图，再总结了 HashMap 的概括类图，这让我们对 Map 以及 HashMap 有一个整体的轮廓。
有了一个轮廓后再去看各个类的实现细节就会产生迷失在细节里的情况，也能大概知道各个类之间的关联性。

### 二、Map 概述

#### 2.1 Map大家族
首先我们列举出 Map 相关大部分类，并画成如下类图 Map 大家族。通过对 Map 大家族类图使得我们可以在脑海中对这些数据结构有一个相对比较完整的轮廓。
即哪些是重点，哪些是常用的类，我们在使用这些数据结构时，它在其家族成员中大概是在什么位置，它们都有哪一些基本特性和适用场景，我们在心里应该要有一个拿捏。

![Map大家族](https://upload-images.jianshu.io/upload_images/5828513-4aa28fb9a8674ae9.jpg)

从上图看 Map 大族还是挺多成员的，而实际应用中我们常用的其实没有这么多，所以在此基础上，我精减了一下:

#### 2.2 Map大家族简版
把常用的且重要的抽了一下，得到一个简版的，如下。

![Map大家族简版](https://upload-images.jianshu.io/upload_images/5828513-d7b70f6d6af25e27.jpg)

这个简版的图看起来是不是舒服多了，事实上只要我们熟练常握了上述这些Map数据结构的特性以及原理，
那可以说在工作中的应用应当是游刃有余了。

#### 2.3 Map概述
下面表格是对这些常用 Map 的一个概述。

Map  | 概述
------------- | -------------
HashMap  | 基于Map接口实现、允许null键/值、非同步、不保证有序(比如插入的顺序)、也不保证序不随时间变化
LinkedHashMap | 	LinkedHashMap是Hash表和链表的实现，并且依靠着双向链表保证了迭代顺序是插入的顺序
HashTable	 | 很大程度上和 HashMap 的实现差不多，不同的是HashTable 基于 Dictionary 类实现，key 和 value 都不允许为 null，方法都是同步的
TreeMap | 	使用红黑树实现，保证了 key 的大小排序性
ConcurrentHashMap | 	ConcurrentHashMap 是一个并发散列映射表的实现，它允许完全并发的读取，并且支持给定数量的并发更新。相比于 HashTable 和用同步包装器包装的 Collections.synchronizedMap(new HashMap())，ConcurrentHashMap 拥有更高的并发性

#### 2.4 HashMap数据结构
![](https://upload-images.jianshu.io/upload_images/5459476-a4fe2584887e6eb7.png)

HashMap的底层就是一个数组，数组中每一项又是一个链表，当新建一个HashMap时候，就会初始化一个数组，
查看源码如下，直接看重点，table = new Entry[capacity]; 创建一个Entry数组，也就是上面的table ,
这个Entry结构就是static的包含key value 还有一个next的指针，指向下一个元素的引用，也就构成了链表
```java
public HashMap(int initialCapacity, float loadFactor) {
    if (initialCapacity < 0)
        throw new IllegalArgumentException("Illegal initial capacity: " +
                                           initialCapacity);
    if (initialCapacity > MAXIMUM_CAPACITY)
        initialCapacity = MAXIMUM_CAPACITY;
    if (loadFactor <= 0 || Float.isNaN(loadFactor))
        throw new IllegalArgumentException("Illegal load factor: " +
                                           loadFactor);

    // Find a power of 2 >= initialCapacity
    int capacity = 1;
    while (capacity < initialCapacity)
        capacity <<= 1;

    this.loadFactor = loadFactor;
    threshold = (int)Math.min(capacity * loadFactor, MAXIMUM_CAPACITY + 1);
    table = new Entry[capacity];
    useAltHashing = sun.misc.VM.isBooted() &&
            (capacity >= Holder.ALTERNATIVE_HASHING_THRESHOLD);
    init();
}
```
详细源码接下来我们慢慢分析

### 三、HashMap 源码分析

#### 3.1 demo 测试
分析之前先来看一段 demo，除了常规的插入字符串，还重复插入了 null 引用和空字串。
```java
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put(null,null);
        hashMap.put("","");
        hashMap.put(null,null);
        hashMap.put("","");
        hashMap.put("语文","张大爷同学");
        hashMap.put("数学","李大节同学");
        hashMap.put("英语","王大妈同学");
        hashMap.put("体育","刘部长同学");
        hashMap.put("物理","吴先生同学");
        hashMap.put("化学","成龙同学");
        hashMap.put("地理","胡歌同学");
        hashMap.put("生物","韩同学");
        hashMap.put("自然","方同学");
        hashMap.put("政治","马同学");
        hashMap.put("音乐","舒同学");
        hashMap.put("美术","百同学");

        Log.d("HashMap","testHashMap: hashMap size = " + hashMap.size());
        Set<Map.Entry<String,String>> entries = hashMap.entrySet();
        for (Map.Entry<String,String> entry : entries) {
            Log.d("HashMap", "testHashMap: key = " + entry.getKey() + ";value = " + entry.getValue());
        }
```
下面来看看这段代码的输出结果
```java
    testHashMap: hashMap size = 14
    testHashMap: key = 物理;value = 吴先生同学
    testHashMap: key = null;value = null
    testHashMap: key = 政治;value = 马同学
    testHashMap: key = 自然;value = 方同学
    testHashMap: key = ;value = 
    testHashMap: key = 美术;value = 百同学
    testHashMap: key = 数学;value = 李大节同学
    testHashMap: key = 地理;value = 胡歌同学
    testHashMap: key = 生物;value = 韩同学
    testHashMap: key = 体育;value = 刘部长同学
    testHashMap: key = 化学;value = 成龙同学
    testHashMap: key = 语文;value = 张大爷同学
    testHashMap: key = 英语;value = 王大妈同学
    testHashMap: key = 音乐;value = 舒同学
```
demo 中我们一共插入了 16 个元素，但实际 size 只有 14 个，
也就是相同的 key 只能有一个，且 null 不等于空字串。

#### 3.2 认知 HashMap
在概述部分，我们看到了 Map 大家族的大致轮廓。在这里我们再来看一下 HashMap 的继承关系以及内部结构的概括图，
概括图同样也是让我们对 HashMap 有一个全貌的了解。
![HashMap](https://upload-images.jianshu.io/upload_images/5828513-f2df28de1a8ede49.jpg)

下面对这个概要类图作一个稍微详细的描述：
1. HashMap 继承自抽象类 AbstractMap，而 AbstractMap 以及 HashMap 本身又都实现了接口 Map。
   Map 接口规范了作为一个 key-value 类你应该有哪一些方法，其中最重要的是 get()，put()，remove()
   以及用来管理内部数据的视图keySet(),values()，entrySet()。同时还定义用于抽象 key-value 
   的Entry 接口。顺便提一下，从 JDK 1.8 开始，通过关键字 default，Map 接口中也提供了一些方法的默认实现。
2. AbstractMap 抽象了一个最简单实现 Map 接口的骨架，该类同时定义了 keySet 和 values 视图 。
   视图主要是用于实现如何遍历，其主要是起到缓存的作用。
3. HashMap 自然是具体的实现类，其定义了具体的成员变量，每个成员变量都非常的重要，分析的过程中，
   我们应该要掌握每一个成员变量的定义以及作用。其中的 Node 类封装了 Key-Value 的节点，
   也是存储 key-value 的实际对象。这里先简单了解一下各个成员变量的定义。

变量名  | 定义
------------- | -------------
table  | 其定义为 Node<K,V>[]，即用来存储 key-value 的节点对象。在 HashMap 中它有个专业的叫法 buckets ，中文叫作桶。
entrySet  | 同时封装了 keySet 和 values 的视图，作用同 AbstractMap 中的 KeySet 和 values 视图一样
size  | 容器中实际存放 Node 的大上
modCount  | HashMap 在结构上被修改的次数，结构修改是指改变HashMap中映射的次数，或者以其他方式修改其内部结构(例如，rehash)。此字段用于使HashMap集合视图上的迭代器快速失败。(著名的ConcurrentModificationException便与此有关)。
threshold  | 下一个需要扩容的阈值，其大小 = capacity * load factor，这里的 capacity 便是当前 buckets 的容量大小，一般情况即是 table 数组的大小。load factor 的定义在下面
loadFactor  | buckets 被填满的比例因子，实际上主要是计算得到 threshold


#### 3.3 代码分析
说到代码分析，相对来说会难一点，但我们不要畏难，复杂的事情也可以简单化的。
我们先不考虑 HashMap 有多复杂，有多少多少的功能，我们且以 demo 为主线来分析其主要的路线，
然后在这个基础上再补齐相关功能的分析要简单的多。

根据上面的 demo 测试，我们先来看看时序图。
![demo时序图](https://upload-images.jianshu.io/upload_images/5828513-e35e72d0a29addd9.jpg)

时序图里一共 8 个步骤，但其主要其实可以分成 3 个部分：初始化、插入以及遍历。

##### 3.3.1 初始化——HashMap 的构造方法
初始化，也就是 HashMap 的构造方法。
```java
    /**
     * 构造一个空的 HashMap，其 capacity 默认为 16，load factor 默认为 0.75
     */
    public HashMap() {
        this.loadFactor = DEFAULT_LOAD_FACTOR; 
    }
```
构造方法做的事情很简单，就是确定容量大小以及比例因子的大小。
构造方法还有 2 个比较重要的重载方法，一起来看一下。
```java
    /**
    * 指定 capacity 大小，但 load factor 默认为 0.75
    */
    public HashMap(int initialCapacity) {
            this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }
    /**
    * 同时指定 capacity 和 load factor 的大小，并且同时计算出 threshold 的值 
    */
    public HashMap(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0)
            throw new IllegalArgumentException("Illegal initial capacity: " +
                                               initialCapacity);
        if (initialCapacity > MAXIMUM_CAPACITY)
            initialCapacity = MAXIMUM_CAPACITY;
        if (loadFactor <= 0 || Float.isNaN(loadFactor))
            throw new IllegalArgumentException("Illegal load factor: " +
                                               loadFactor);
        this.loadFactor = loadFactor;
        // 约束 threshold 的大小应该为 2 的 n 次幂
        this.threshold = tableSizeFor(initialCapacity);
    }
```
通过 HashMap 的构造方法其实给了我们一个优化思路，就是根据不同的应用场景，
如果我们能够预期其大小或者说能够预期其未来的变化率，那么我们应该初始化时就指定好 capacity 和 loadFactor，
那么就能有效减少内存的分配和 扩容的分配，从而提升 HashMap 的使用效率。

##### 3.3.2 插入——put()方法
```java
    /**
    * 使 key 和 value 产生关联，但如果有相同的 key 则新的会替换掉旧的
    */
    public V put(K key, V value) {
        return putVal(hash(key), key, value, false, true);
    }
```
上面的代码里，进行了 2 步操作，先通过 hash() 函数对 key 求 hash 码，然后再进一步调用 putVal()。
那么先来分析 hash() 函数吧。
```java
    static final int hash(Object key) {
        int h;
        // 如果为 null 则返回的就是 0，否则就是 hashCode 异或上 hashCode 无符号右移 16 位
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }
```
注释里有简要说明了 hash 值的产生方法，得到的结果就是 hashCode 的高16 位不变，低16 位与高16 位做一个异或。
这样做的目的是同时把高 16 位和低 16 位的影响都考虑进来以减少小容量 HashMap 的散列冲突。
当然，这也与 HashMap 中计算散列后的 index 的方法有关。

计算散列 index 的实现在 putVal() 方法里，不防先来看一看。
```java
i = (n - 1) & hash
```
可以看到，HashMap 并没有采用 %(取余) 这种简单粗暴的实现，而是使用 &(按位与) 来分布散列 index 的生成，
其主要目的当然是尽量减少碰撞冲突。相比较来说 % 的碰撞冲突应该是非常高的。

再来说上面的为什么要同时考虑到高 16 位与低 16 位的影响。

capacity 的容量大小是 2 的 n 次幂，试想一下如果不做异或，而只是用原 hashcode ，那么在小 map 中，
能起作用的就永远只是低位，虽然 hashCode 的生成已经分布的很平衡了，但相比较而前，同时考虑到高位与低位的影响，
最后计算出的散列 index 发生碰撞的冲突肯定要小的多。
关于 hash() 方法的实现，其实设计者也作了比较详尽的解释，比如其还提到，没有采用更复杂的生成 hash 方法，
也是出于效率考虑。而对于大的 map 发生的散列冲突，其采用了红黑树来提高了查询的效率。
感兴趣的可以看看原设计者的注释。

`
Computes key.hashCode() and spreads (XORs) higher bits of hash to lower. Because the table uses power-of-two masking, sets of hashes that vary only in bits above the current mask will always collide. (Among known examples are sets of Float keys holding consecutive whole numbers in small tables.) So we apply a transform that spreads the impact of higher bits downward. There is a tradeoff between speed, utility, and quality of bit-spreading. Because many common sets of hashes are already reasonably distributed (so don't benefit from spreading), and because we use trees to handle large sets of collisions in bins, we just XOR some shifted bits in the cheapest possible way to reduce systematic lossage, as well as to incorporate impact of the highest bits that would otherwise never be used in index calculations because of table bounds.
` 

hash() 就了解到这里，来进一步看看 putVal() 方法。
```java
final V putVal(int hash, K key, V value, boolean onlyIfAbsent,boolean evict) {
    Node<K,V>[] tab; Node<K,V> p; int n, i;
    // tab为空则通过resize()创建，插入第 1 个值的时候发生
    if ((tab = table) == null || (n = tab.length) == 0)
        n = (tab = resize()).length;
    // 计算散列 index，没有冲突直接插入
    if ((p = tab[i = (n - 1) & hash]) == null)
        tab[i] = newNode(hash, key, value, null);
    // 有冲突
    else {
        Node<K,V> e; K k;
       // 存在 hash 值相同且 key 相等的，先记录下来，后面的插入步骤会使用新值将旧值替换掉
        if (p.hash == hash && ((k = p.key) == key || (key != null && key.equals(k))))
            e = p;
        // 该节点为树，散列冲突过长，大于 TREEIFY_THRESHOLD = 8 时会转换成树
        else if (p instanceof TreeNode)
            e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
        // 该节点为链表
        else {
            for (int binCount = 0; ; ++binCount) {
                if ((e = p.next) == null) {
                    // 插入到链尾
                    p.next = newNode(hash, key, value, null);
                    // 链表的长度超过 TREEIFY_THRESHOLD - 1 则转换成树
                    if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
                        treeifyBin(tab, hash);
                    break;
                }
                // 对链表中的相同 hash 值且 key 相同的进一步作检查
                if (e.hash == hash &&
                    ((k = e.key) == key || (key != null && key.equals(k))))
                    break;
                p = e;
            }
        }
        // 插入
        // existing mapping for key
        if (e != null) { 
            // 取出旧值，onlyIfAbsent此时为 false，所以不管 oldValue 有与否，都拿新值来替换
            V oldValue = e.value;
            if (!onlyIfAbsent || oldValue == null)
                e.value = value;
            afterNodeAccess(e);
            return oldValue;
        }
    }
    ++modCount;
    // 超过阈值 threshold = capacity * factor，调用 resize() 进行扩容
    if (++size > threshold)
        resize();
    afterNodeInsertion(evict);
    return null;
}
```
putVal() 里面作的事情比较多，每一个重要的过程都写在了注释里面。但这里还是来总结一下吧：
1. 通过对 hash(key) 计算出来的 hash 值，计算出散列 index。
2. 如果没碰撞冲突直接放到 table 里。
3. 如果碰撞冲突了，先以链表的形式解决冲突，并把新的 node 插入到链尾。
4. 如果碰撞冲突导致链表过长(>= TREEIFY_THRESHOLD)，就把链表转换成红黑树，提高查询效率。
5. 如果节点已经存在，即key的 hash() 值相等且 key 的内容相等，就替换 old value，从而保证 key 的唯一性
6. 如果 table 满了( > load factor*capacity)，就要扩容resize()。

在 putVal() 方法中，其中有 3 个关键的调用：putTreeVal()，treeifyBin()以及resize()。
putTreeVal()和treeifyBin()分别涉及到了红黑二叉树的插入以及初始化，这个就先不深入展开了。

而对于 resize() 我们还是要深入了解一下的，否则我们怎么能体会得到扩容的代价到底有多大呢？
```java
final Node<K,V>[] resize() {
    Node<K,V>[] oldTab = table;
    int oldCap = (oldTab == null) ? 0 : oldTab.length;
    int oldThr = threshold;
    int newCap, newThr = 0;
    if (oldCap > 0) {
        // 超过最大值就不再扩充 table，但并不表示不能插入了，只是后面的只能碰撞冲突了
        if (oldCap >= MAXIMUM_CAPACITY) {
            threshold = Integer.MAX_VALUE;
            return oldTab;
        }
        // 没超过最大值，就扩充为原来的 2 倍。主要是容量以及阈值都为原来的 2倍。容量和阈值本身就都必须是 2 的幂，所以扩容的倍数必须是2的倍数，那么扩2倍就非常合理了。
        else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY && oldCap >= DEFAULT_INITIAL_CAPACITY)
            newThr = oldThr << 1; // double threshold
    }
    else if (oldThr > 0) // initial capacity was placed in threshold
        newCap = oldThr;
    else {               // zero initial threshold signifies using defaults
        newCap = DEFAULT_INITIAL_CAPACITY;
        newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
    }
    // 计算新的resize阈值
    if (newThr == 0) {
        float ft = (float)newCap * loadFactor;
        newThr = (newCap < MAXIMUM_CAPACITY && ft < (float)MAXIMUM_CAPACITY ?
                  (int)ft : Integer.MAX_VALUE);
    }
    threshold = newThr;
    @SuppressWarnings({"rawtypes","unchecked"})
    // 重新分配内存
    Node<K,V>[] newTab = (Node<K,V>[])new Node[newCap];
    table = newTab;
    if (oldTab != null) {
        // 把原来 tables 中的每个节点都移动到新的 tables 中
        for (int j = 0; j < oldCap; ++j) {
            Node<K,V> e;
            if ((e = oldTab[j]) != null) {
                oldTab[j] = null;
                if (e.next == null)// 没有冲突，那重新计算下位置
                    newTab[e.hash & (newCap - 1)] = e;
                else if (e instanceof TreeNode)// 冲突的是一棵树节点，分裂成 2 个树，或者如果树很小就转成链表
                    ((TreeNode<K,V>)e).split(this, newTab, j, oldCap);
                else { // preserve order，冲突构成的是链表
                    Node<K,V> loHead = null, loTail = null;
                    Node<K,V> hiHead = null, hiTail = null;
                    Node<K,V> next;
                    do {
                        next = e.next;
                        // 索引不变
                        if ((e.hash & oldCap) == 0) {
                            if (loTail == null)
                                loHead = e;
                            else
                                loTail.next = e;
                            loTail = e;
                        }
                        // 原索引+oldCap
                        else {
                            if (hiTail == null)
                                hiHead = e;
                            else
                                hiTail.next = e;
                            hiTail = e;
                        }
                    } while ((e = next) != null);
                    // 原索引放到 tables 里
                    if (loTail != null) {
                        loTail.next = null;
                        newTab[j] = loHead;
                    }
                    // 原索引+oldCap放到  tables 里
                    if (hiTail != null) {
                        hiTail.next = null;
                        newTab[j + oldCap] = hiHead;
                    }
                }
            }
        }
    }
    return newTab;
}
```
resize() 里面关键做了2个耗时耗力的事情：
- 一是分配了 2 倍的 tables 的空间，最糟糕的情况是扩容完后不再有插入了。
- 二是将旧的 tables 放入新的 tables 中，这里就包括了 index 的重新计算，链表冲突重新分布，
  tree 冲突分裂或者转化成链表。算法的细节展示在了注释里面了，有兴趣的同学可以跟着代码推导一下，
  没兴趣也不影响理解。

##### 3.3.3 遍历
```java
    /**
     * Returns a {@link Set} view of the mappings contained in this map.
     * The set is backed by the map, so changes to the map are
     * reflected in the set, and vice-versa.  If the map is modified
     * while an iteration over the set is in progress (except through
     * the iterator's own <tt>remove</tt> operation, or through the
     * <tt>setValue</tt> operation on a map entry returned by the
     * iterator) the results of the iteration are undefined.  The set
     * supports element removal, which removes the corresponding
     * mapping from the map, via the <tt>Iterator.remove</tt>,
     * <tt>Set.remove</tt>, <tt>removeAll</tt>, <tt>retainAll</tt> and
     * <tt>clear</tt> operations.  It does not support the
     * <tt>add</tt> or <tt>addAll</tt> operations.
     *
     * @return a set view of the mappings contained in this map
     */
    public Set<Map.Entry<K,V>> entrySet() {
        Set<Map.Entry<K,V>> es;
        return (es = entrySet) == null ? (entrySet = new EntrySet()) : es;
    }
```
这段代码实际上就是 new 了一个 EntrySet 对象，而上面的意思主要表达的是这是由 map 映射的 set，
对 map 的修改将会反映到 set 中，反之亦然。怎么做到的呢？当然通过 EntrySet 也只能是修改 value，
即通过 setValue()。如果在遍历过程中进行删除的话，也是会触发 ConcurrentModificationException 的。

再来看看 EntrySet 的实现，来研究一下它是怎么做到映射的。
```java
final class EntrySet extends AbstractSet<Map.Entry<K,V>> {
    public final Iterator<Map.Entry<K,V>> iterator() {
        return new EntryIterator();
    }
}
```
如上代码，只列表出了一个关键的实现，即 iterator() 接口。Java 中的 foreach 之所以能够对 Collection 类进行遍历，
其原理就是要 Collection 的子类实现 iterator() 接口并返回一个具体的迭代器，遍历时通过 hasNext()判断其是否还有元素，
而通过 next() 来获取下一个元素。这也是迭代器设计模式的一个实现。

那么，再来看看 EntryIterator 的 next() 实现。
```java
// EntryIterator 又继承了 HashIterator
final class EntryIterator extends HashIterator
        implements Iterator<Map.Entry<K,V>> {
        public final Map.Entry<K,V> next() { return nextNode(); }
}
// HashIterator 的定义，这里只列出了HashIterator构造方法 nextNode() 方法
 abstract class HashIterator {
   ......
  HashIterator() {
            expectedModCount = modCount;
            Node<K,V>[] t = table;
            current = next = null;
            index = 0;
            // 初始时 next 为 tables 中的第一个节点的第一个 node
            if (t != null && size > 0) { // advance to first entry
                do {} while (index < t.length && (next = t[index++]) == null);
            }
        }
   final Node<K,V> nextNode() {
            Node<K,V>[] t;
            Node<K,V> e = next;
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
            if (e == null)
                throw new NoSuchElementException();
            // 先获取当前 next 节点的 next
            if ((next = (current = e).next) == null && (t = table) != null) {
                // 如果为空则再到 tables 中的下一个节点中的 Node 中去找
                do {} while (index < t.length && (next = t[index++]) == null);
            }
            return e;
        }
......
}
```
从上面代码以及增加的注释知道，首先是初始化HashIterator时，next 为 tables 中的第 1 个节点，
后面的遍历过程中会先看当前这个节点是否已经没有 next 了，如果没有了再 index + 1 取下一个节点，
以此类推来遍历完所有的节点。

entrySet()的遍历就分析到这里了，entrySet()遍历只是遍历方式中的其中的一种，

其他几种我们也一并列出来了解一下 。
```java
        //方法一：通过 Map.keySet 遍历 key 和 value，多了个 getValue 的过程
        for (String key : hashMap.keySet()) {
            System.out.println("Key: " + key + " Value: " + hashMap.get(key));
        }

        //方法二：通过 Map.values() 遍历所有的 value，但不能遍历 key
        for (String v : hashMap.values()) {
            System.out.println("The value is " + v);
        }

        //方法三：通过 Map.entrySet 使用 iterator 遍历 key 和 value，而 iterator 又是要取出 entrySet的，相当于又多了一步。但其最大的特点是适用于一边遍历一边删除的场景。不需要用一个 set 先保存下来再删除了。
        Iterator iterator = hashMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry<String, String>) iterator.next();
            System.out.println("Key: " + entry.getKey() + " Value: " + entry.getValue());
            // 遍历完成后马上进行删除
            iterator.remove();
        }
        // 方法四：通过 entrySet 进行遍历，直接遍历出key和value。对于 size 比较大的情况下，又需要全部遍历的时候，效率是最高的。
        for (Map.Entry<String, String> entry : entries) {
            System.out.println("testHashMap: key = " + entry.getKey() + ";value = " + entry.getValue());
        }
```     
观察这 4 种遍历方式会发现，只有方法三是可以在遍历过程中通过迭代器进行删除的，
其他的方法都会报 ConcurrentModificationException，而方法四是最快的。

至此，HashMap 的初始化--插入--再到遍历的主路径已经分析完了。

可是对于 HashMap 来说还没有完，还有我们的 get() 操作和 remove() 操作。

##### 3.3.4 get()
```java
public V get(Object key) {
    Node<K,V> e;
    return (e = getNode(hash(key), key)) == null ? null : e.value;
}

final Node<K,V> getNode(int hash, Object key) {
    Node<K,V>[] tab; 
    Node<K,V> first, e; 
    int n; 
    K k;
    if ((tab = table) != null && (n = tab.length) > 0 &&
        (first = tab[(n - 1) & hash]) != null) {
        // 命中
        if (first.hash == hash && // always check first node
            ((k = first.key) == key || (key != null && key.equals(k))))
            // 直接命中了 tables 中的 node
            return first;
        // 未命中 tables 中的 node
        if ((e = first.next) != null) {// 存在碰撞冲突的情况
            // 如果是红黑树
            if (first instanceof TreeNode)
                return ((TreeNode<K,V>)first).getTreeNode(hash, key);
            // 否则认为是链表
            do {
                if (e.hash == hash &&
                    ((k = e.key) == key || (key != null && key.equals(k))))
                    return e;
            } while ((e = e.next) != null);
        }
    }
    // 未命中返回 null
    return null;
}
```
果然在理解了 put() 的基础上，再来看 get() 就轻松多了。一切都在注释中，就不重复了。

##### 3.3.5 remove()
```java
    public V remove(Object key) {
        Node<K,V> e;
        return (e = removeNode(hash(key), key, null, false, true)) == null ?
            null : e.value;
    }
    
    final Node<K,V> removeNode(int hash, Object key, Object value,
                               boolean matchValue, boolean movable) {
        Node<K,V>[] tab; Node<K,V> p; int n, index;
        // 第一步：先查找
        if ((tab = table) != null && (n = tab.length) > 0 &&
            (p = tab[index = (n - 1) & hash]) != null) {
           // 命中
            Node<K,V> node = null, e; K k; V v;
            if (p.hash == hash &&
                ((k = p.key) == key || (key != null && key.equals(k))))
               // 在 tables 中就命中了
                node = p;
            else if ((e = p.next) != null) {
                if (p instanceof TreeNode)
                    // 在红黑树中找
                    node = ((TreeNode<K,V>)p).getTreeNode(hash, key);
                else {
                   // 在链表中找
                    do {
                        if (e.hash == hash &&
                            ((k = e.key) == key ||
                             (key != null && key.equals(k)))) {
                            node = e;
                            break;
                        }
                        p = e;
                    } while ((e = e.next) != null);
                }
            }
           // 第二步：删除
            if (node != null && (!matchValue || (v = node.value) == value ||
                                 (value != null && value.equals(v)))) {
                // 找到了就要删除掉
                if (node instanceof TreeNode)
                    // 从树中移除
                    ((TreeNode<K,V>)node).removeTreeNode(this, tab, movable);
                else if (node == p)
                    // 从tables节点中删除
                    tab[index] = node.next;
                else
                    // 从链表中删除
                    p.next = node.next;
                ++modCount;
                --size;
                afterNodeRemoval(node);
                return node;
            }
        }
       // 未命中直接返回 null
        return null;
    }
```
同理，这里理解 remove() 的实现也是非常轻松的一件事情。详细的过程都在代码的注释里，
其也是分了两个大步骤进行的，先查找再删除，而且查找的过程与 get() 实现非常类似。

##### 3.3.6 关于 ConcurrentModificationException
在 HashMap 的代码中有很多地方都可能会发生 ConcurrentModificationException，
从代码上看其原因是 modCount != expectedModCount。那这又代表了什么呢？
这里以 HashIterator的 next() 为例来分析。贴一下相关代码。
```java
        HashIterator() {
            expectedModCount = modCount;
           ......
        }
        final Node<K,V> nextNode() {
            ......
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
            ......
            return e;
        }
```
在遍历时，foreach 会调用 Collection 的 iterator() 接口，而从 EntrySet 的实现中我们知道每次遍历都会 new 一个新的 Iterator ，
也就是说 HashIterator 每次遍历时都会被初始化。有了这个基础，我们再来看一看其发生的过程。
1. HashIterator 在初始化时，令 modCount 赋值给了 expectedModCount，这个时候也进行了迭代的时候。
2. 当 HashMap 发生 put() 或者 remove() 时都会修改到 modCount 的值
3. 一旦 modCount 的值被修改，那么再遍历到 nextNode() 时就会发生 ConcurrentModificationException 了。

至此，HashMap 中关于初始化，遍历，put，get , remove 以及 ConcurrentModificationException 产生的原因都分析完了。

### 四、总结
1. HashMap 中的 index 的计算是扰动了 hashCode ，并且通过位运算 & 来计算的，
   这也是因为其长度为 2 的 n 次幂才能通过位运算来计算的。
2. 关于碰撞冲突，可能会连接成一个链表。当链表长度过长会将链表转成红黑二叉树，默认的长度阈值是 8 个。
3. 关于扩容，默认容量是 16 个，当容量到达当前容量 * 比例因子时，就会发生扩容。
   默认的比例因子是 0.75，扩容时是扩大原 tables 的 1 倍。扩容的代价是比较大的，内存是扩充一倍，
   且元素的存储都要进行相应的调整。
4. 关于遍历，遍历时一般不能再修改 HashMap ，否则可能会造成 ConcurrentModificationException。

