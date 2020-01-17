## Java架构师--成神之路

#### 修改记录
| 版本        | 编写时间   |  作者  | 描述  |
| --------   | -----:  | :----:  |:----:  |
| v1.0.0      | 2019-10-29  |   Rock.Sang    |  梳理大纲   |
| v1.0.1      | 2019-11-15  |   Rock.Sang    |  完善所有目录结构   |
| v1.0.2      | 2020-01-07  |   Rock.Sang    |  添加英语模块   |


#### 文章目录

- [第一章 基础篇](#第一章-基础篇 )
   - [1.1 集合容器](#集合容器)
   - [1.2 数据结构](#数据结构)
   - [1.3 常用算法](#常用算法)
   - [1.4 JDK演变](#jdk演变)
   - [1.5 I/O机制](#i/o机制)
   - [1.6 网络协议](#网络协议)
- [第二章 进阶篇](#第二章-进阶篇 )
   - [2.1 类加载](#类加载)
   - [2.2 JVM](#jvm)
   - [2.3 垃圾回收](#垃圾回收)
   - [2.4 线程](#线程)
   - [2.5 线程池](#线程池)
   - [2.6 锁](#锁)
- [第三章 中间件篇](#第三章-中间件篇 )
   - [3.1 缓存](#缓存)
   - [3.2 远程调用](#远程调用)
   - [3.3 消息队列](#消息队列)
   - [3.4 任务调序](#任务调序)
   - [3.5 搜索引擎](#搜索引擎)
   - [3.6 分布式锁](#分布式锁)
   - [3.7 监控](#监控)
   - [3.8 日志监控](#日志监控)
   - [3.9 限流&熔断](#限流&熔断)
   - [3.10 分库分表](#分库分表)
   - [3.11 配置中心](#配置中心)
   - [3.12 API网关](#api网关)
- [第四章 架构设计篇](#第四章-架构设计篇 )
   - [4.1 系统设计](#系统设计)    
     - [4.1.1 UML](#UML)    
     - [4.1.2 流程图](#流程图)    
     - [4.1.3 领域模型](#领域模型)    
   - [4.2 权限认证](#权限认证)    
- [第五章 设计思想和开发模式篇](#第五章-设计思想和开发模式篇 )
   - [5.1 设计模式](#设计模式) 
   - [5.2 领域驱动设计](#领域驱动设计) 
   - [5.3 Actor模式](#Actor模式) 
   - [5.4 响应式编程](#响应式编程) 
   - [5.5 DODAF2.0](#DODAF2.0) 
   - [5.6 Serverless](#Serverless) 
   - [5.7 ServerMesh](#ServiceMesh) 
- [第六章 框架篇](#第六章-框架篇 )
   - [6.1 Spring框架](#Spring框架)       
   - [6.2 分布式框架](#分布式框架)       
   - [6.3 SpringBoot框架](#SpringBoot框架)       
   - [6.4 SpringCloud框架](#SpringCloud框架)       
- [第七章 数据库篇](#第七章-数据库篇 )
   - [7.1 基础理论](#基础理论)       
   - [7.2 Mysql](#MySQL)       
   - [7.3 NoSQL](#NoSQL)       
- [第八章 源码篇](#第八章-源码篇 )
   - [8.1 Spring源码](#Spring源码)       
   - [8.2 Mybatis源码](#Mybatis源码)       
   - [8.3 Dubbo源码](#Dubbo源码)       
   - [8.4 Netty源码](#Netty源码)       
   - [8.5 ZooKeeper源码](#ZooKeeper源码)       
- [第九章 大数据篇](#第九章-大数据篇 )
   - [9.1 流式计算](#流式计算) 
   - [9.2 Hadoop](#Hadoop) 
   - [9.3 Spark](#Spark) 
- [第十章 深度/机器学习篇](#第十章-深度和机器学习篇)
- [第十一章 备用一篇](#第十一章-备用一篇)
- [第十二章 备用二篇](#第十二章-备用二篇)
- [第十三章 备用三篇](#第十三章-备用三篇)
- [第十四章 备用四篇](#第十四章-备用四篇)
- [第十五章 备用五篇](#第十五章-备用五篇)
- [第十六章 备用六篇](#第十六章-备用六篇)
- [第十七章 备用七篇](#第十七章-备用七篇)
- [第十八章 备用八篇](#第十八章-备用八篇)
- [第十九章 备用九篇](#第十九章-备用九篇)
- [第二十章 面试篇](#第二十章-面试篇 )
   - [20.1 备战面试](#备战面试) 
   - [20.2 常见面试题总结](#常见面试题总结) 
   - [20.3 面经](#面经) 
   - [20.4 简历](#简历) 
- [第二十一章 工具篇](#第二十一章-工具篇)
   - [21.1 常用IDE](#常用IDE)    
   - [22.2 Git](#Git)    
- [第二十二章 项目实战篇](#第二十二章-项目实战篇)
   - [22.1 支付系统架构图](#支付系统架构图)   
   - [22.2 收银系统架构图](#收银系统架构图)   
   - [22.3 秒杀系统设计](#秒杀系统设计)   
- [第二十三章 资源篇](#第二十三章-资源篇)
   - [23.1 已阅书单](#已阅书单)  
   - [23.2 待阅书单](#待阅书单)  
   - [23.3 Github榜单](#Github榜单)  
   - [23.4 Blog榜单](#Blog榜单)  
   - [23.5 技术网站](#技术网站)  
- [第二十四章 技术管理及自我提升](#第二十四章-技术管理及自我提升) 
   - [24.1 技术管理](#技术管理) 
   - [24.2 自我提升](#自我提升) 
- [第二十五章 英语能力提升](#第二十五章-英语能力提升) 
   - [25.1 英语学习](#英语学习) 
   - [25.2 口语学习](#口语学习) 

## 第一章 基础篇

#### 集合容器

* [1.1.1 Java 基础知识回顾](docs/part1-basics/1-1-1%20Java基础知识.md)
* [1.1.2 集合类](docs/part1-basics/1-1-2%20集合.md)
* [1.1.3 ArrayList源码学习](docs/part1-basics/1-1-3%20ArrayList源码学习.md)
* [1.1.4 LinkedList源码学习](docs/part1-basics/1-1-4%20LinkedList源码学习.md)
* [1.1.5 HashMap源码学习](docs/part1-basics/1-1-5%20HashMap源码学习.md)
* [1.1.6 HashTable源码学习](docs/part1-basics/1-1-6%20HashTable源码学习.md)
* [1.1.7 ConcurrentHashMap源码学习](docs/part1-basics/1-1-7%20ConcurrentHashMap源码学习.md)

#### 数据结构

* [1.2.1 数组](docs/part1-basics/1-2-1%20数组.md)
* [1.2.2 链表](docs/part1-basics/1-2-2%20链表.md)
* [1.2.3 队列](docs/part1-basics/1-2-3%20队列.md)
* [1.2.4 树](docs/part1-basics/1-2-4%20树.md)
* [1.2.5 堆和栈](docs/part1-basics/1-2-5%20堆和栈.md)
* [1.2.6 散列表](docs/part1-basics/1-2-6%20散列表.md)
* [1.2.7 图](docs/part1-basics/1-2-7%20图.md)

#### 常用算法

* [1.3.1 几种常用排序算法](docs/part1-basics/1-3-1%20几种常用排序算法.md)
* [1.3.2 几种常用查找算法](docs/part1-basics/1-3-2%20几种常用查找算法.md)

#### JDK演变

* [1.4.1 Java8新特性](docs/part1-basics/1-4-1%20Java8新特性.md)
* [1.4.2 Java9新特性](docs/part1-basics/1-4-2%20Java9新特性.md)
* [1.4.3 Java10+新特性](docs/part1-basics/1-4-3%20Java10+新特性.md)

#### I/O机制

* [1.5.1 I/O工作机制](docs/part1-basics/1-5-1%20BIO、NIO、AIO总结.md)

#### 网络协议

* [1.6.1 网络分层结构](docs/part1-basics/1-6-1%20网络分层结构.md)
* [1.6.2 三次握手和四次挥手](docs/part1-basics/1-6-2%20三次握手和四次挥手.md)

## 第二章 进阶篇

#### 类加载

* [2.1.1 类文件结构](docs/part2-advance/2-1-1%20类文件结构.md)
* [2.1.2 类加载机制](docs/part2-advance/2-1-2%20类加载机制.md)

#### JVM

* [2.2.1 JVM内存结构](docs/part2-advance/2-2-1%20JVM内存结构.md)
* [2.2.2 JDK监控和故障处理工具](docs/part2-advance/2-2-2%20JDK监控和故障处理工具.md)

#### 垃圾回收

* [2.3.1 垃圾回收算法](docs/part2-advance/2-3-1%20垃圾回收算法.md)
* [2.3.2 垃圾回收器](docs/part2-advance/2-3-2%20垃圾回收器.md)

#### 线程

* [2.4.1 线程和进程](docs/part2-advance/2-4-1%20线程和进程.md)
* [2.4.2 线程状态流转](docs/part2-advance/2-4-2%20线程状态流转.md)
* [2.4.3 线程安全](docs/part2-advance/2-4-3%20线程安全.md)

#### 线程池

* [2.5.1 Java并发](docs/part2-advance/2-5-1%20Java并发.md)
* [2.5.2 多线程](docs/part2-advance/2-5-2%20多线程.md)
* [2.5.3 线程池实现原理](docs/part2-advance/2-5-3%20线程池实现原理.md)
* [2.5.4 一致性事务](docs/part2-advance/2-5-4%20一致性事务.md)

#### 锁 

* [2.6.1 Java中的锁和同步类](docs/part2-advance/2-6-1%20java中的锁和同步类.md)
* [2.6.2 公平锁和非公平锁](docs/part2-advance/2-6-2%20公平锁和非公平锁.md)
* [2.6.3 乐观锁和悲观锁](docs/part2-advance/2-6-3%20乐观锁和悲观锁.md)
* [2.6.4 CAS原理和ABA问题](docs/part2-advance/2-6-4%20CAS原理和ABA问题.md)
* [2.6.5 CopyOnWrite容器](docs/part2-advance/2-6-5%20CopyOnWrite容器.md)
* [2.6.6 RingBuffer](docs/part2-advance/2-6-6%20RingBuffer.md)
* [2.6.7 可重入锁和不可重入锁](docs/part2-advance/2-6-7%20可重入锁和不可重入锁.md)
* [2.6.8 互斥锁和共享锁](docs/part2-advance/2-6-8%20互斥锁和共享锁.md)
* [2.6.9 死锁](docs/part2-advance/2-6-9%20死锁.md)

## 第三章 中间件篇

#### 缓存

* [3.1.1 Java中的锁和同步类](docs/part3-middleware/3-1-1%20Web缓存.md)
* [3.1.2 Memcached](docs/part3-middleware/3-1-2%20Memcached.md)
* [3.1.3 Redis](docs/part3-middleware/3-1-3%20Redis.md)
* [3.1.4 客户端缓存](docs/part3-middleware/3-1-4%20客户端缓存.md)

#### 远程调用

* [3.2.1 Dubbo](docs/part3-middleware/3-2-1%20Dubbo.md)
* [3.2.2 Thrift](docs/part3-middleware/3-2-2%20Thrift.md)
* [3.2.3 gRPC](docs/part3-middleware/3-2-3%20gRPC.md)

#### 消息队列

* [3.3.1 消息总线](docs/part3-middleware/3-3-1%20消息总线.md)
* [3.3.2 消息的顺序](docs/part3-middleware/3-3-2%20消息的顺序.md)
* [3.3.3 RabbitMQ](docs/part3-middleware/3-3-3%20RabbitMQ.md)
* [3.3.4 RocketMQ](docs/part3-middleware/3-3-4%20RocketMQ.md)
* [3.3.5 ActiveMQ](docs/part3-middleware/3-3-5%20ActiveMQ.md)
* [3.3.6 Kafka](docs/part3-middleware/3-3-6%20Kafka.md)
* [3.3.7 Redis消息推送](docs/part3-middleware/3-3-7%20Redis消息推送.md)
* [3.3.8 ZeroMQ](docs/part3-middleware/3-3-8%20ZeroMQ.md)

#### 任务调序

* [3.4.1 单机定时调度](docs/part3-middleware/3-4-1%20单机定时调度.md)
* [3.4.2 分布式定时调度](docs/part3-middleware/3-4-2%20分布式定时调度.md)

#### 搜索引擎

* [3.5.1 搜索引擎原理](docs/part3-middleware/3-5-1%20搜索引擎原理.md)
* [3.5.2 Solr](docs/part3-middleware/3-5-2%20Solr.md)
* [3.5.3 Elasticsearch](docs/part3-middleware/3-5-3%20Elasticsearch.md)
* [3.5.4 Lucene](docs/part3-middleware/3-5-4%20Lucene.md)
* [3.5.5 Sphinx](docs/part3-middleware/3-5-5%20Sphinx.md)

#### 分布式锁

* [3.6.1 数据库实现](docs/part3-middleware/3-6-1%20数据库实现.md)
* [3.6.2 缓存实现](docs/part3-middleware/3-6-2%20缓存实现.md)
* [3.6.3 Zookeeper实现](docs/part3-middleware/3-6-3%20Zookeeper实现.md)

#### 监控

* [3.7.1 CAT](docs/part3-middleware/3-7-1%20CAT.md)
* [3.7.2 APM](docs/part3-middleware/3-7-2%20APM.md)
* [3.7.3 Zabbix](docs/part3-middleware/3-7-3%20Zabbix.md)

#### 日志监控

* [3.8.1 日志搜集ELK](docs/part3-middleware/3-8-1%20日志搜集ELK.md)

#### 限流&熔断

* [3.9.1 限流](docs/part3-middleware/3-9-1%20限流.md)
* [3.9.2 熔断](docs/part3-middleware/3-9-2%20熔断.md)

#### 分库分表

* [3.10.1 ShardingJDBC](docs/part3-middleware/3-10-1%20ShardingJDBC.md)
* [3.10.2 Mycat](docs/part3-middleware/3-10-2%20Mycat.md)

#### 配置中心

* [3.11.1 配置中心](docs/part3-middleware/3-11-1%20配置中心.md)

#### API网关

* [3.12.1 API网关](docs/part3-middleware/3-12-1%20API网关.md)


## 第四章 架构设计篇

#### 系统设计

* [4.1.1 系统设计](docs/part4-architectureDesign/4-1-1%20系统设计.md)

##### UML

* [4.1.1.1 UML](docs/part4-architectureDesign/4-1-1-1%20UML.md)

##### 流程图

* [4.1.1.2 流程图](docs/part4-architectureDesign/4-1-1-2%20流程图.md)

##### 领域模型

* [4.1.1.3 领域模型](docs/part4-architectureDesign/4-1-1-3%20领域模型.md)

#### 权限认证

* [4.2.1 授权和认证](docs/part4-architectureDesign/4-2-1%20授权和认证.md)


## 第五章 设计思想和开发模式篇

#### 设计模式

* [5.1.1 设计模式](docs/part5-designIdeaAndDesignMode/5-1-1%20设计模式.md)

#### 领域驱动设计

* [5.2.1 领域驱动设计](docs/part5-designIdeaAndDesignMode/5-2-1%20领域驱动设计.md)
* [5.2.2 命令查询职责分离](docs/part5-designIdeaAndDesignMode/5-2-2%20命令查询职责分离.md)
* [5.2.3 贫血和充血模型](docs/part5-designIdeaAndDesignMode/5-2-3%20贫血和充血模型.md)

#### Actor模式

* [5.3.1 Actor模式](docs/part5-designIdeaAndDesignMode/5-3-1%20Actor模式.md)

#### 响应式编程

* [5.4.1 Reactor](docs/part5-designIdeaAndDesignMode/5-4-1%20Reactor.md)
* [5.4.2 RxJava](docs/part5-designIdeaAndDesignMode/5-4-2%20RxJava.md)
* [5.4.3 Ver.x](docs/part5-designIdeaAndDesignMode/5-4-3%20VerX.md)

#### DODAF2.0

* [5.5.1 DODAF2.0](docs/part5-designIdeaAndDesignMode/5-5-1%20DODAF2.0.md)

#### Serverless

* [5.6.1 Serverless](docs/part5-designIdeaAndDesignMode/5-6-1%20Serverless.md)

#### ServiceMesh
 
* [5.7.1 ServiceMesh](docs/part5-designIdeaAndDesignMode/5-7-1%20ServiceMesh.md)  


## 第六章 框架篇

#### Spring框架

* [6.1.1 Spring架构设计](docs/part6-framework/6-1-1%20Spring架构设计.md)

#### 分布式框架

* [6.2.1 分布式相关知识](docs/part6-framework/6-2-1%20分布式相关知识.md)

#### SpringBoot框架

* [6.3.1 SpringBoot相关知识](docs/part6-framework/6-3-1%20SpringBoot相关知识.md)

#### SpringCloud框架

* [6.4.1 SpringCloud相关知识](docs/part6-framework/6-4-1%20SpringCloud相关知识.md)

## 第七章 数据库篇

#### 基础理论

* [7.1.1 数据库设计的三大范式](docs/part7-database/7-1-1%20数据库设计的三大范式.md)

#### MySQL

* [7.2.1 Mysql原理](docs/part7-database/7-2-1%20Mysql原理.md)
* [7.2.2 InnoDB](docs/part7-database/7-2-2%20InnoDB.md)
* [7.2.3 优化](docs/part7-database/7-2-3%20优化.md)
* [7.2.4 索引](docs/part7-database/7-2-4%20索引.md)
* [7.2.5 explain](docs/part7-database/7-2-5%20explain.md)

#### NoSQL

* [7.3.1 MongoDB](docs/part7-database/7-3-1%20MongoDB.md)
* [7.3.2 HBase](docs/part7-database/7-3-2%20HBase.md)

## 第八章 源码篇

#### Spring源码

* [8.1.1 MongoDB](docs/part8-soundCode/8-1-1%20Spring源码.md)

#### Mybatis源码

* [8.2.1 Mybatis源码](docs/part8-soundCode/8-2-1%20Mybatis源码.md)

#### Dubbo源码

* [8.3.1 Dubbo源码](docs/part8-soundCode/8-3-1%20Dubbo源码.md)

#### Netty源码

* [8.4.1 Netty源码](docs/part8-soundCode/8-4-1%20Netty源码.md)

#### ZooKeeper源码

* [8.5.1 Zookeeper源码](docs/part8-soundCode/8-5-1%20Zookeeper源码.md)

## 第九章 大数据篇

#### 流式计算

* [9.1.1 Storm](docs/part9-bigData/9-1-1%20Storm.md)
* [9.1.2 Flink](docs/part9-bigData/9-1-2%20Flink.md)
* [9.1.3 KafkaStream](docs/part9-bigData/9-1-3%20KafkaStream.md)
* [9.1.4 应用场景](docs/part9-bigData/9-1-4%20应用场景.md)

#### Hadoop

* [9.2.1 HDFS](docs/part9-bigData/9-2-1%20HDFS.md)
* [9.2.2 MapReduce](docs/part9-bigData/9-2-2%20MapReduce.md)
* [9.2.3 Yarn](docs/part9-bigData/9-2-3%20Yarn.md)

#### Spark

* [9.3.1 Spark](docs/part9-bigData/9-3-1%20Spark.md)

## 第十章 深度和机器学习篇

#### 智能时代

* [10.1.1 数学基础](docs/part10-machineLearning/10-1-1%20数学基础.md)
* [10.1.2 机器学习](docs/part10-machineLearning/10-1-2%20机器学习.md)
* [10.1.3 人工神经网络](docs/part10-machineLearning/10-1-3%20人工神经网络.md)
* [10.1.4 深度学习](docs/part10-machineLearning/10-1-4%20深度学习.md)
* [10.1.5 应用场景](docs/part10-machineLearning/10-1-5%20应用场景.md)

#### 常用框架

* [10.2.1 TensorFlow](docs/part10-machineLearning/10-2-1%20TensorFlow.md)
* [10.2.2 DeepLearning4J](docs/part10-machineLearning/10-2-2%20DeepLearning4J.md)

## 第十一章 LeetCode - 力扣

#### 简单水平
* [11.1.1 两数之和](docs/part11-LeetCode/11-1-1%20两数之和.md)

#### 中等水平
* [11.2.1 两数相加](docs/part11-LeetCode/11-2-1%20两数相加.md)

#### 困难水平
* [11.3.1 寻找两个有序数组的中位数](docs/part11-LeetCode/11-3-1%20寻找两个有序数组的中位数.md)

## 第十二章 备用二篇
## 第十三章 备用三篇
## 第十四章 备用四篇
## 第十五章 备用五篇
## 第十六章 备用六篇
## 第十七章 备用七篇
## 第十八章 备用八篇
## 第十九章 备用九篇

## 第二十章 面试篇

#### 备战面试

* [20.1.1 备战面试](docs/part20-interview/20-1-1%20备战面试.md)

#### 常见面试题总结

* [20.2.1 常见面试题总结](docs/part20-interview/20-2-1%20常见面试题总结.md)

#### 面经

* [20.3.1 面经](docs/part20-interview/20-3-1%20面经.md)

#### 简历

* [20.4.1 简历](docs/part20-interview/20-4-1%20简历.md)

## 第二十一章 工具篇

#### 常用IDE

* [21.1.1 常用IDE](docs/part21-tools/21-1-1%20常用IDE.md)

#### Git

* [21.2.1 Git](docs/part21-tools/21-2-1%20Git.md)

## 第二十二章 项目实战篇

#### 支付系统架构图

* [22.1.1 支付架构设计](docs/part22-projectExperience/22-1-1%20支付架构设计.md)

#### 收银系统架构图

* [22.2.1 收银台结构设计](docs/part22-projectExperience/22-2-1%20收银台结构设计.md)

#### 秒杀系统设计

* [22.3.1 秒杀系统设计](docs/part22-projectExperience/22-3-1%20秒杀系统设计.md)

## 第二十三章 资源篇

#### 已阅书单

* [23.1.1 已阅书单](docs/part23-resources/23-1-1%20已阅书单.md)

#### 待阅书单

* [23.2.1 待阅书单](docs/part23-resources/23-2-1%20待阅书单.md)

#### Github榜单

* [23.3.1 Github榜单](docs/part23-resources/23-3-1%20Github榜单.md)

#### Blog榜单

* [23.4.1 Blog榜单](docs/part23-resources/23-4-1%20Blog榜单.md)

#### 技术网站

* [23.5.1 技术网站](docs/part23-resources/23-5-1%20技术网站.md)

## 第二十四章 技术管理及自我提升

#### 技术管理

* [24.1.1 技术管理](docs/part24-techManagement/24-1-1%20技术管理.md)

#### 自我提升

* [24.2.1 自我提升](docs/part24-techManagement/24-2-1%20自我提升.md)

## 第二十五章 英语能力提升

#### 英语学习

* [25.1.1 英语学习](docs/part25-english/25-1-1%20英语学习.md)

#### 口语学习

* [25.2.1 口语学习](docs/part25-english/25-2-1%20口语学习.md)


