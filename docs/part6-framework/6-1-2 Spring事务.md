## Spring事务





### 什么是事务
事务逻辑上的一组对数据对操作，组成这些操作的各个逻辑单元，要么一起成功，要么一起失败。

### 事务特性
    原子性（atomicity）：强调事务的不可分割；
    一致性（consistency）：事务的执行前后数据的完整性保持一致；
    隔离性（isolation）：一个事务的执行的过程中，不应该受到其他事务的干扰；
    持久性（durability）：事务一旦结束，数据就持久到数据库。

### 事务安全性
如果不考虑隔离性引发的安全性问题：

    脏读：一个事务读到了另一个事务未提交的数据
    不可重复读：一个事务督导另一个事务已经提交的update的数据导致多次查询结果不一致
    虚幻读：一个事务读到了另一个事务已经提交的insert的数据导致多次查询结果不一致。

### 事务隔离级别
解决读问题：设置事务隔离级别（5种）
DEFAULT这是一个PlatfromTransactionManager默认的隔离级别，使用数据库默认的事务隔离级别；

    未提交读（read uncommited）：脏读，不可重复读，虚读都有可能发生
    已提交读（read commited）：避免脏读。但是不可重复读和虚读都有可能发生；
    可重复读（repeatable read）：避免脏读和不可重复读，但是虚读有可能发生；
    串行化的（serializable）：避免以上所有读问题。
    
    MySQL默认：可重复读
    Oracle默认：已提交读


    read uncommitted：是最低读事务隔离级别，它允许另外一个事务可以看到这个事务未提交读数据。
    read commited： 保证一个事务提交后才能被另外一个事务读取。另外一个事务不能读取该事务未提交的数据。
    repeatable read：这种事务隔离级别可以防止脏读，不可重复读。但是可能会出现幻想读。它除了保证一个事务不能被另外一个事务读取未提交读数据之外还避免了一下情况产生（不可重复读）。
    serializable：这是花费最高代价但最可靠但事务隔离级别。事务被处理为顺序执行。除了防止脏读，不可重复读之外，还避免了幻象读（避免三种）。

 

### 事务读传播行为
PROPAGION_XXX：事务的传播行为

    * 保证同一个事务中
    PROPAGATION_REQUIRED      如果存在一个事务，则支持当前事务。如果没有事务则开启一个新的事务。
    PROPAGATION_SUPPORTS       如果存在一个事务，支持当前事务。如果没有事务，则非事务的执行。但是对于事务同步的事务管理器，PROPAGATION_SUPPORTS与不使用事务有少许不同。
    PROPAGATION_MANDATORY        如果已经存在一个事务，支持当前事务。如果没有一个活动的事务，则抛出异常。
    
    * 保证没有在同一个事务中
    PROPAGATION_REQUIRES_NEW     总是开启一个新的事务。如果一个事务已经存在，则将这个存在的事务挂起。
    PROPAGATION_NOT_SUPPORTED      总是非事务地执行，并挂起任何存在的事务。
    PROPAGATION_NEVER      总是非事务地执行，如果存在一个活动事务，则抛出异常
    PROPAGATION_NESTED      如果一个活动的事务存在，则运行在一个嵌套的事务中. 如果没有活动事务, 则按TransactionDefinition.PROPAGATION_REQUIRED 属性执行
    

### 事务传播行为种类

Spring在TransactionDefinition接口中规定了7种类型的事务传播行为，它们规定了事务方法和事务方法发生嵌套调用时事务如何进行传播：

下表为事务传播行为类型

事务传播行为类型	 | 说明
------------- | -------------
PROPAGATION_REQUIRED	 | 如果当前没有事务，就新建一个事务，如果已经存在一个事务中，加入到这个事务中。这是最常见的选择。
PROPAGATION_SUPPORTS	 | 支持当前事务，如果当前没有事务，就以非事务方式执行。
PROPAGATION_MANDATORY | 使用当前的事务，如果当前没有事务，就抛出异常。
PROPAGATION_REQUIRES_NEW	 | 新建事务，如果当前存在事务，把当前事务挂起。
PROPAGATION_NOT_SUPPORTED	| 以非事务方式执行操作，如果当前存在事务，就把当前事务挂起。
PROPAGATION_NEVER	 | 	以非事务方式执行，如果当前存在事务，则抛出异常。
PROPAGATION_NESTED	 | 	如果当前存在事务，则在嵌套事务内执行。如果当前没有事务，则执行与PROPAGATION_REQUIRED类似的操作。

 
### spring事务使用
 
spring的事务管理有几种方式实现，如何实现？
            
       事务的实现方式：实现方式共有两种：编码方式；声明式事务管理方式
    
       基于AOP技术实现的声明式事务管理，实质就是：在方法执行前后进行拦截，然后再目标方法开始之前创建并加入事务执行完目标方法后根据执行情况提交或回滚事务.
    
       声明式事务管理又有两种实现方式：基于xml配置文件的方式；另一个实在业务方法上进行@Transaction注解，将事务规则应用到业务逻辑中。
    
       一种常见的事务管理配置：事务拦截器TransactionInterceptor和事务自动代理BeanNameAutoProxyCreator相结合的方式。

### 事物配置中有哪些属性可以配置

（1）、事务的传播性：@Transactional(propagation=Propagation.REQUIRED) 

      如果有事务, 那么加入事务, 没有的话新建一个(默认情况下)

（2）、事务的超时性：@Transactional(timeout=30) //默认是30秒 

      注意这里说的是事务的超时性而不是Connection的超时性，这两个是有区别的

（3）、事务的隔离级别：@Transactional(isolation = Isolation.READ_UNCOMMITTED)

      读取未提交数据(会出现脏读, 不可重复读) 基本不使用

（4）、回滚：

    指定单一异常类：@Transactional(rollbackFor=RuntimeException.class)
    
    指定多个异常类：@Transactional(rollbackFor={RuntimeException.class, Exception.class})
    
    该属性用于设置需要进行回滚的异常类数组，当方法中抛出指定异常数组中的异常时，则进行事务回滚。

（5）、只读：@Transactional(readOnly=true)

    该属性用于设置当前事务是否为只读事务，设置为true表示只读，false则表示可读写，默认值为false。

 

 

 

 

 

 

 

 

 

 

  

 

 

 

 








