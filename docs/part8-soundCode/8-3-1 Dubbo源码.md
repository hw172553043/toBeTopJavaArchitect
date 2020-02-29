## DUBBO 源码学习

- [一、服务是如何发布的](#服务是如何发布的)   
    - [1.1 服务是如何发布的](#服务是如何发布的)
- [二、服务是如何引用的](#服务是如何引用的)
- [三、注册中心分析](#注册中心分析)
- [四、集群负载均衡算法的实现](#集群负载均衡算法的实现)
- [五、优雅停机原理及在SpringBoot中遇到的问题](#优雅停机原理及在SpringBoot中遇到的问题)




### 服务是如何发布的

### 服务是如何引用的

### 注册中心分析

### 集群负载均衡算法的实现

### 优雅停机原理及在SpringBoot中遇到的问题








**1、Random LoadBalance (随机)**

算法源码：
```java
    protected <T> Invoker<T> doSelect(List<Invoker<T>> invokers, URL url, Invocation invocation) {
        // Number of invokers
        int length = invokers.size();
        // Every invoker has the same weight?
        boolean sameWeight = true;
        // the weight of every invokers
        int[] weights = new int[length];
        // the first invoker's weight
        int firstWeight = getWeight(invokers.get(0), invocation);
        weights[0] = firstWeight;
        // The sum of weights
        int totalWeight = firstWeight;
        for (int i = 1; i < length; i++) {
            int weight = getWeight(invokers.get(i), invocation);
            // save for later use
            weights[i] = weight;
            // Sum
            totalWeight += weight;
            if (sameWeight && weight != firstWeight) {
                sameWeight = false;
            }
        }
        if (totalWeight > 0 && !sameWeight) {
            // If (not every invoker has the same weight & at least one invoker's weight>0), select randomly based on totalWeight.
            int offset = ThreadLocalRandom.current().nextInt(totalWeight);
            // Return a invoker based on the random value.
            for (int i = 0; i < length; i++) {
                offset -= weights[i];
                if (offset < 0) {
                    return invokers.get(i);
                }
            }
        }
        // If all invokers have the same weight value or totalWeight=0, return evenly.
        return invokers.get(ThreadLocalRandom.current().nextInt(length));
    }
```

**2、RoundRobin LoadBalance (轮询)**

算法源码：

**3、LeastActive LoadBalance (最少活跃调用数)**

算法源码：

**4、ConsistentHash LoadBalance (一致性 Hash)**

算法源码：


2、ConsistentHash LoadBalance
一致性Hash，相同参数的请求总是发到同一个提供者。一：一致性Hash算法可以解决服务提供者的增加、移除及挂掉时的情况，能尽可能小的改变已存在 key 映射关系，尽可能的满足单调性的要求。二：一致性Hash通过构建虚拟节点，能尽可能避免分配失衡，具有很好的平衡性。
一致性Hash下面就来按照 5 个步骤简单讲讲 consistent hash算法的基本原理。因为以下资料来自于互联网，现说明几点：一、下面例子中的对象就相当于Client发的请求，cache相当于服务提供者。
2.1环形hash 空间
考虑通常的hash 算法都是将 value 映射到一个 32 为的 key 值，也即是 0~2^32-1 次方的数值空间；我们可以将这个空间想象成一个首(0)尾(2^32-1)相接的圆环，如下面图 2 所示的那样。
 
图 2 环形 hash 空间
2.2把对象映射到hash 空间
接下来考虑4个对象 object1~object4，通过 hash 函数计算出的 hash 值 key 在环上的分布如图 3所示。
hash(object1) = key1;
hash(object2) = key2;
hash(object3) = key3;
hash(object4) = key4;

图 3  4个对象的 key 值分布
2.3把cache 映射到hash空间
Consistent hashing 的基本思想就是将对象和 cache 都映射到同一个 hash 数值空间中，并且使用相同的hash算法。
假设当前有A,B和C 共3台cache，那么其映射结果将如图 4 所示，他们在 hash 空间中，以对应的 hash 值排列。
hash(cache A) = key A;
hash(cache B) = key B;
hash(cache C) = key C;
 
图 4  cache 和对象的 key 值分布
说到这里，顺便提一下 cache 的 hash 计算，一般的方法可以使用 cache 机器的 IP 地址或者机器名作为 hash输入。
2.4把对象映射到cache
现在 cache 和对象都已经通过同一个 hash 算法映射到 hash 数值空间中了，接下来要考虑的就是如何将对象映射到 cache上面了。
在这个环形空间中，如果沿着顺时针方向从对象的 key 值出发，直到遇见一个 cache ，那么就将该对象存储在这个 cache 上，因为对象和 cache 的 hash 值是固定的，因此这个 cache 必然是唯一和确定的。这样不就找到了对象和 cache 的映射方法了吗！
依然继续上面的例子（参见图 4 ），那么根据上面的方法，对象 object1 将被存储到 cache A 上； object2 和object3 对应到 cache C ； object4 对应到 cache B ；
2.5考察cache 的变动
前面讲过，一致性Hash算法可以解决服务提供者的增加、移除及挂掉时的情况，能尽可能小的改变已存在 key 映射关系，尽可能的满足单调性的要求。
移除 cache
考虑假设 cache B 挂掉了，根据上面讲到的映射方法，这时受影响的将仅是那些沿 cache B 逆时针遍历直到下一个 cache （ cache C ）之间的对象，也即是本来映射到 cache B 上的那些对象。
因此这里仅需要变动对象 object4 ，将其重新映射到 cache C 上即可；参见图 5 。
 
图 5  Cache B 被移除后的 cache 映射
添加 cache
再考虑添加一台新的 cache D 的情况，假设在这个环形 hash 空间中， cache D 被映射在对象 object2 和object3 之间。这时受影响的将仅是那些沿 cache D 逆时针遍历直到下一个 cache （ cache B ）之间的对象（它们是也本来映射到 cache C 上对象的一部分），将这些对象重新映射到 cache D 上即可。
因此这里仅需要变动对象 object2 ，将其重新映射到 cache D 上；参见图 6 。
 
图 6  添加 cache D 后的映射关系
2.6虚拟节点
考虑Hash 算法的另一个指标是平衡性 (Balance) ，定义如下：
平衡性是指哈希的结果能够尽可能分布到所有的缓冲中去，这样可以使得所有的缓冲空间都得到利用。
hash 算法并不是保证绝对的平衡，如果 cache 较少的话，对象并不能被均匀的映射到 cache 上，比如在上面的例子中，仅部署 cache A 和 cache C 的情况下，在 4 个对象中， cache A 仅存储了 object1 ，而 cache C 则存储了object2 、 object3 和 object4 ；分布是很不均衡的。
为了解决这种情况， consistent hashing 引入了“虚拟节点”的概念，它可以如下定义：
“虚拟节点”（ virtual node ）是实际节点在 hash 空间的复制品（ replica ），一实际个节点对应了若干个“虚拟节点”，这个对应个数也成为“复制个数”，“虚拟节点”在 hash 空间中以 hash 值排列。
仍以仅部署 cache A 和 cache C 的情况为例，在图 5 中我们已经看到， cache 分布并不均匀。现在我们引入虚拟节点，并设置“复制个数”为 2 ，这就意味着一共会存在 4 个“虚拟节点”， cache A1, cache A2 代表了cache A ； cache C1, cache C2 代表了 cache C ；假设一种比较理想的情况，参见图 7 。
 
图 7  引入“虚拟节点”后的映射关系
 
此时，对象到“虚拟节点”的映射关系为：
objec1->cache A2 ； objec2->cache A1 ； objec3->cache C1 ； objec4->cache C2 ；
因此对象 object1 和 object2 都被映射到了 cache A 上，而 object3 和 object4 映射到了 cache C 上；平衡性有了很大提高。
引入“虚拟节点”后，映射关系就从 { 对象 -> 节点 } 转换到了 { 对象 -> 虚拟节点 } 。查询物体所在 cache 时的映射关系如图 8 所示。
 
图 8 查询对象所在 cache
 
“虚拟节点”的 hash 计算可以采用对应节点的 IP 地址加数字后缀的方式。例如假设 cache A 的 IP 地址为202.168.14.241 。
引入“虚拟节点”前，计算 cache A 的 hash 值：
Hash(“202.168.14.241”);
引入“虚拟节点”后，计算“虚拟节”点 cache A1 和 cache A2 的 hash 值：
Hash(“202.168.14.241#1”);  // cache A1
Hash(“202.168.14.241#2”);  // cache A2


