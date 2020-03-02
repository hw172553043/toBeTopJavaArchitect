## Kafka

- [一、简介]()
- [二、Kafka基本架构]()
- [三、基本原理]()
- [四、Zookeeper在kafka的作用]()
- [五、执行流程]()
- [六、Kafka的特性]()
- [七、Kafka的使用场景]()

### 简介

Apache Kafka是分布式发布-订阅消息系统，在 kafka官网上对 kafka 的定义：一个分布式发布-订阅消息传递系统。 
它最初由LinkedIn公司开发，Linkedin于2010年贡献给了Apache基金会并成为顶级开源项目。

Kafka是一种快速、可扩展的、设计内在就是分布式的，分区的和可复制的提交日志服务。

几种分布式系统消息系统的对比：
![分布式系统消息系统的对比](https://img-blog.csdn.net/20170816195836787?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQveGxnZW4xNTczODc=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)


### Kafka基本架构

它的架构包括以下组件：

1. **话题（Topic）** 
   
   Topic 是特定类型的消息流。消息是字节的有效负载（Payload），话题是消息的分类名或种子（Feed）名；

2. **生产者（Producer）**
   
   Producer 是能够发布消息到话题的任何对象；

3. **服务代理（Broker）**
    
    Broker 已发布的消息保存在一组服务器中，它们被称为代理（Broker）或Kafka集群；

4. **消费者（Consumer）**
    
    Consumer 可以订阅一个或多个话题，并从Broker拉数据，从而消费这些已发布的消息；

![Kafka基本架构](https://img-blog.csdn.net/20170816194851267?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQveGxnZW4xNTczODc=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

上图中可以看出，生产者将数据发送到Broker代理，Broker代理有多个话题topic，消费者从Broker获取数据。

### 基本原理

我们将消息的发布（publish）称作 producer，将消息的订阅（subscribe）表述为 consumer，将中间的存储阵列称作 broker(代理)，这样就可以大致描绘出这样一个场面：

![基本原理](https://img-blog.csdn.net/20170816192915339?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQveGxnZW4xNTczODc=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

生产者将数据生产出来，交给 broker 进行存储，消费者需要消费数据了，就从broker中去拿出数据来，然后完成一系列对数据的处理操作。

乍一看返也太简单了，不是说了它是分布式吗，难道把 producer、 broker 和 consumer 放在三台不同的机器上就算是分布式了吗。看 kafka 官方给出的图：

![分布式](https://img-blog.csdn.net/20170816193213042?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQveGxnZW4xNTczODc=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

多个 broker 协同合作，producer 和 consumer 部署在各个业务逻辑中被频繁的调用，三者通过 zookeeper管理协调请求和转发。这样一个高性能的分布式消息发布订阅系统就完成了。

图上有个细节需要注意，producer 到 broker 的过程是 push，也就是有数据就推送到 broker，而 consumer 到 broker 的过程是 pull，是通过 consumer 主动去拉数据的，而不是 broker 把数据主懂发送到 consumer 端的。

### Zookeeper在kafka的作用

上述，提到了Zookeeper，那么Zookeeper在kafka的作用是什么？

- （1）无论是kafka集群，还是producer和consumer都依赖于zookeeper来保证系统可用性集群，保存一些meta信息。

- （2）Kafka使用zookeeper作为其分布式协调框架，很好的将消息生产、消息存储、消息消费的过程结合在一起。

- （3）同时借助zookeeper，kafka能够生产者、消费者和broker在内的所以组件在无状态的情况下，建立起生产者和消费者的订阅关系，并实现生产者与消费者的负载均衡。

### 执行流程

首先看一下如下的过程：

![执行流程](https://img-blog.csdn.net/20170816195347506?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQveGxnZW4xNTczODc=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

我们看上面的图，我们把 broker 的数量减少，只有一台。现在假设我们按照上图进行部署：

（1）Server-1 broker 其实就是 kafka 的 server，因为 producer 和 consumer 都要去用它。 Broker 主要还是做存储用。

（2）Server-2 是 zookeeper 的 server 端，它维持了一张表，记录了各个节点的 IP、端口等信息。

（3）Server-3、 4、 5 他们的共同之处就是都配置了 zkClient，更明确的说，就是运行前必须配置 zookeeper的地址，道理也很简单，这之间的连接都是需要 zookeeper 来进行分发的。

（4）Server-1 和 Server-2 的关系，他们可以放在一台机器上，也可以分开放，zookeeper 也可以配集群。目的是防止某一台挂了。

简单说下整个系统运行的顺序：

（1）启动zookeeper 的 server

（2）启动kafka 的 server

（3）Producer 如果生产了数据，会先通过 zookeeper 找到 broker，然后将数据存放到 broker

（4）Consumer 如果要消费数据，会先通过 zookeeper 找对应的 broker，然后消费。

### Kafka的特性

（1）高吞吐量、低延迟：kafka每秒可以处理几十万条消息，它的延迟最低只有几毫秒，每个topic可以分多个partition, consumer group 对partition进行consume操作；

（2）可扩展性：kafka集群支持热扩展；

（3）持久性、可靠性：消息被持久化到本地磁盘，并且支持数据备份防止数据丢失；

（4）容错性：允许集群中节点失败（若副本数量为n,则允许n-1个节点失败）；

（5）高并发：支持数千个客户端同时读写；

（6）支持实时在线处理和离线处理：可以使用Storm这种实时流处理系统对消息进行实时进行处理，同时还可以使用Hadoop这种批处理系统进行离线处理；

### Kafka的使用场景

（1）日志收集：一个公司可以用Kafka可以收集各种服务的log，通过kafka以统一接口服务的方式开放给各种consumer，例如Hadoop、Hbase、Solr等；

（2）消息系统：解耦和生产者和消费者、缓存消息等；

（3）用户活动跟踪：Kafka经常被用来记录web用户或者app用户的各种活动，如浏览网页、搜索、点击等活动，这些活动信息被各个服务器发布到kafka的topic中，然后订阅者通过订阅这些topic来做实时的监控分析，或者装载到Hadoop、数据仓库中做离线分析和挖掘；

（4）运营指标：Kafka也经常用来记录运营监控数据。包括收集各种分布式应用的数据，生产各种操作的集中反馈，比如报警和报告；

（5）流式处理：比如spark streaming和storm；

（6）事件源；

[参考文档](https://blog.csdn.net/xlgen157387/article/details/77266719)