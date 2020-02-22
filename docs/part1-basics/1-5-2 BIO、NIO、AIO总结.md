
## 深入理解BIO、NIO、AIO

- [导读](#导读)
- [一、IO 介绍](#导读)
- [导读](#导读)
- [导读](#导读)
- [导读](#导读)
- [导读](#导读)



### 导读
本文你将获取到：同/异步 + 阻/非阻塞的性能区别；BIO、NIO、AIO 的区别；
理解和实现 NIO 操作 Socket 时的多路复用；同时掌握 IO 最底层最核心的操作技巧。

- BIO、NIO、AIO 的区别是什么？
- 同/异步、阻/非阻塞的区别是什么？
- 文件读写最优雅的实现方式是什么？
- NIO 如何实现多路复用功能？

带着以上这几个问题，让我们一起进入IO的世界吧。

在开始之前，我们先来思考一个问题：我们经常所说的“IO”的全称到底是什么？

可能很多人看到这个问题和我一样一脸懵逼，IO的全称其实是：Input/Output的缩写。

### 一、IO介绍
我们通常所说的 BIO 是相对于 NIO 来说的，BIO 也就是 Java 开始之初推出的 IO 操作模块，
BIO 是 BlockingIO 的缩写，顾名思义就是阻塞 IO 的意思。

####  1.1 BIO、NIO、AIO的区别
- **BIO** 就是传统的 java.io 包，它是基于流模型实现的，交互的方式是同步、阻塞方式，也就是说在读入输入流或者输出流时，在读写动作完成之前，线程会一直阻塞在那里，它们之间的调用时可靠的线性顺序。它的有点就是代码比较简单、直观；缺点就是 IO 的效率和扩展性很低，容易成为应用性能瓶颈。
- **NIO** 是 Java 1.4 引入的 java.nio 包，提供了 Channel、Selector、Buffer 等新的抽象，可以构建多路复用的、同步非阻塞 IO 程序，同时提供了更接近操作系统底层高性能的数据操作方式。
- **AIO** 是 Java 1.7 之后引入的包，是 NIO 的升级版本，提供了异步非堵塞的 IO 操作方式，所以人们叫它 AIO（Asynchronous IO），异步 IO 是基于事件和回调机制实现的，也就是应用操作之后会直接返回，不会堵塞在那里，当后台处理完成，操作系统会通知相应的线程进行后续的操作。

####  1.2 全面认识 IO
传统的 I/O 大致可以分为4种类型：
1. InputStream、OutputStream 基于字节操作的 IO
2. Writer、Reader 基于字符操作的 IO
3. File 基于磁盘操作的 IO
4. Socket 基于网络操作的 IO

java.net 下提供的 Scoket 很多时候人们也把它归为 同步阻塞 IO ,因为网络通讯同样是 IO 行为。

java.io 下的类和接口很多，但大体都是 InputStream、OutputStream、Writer、Reader 的子集，所有掌握这4个类和File的使用，是用好 IO 的关键。

####  1.3 IO 使用
接下来看 InputStream、OutputStream、Writer、Reader 的继承关系图和使用示例。

#####  1.3.1 InputStream 使用
继承关系图和类方法，如下图：
![InputStream](http://icdn.apigo.cn/blog/javacore-io-001.png)

InputStream 使用示例：
```java
InputStream inputStream = new FileInputStream("D:\\log.txt");
byte[] bytes = new byte[inputStream.available()];
inputStream.read(bytes);
String str = new String(bytes, "utf-8");
System.out.println(str);
inputStream.close();
```

#####  1.3.2 OutputStream 使用
继承关系图和类方法，如下图：
![OutputStream](http://icdn.apigo.cn/blog/javacore-io-002.png)

OutputStream 使用示例：
```java
// 参数二，表示是否追加，true=追加
OutputStream outputStream = new FileOutputStream("D:\\log.txt",true); 
outputStream.write("你好，老王".getBytes("utf-8"));
outputStream.close();
```
#####  1.3.3 Writer 使用
Writer 继承关系图和类方法，如下图：
![Writer](http://icdn.apigo.cn/blog/javacore-io-004.png)

Writer 使用示例：
```java
Writer writer = new FileWriter("D:\\log.txt",true); // 参数二，是否追加文件，true=追加
writer.append("老王，你好");
writer.close();
```

#####  1.3.4 Reader 使用
Reader 继承关系图和类方法，如下图：
![Writer](http://icdn.apigo.cn/blog/javacore-io-003.png)

Reader 使用示例：
```java
Reader reader = new FileReader(filePath);
BufferedReader bufferedReader = new BufferedReader(reader);
StringBuffer bf = new StringBuffer();
String str;
while ((str = bufferedReader.readLine()) != null) {
    bf.append(str + "\n");
}
bufferedReader.close();
reader.close();
System.out.println(bf.toString());
```

### 二、同步、异步、阻塞、非阻塞
上面说了很多关于同步、异步、阻塞和非阻塞的概念，接下来就具体聊一下它们4个的含义，以及组合之后形成的性能分析。

#### 2.1 同步与异步
同步就是一个任务的完成需要依赖另外一个任务时，只有等待被依赖的任务完成后，依赖的任务才能算完成，
这是一种可靠的任务序列。要么成功都成功，失败都失败，两个任务的状态可以保持一致。
而异步是不需要等待被依赖的任务完成，只是通知被依赖的任务要完成什么工作，依赖的任务也立即执行，
只要自己完成了整个任务就算完成了。至于被依赖的任务最终是否真正完成，依赖它的任务无法确定，
所以它是不可靠的任务序列。我们可以用打电话和发短信来很好的比喻同步与异步操作。

#### 2.2 阻塞与非阻塞
阻塞与非阻塞主要是从 CPU 的消耗上来说的，阻塞就是 CPU 停下来等待一个慢的操作完成 CPU 才接着完成其它的事。
非阻塞就是在这个慢的操作在执行时 CPU 去干其它别的事，等这个慢的操作完成时，CPU 再接着完成后续的操作。
虽然表面上看非阻塞的方式可以明显的提高 CPU 的利用率，但是也带了另外一种后果就是系统的线程切换增加。
增加的 CPU 使用时间能不能补偿系统的切换成本需要好好评估。

#### 2.3 同/异、阻/非堵塞 组合
同/异、阻/非堵塞的组合，有四种类型，如下表：

组合方式 |	性能分析
------------- | -------------
同步阻塞	 | 最常用的一种用法，使用也是最简单的，但是 I/O 性能一般很差，CPU 大部分在空闲状态。
同步非阻塞	 | 提升 I/O 性能的常用手段，就是将 I/O 的阻塞改成非阻塞方式，尤其在网络 I/O 是长连接，同时传输数据也不是很多的情况下，提升性能非常有效。 这种方式通常能提升 I/O 性能，但是会增加CPU 消耗，要考虑增加的 I/O 性能能不能补偿 CPU 的消耗，也就是系统的瓶颈是在 I/O 还是在 CPU 上。
异步阻塞	 | 这种方式在分布式数据库中经常用到，例如在网一个分布式数据库中写一条记录，通常会有一份是同步阻塞的记录，而还有两至三份是备份记录会写到其它机器上，这些备份记录通常都是采用异步阻塞的方式写 I/O。异步阻塞对网络 I/O 能够提升效率，尤其像上面这种同时写多份相同数据的情况。
异步非阻塞	 | 这种组合方式用起来比较复杂，只有在一些非常复杂的分布式情况下使用，像集群之间的消息同步机制一般用这种 I/O 组合方式。如 Cassandra 的 Gossip 通信机制就是采用异步非阻塞的方式。它适合同时要传多份相同的数据到集群中不同的机器，同时数据的传输量虽然不大，但是却非常频繁。这种网络 I/O 用这个方式性能能达到最高。

### 三、优雅的文件读写
Java 7 之前文件的读取是这样的：
```java
// 添加文件
FileWriter fileWriter = new FileWriter(filePath, true);
fileWriter.write(Content);
fileWriter.close();

// 读取文件
FileReader fileReader = new FileReader(filePath);
BufferedReader bufferedReader = new BufferedReader(fileReader);
StringBuffer bf = new StringBuffer();
String str;
while ((str = bufferedReader.readLine()) != null) {
    bf.append(str + "\n");
}
bufferedReader.close();
fileReader.close();
System.out.println(bf.toString());
```

Java 7 引入了Files（java.nio包下）的，大大简化了文件的读写，如下：
```java
// 写入文件（追加方式：StandardOpenOption.APPEND）
Files.write(Paths.get(filePath), Content.getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);

// 读取文件
byte[] data = Files.readAllBytes(Paths.get(filePath));
System.out.println(new String(data, StandardCharsets.UTF_8));
```
读写文件都是一行代码搞定，没错这就是最优雅的文件操作。

Files 下还有很多有用的方法，比如创建多层文件夹，写法上也简单了：
```java
// 创建多（单）层目录（如果不存在创建，存在不会报错）
new File("D://a//b").mkdirs();
```

### 四、Socket 和 NIO 的多路复用
本节带你实现最基础的 Socket 的同时，同时会实现 NIO 多路复用，还有 AIO 中 Socket 的实现。

#### 4.1 传统的 Socket 实现
接下来我们将会实现一个简单的 Socket，服务器端只发给客户端信息，再由客户端打印出来的例子，代码如下：
```java
int port = 4343; //端口号
// Socket 服务器端（简单的发送信息）
Thread sThread = new Thread(new Runnable() {
    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                // 等待连接
                Socket socket = serverSocket.accept();
                Thread sHandlerThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try (PrintWriter printWriter = new PrintWriter(socket.getOutputStream())) {
                            printWriter.println("hello world！");
                            printWriter.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                sHandlerThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
});
sThread.start();

// Socket 客户端（接收信息并打印）
try (Socket cSocket = new Socket(InetAddress.getLocalHost(), port)) {
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(cSocket.getInputStream()));
    bufferedReader.lines().forEach(s -> System.out.println("客户端：" + s));
} catch (UnknownHostException e) {
    e.printStackTrace();
} catch (IOException e) {
    e.printStackTrace();
}
```

调用 accept 方法，阻塞等待客户端连接；

利用 Socket 模拟了一个简单的客户端，只进行连接、读取和打印；

在 Java 中，线程的实现是比较重量级的，所以线程的启动或者销毁是很消耗服务器的资源的，即使使用线程池来实现，
使用上述传统的 Socket 方式，当连接数极具上升也会带来性能瓶颈，原因是线程的上线文切换开销会在高并发的时候体现的很明显，
并且以上操作方式还是同步阻塞式的编程，性能问题在高并发的时候就会体现的尤为明显。

以上的流程，如下图：
![1](http://icdn.apigo.cn/blog/javacore-io-005.png)

#### 4.2 NIO 多路复用
介于以上高并发的问题，NIO 的多路复用功能就显得意义非凡了。

NIO 是利用了单线程轮询事件的机制，通过高效地定位就绪的 Channel，来决定做什么，仅仅 select 阶段是阻塞的，
可以有效避免大量客户端连接时，频繁线程切换带来的问题，应用的扩展能力有了非常大的提高。
```java
// NIO 多路复用
ThreadPoolExecutor threadPool = new ThreadPoolExecutor(4, 4,
        60L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
threadPool.execute(new Runnable() {
    @Override
    public void run() {
        try (Selector selector = Selector.open();
             ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();) {
            serverSocketChannel.bind(new InetSocketAddress(InetAddress.getLocalHost(), port));
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            while (true) {
                selector.select(); // 阻塞等待就绪的Channel
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    try (SocketChannel channel = ((ServerSocketChannel) key.channel()).accept()) {
                        channel.write(Charset.defaultCharset().encode("你好，世界"));
                    }
                    iterator.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
});

// Socket 客户端（接收信息并打印）
try (Socket cSocket = new Socket(InetAddress.getLocalHost(), port)) {
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(cSocket.getInputStream()));
    bufferedReader.lines().forEach(s -> System.out.println("NIO 客户端：" + s));
} catch (IOException e) {
    e.printStackTrace();
}
```
- 首先，通过 Selector.open() 创建一个 Selector，作为类似调度员的角色；
- 然后，创建一个 ServerSocketChannel，并且向 Selector 注册，通过指定 SelectionKey.OP_ACCEPT，
   告诉调度员，它关注的是新的连接请求；
- 为什么我们要明确配置非阻塞模式呢？这是因为阻塞模式下，注册操作是不允许的，会抛出 IllegalBlockingModeException 异常；
- Selector 阻塞在 select 操作，当有 Channel 发生接入请求，就会被唤醒；

下面的图，可以有效的说明 NIO 复用的流程：
![NIO](http://icdn.apigo.cn/blog/javacore-io-006.png)

就这样 NIO 的多路复用就大大提升了服务器端响应高并发的能力。

#### 4.3 AIO 版 Socket 实现
Java 1.7 提供了 AIO 实现的 Socket 是这样的，如下代码：
```java
// AIO线程复用版
Thread sThread = new Thread(new Runnable() {
    @Override
    public void run() {
        AsynchronousChannelGroup group = null;
        try {
            group = AsynchronousChannelGroup.withThreadPool(Executors.newFixedThreadPool(4));
            AsynchronousServerSocketChannel server = AsynchronousServerSocketChannel.open(group).bind(new InetSocketAddress(InetAddress.getLocalHost(), port));
            server.accept(null, new CompletionHandler<AsynchronousSocketChannel, AsynchronousServerSocketChannel>() {
                @Override
                public void completed(AsynchronousSocketChannel result, AsynchronousServerSocketChannel attachment) {
                    server.accept(null, this); // 接收下一个请求
                    try {
                        Future<Integer> f = result.write(Charset.defaultCharset().encode("你好，世界"));
                        f.get();
                        System.out.println("服务端发送时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                        result.close();
                    } catch (InterruptedException | ExecutionException | IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void failed(Throwable exc, AsynchronousServerSocketChannel attachment) {
                }
            });
            group.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
});
sThread.start();

// Socket 客户端
AsynchronousSocketChannel client = AsynchronousSocketChannel.open();
Future<Void> future = client.connect(new InetSocketAddress(InetAddress.getLocalHost(), port));
future.get();
ByteBuffer buffer = ByteBuffer.allocate(100);
client.read(buffer, null, new CompletionHandler<Integer, Void>() {
    @Override
    public void completed(Integer result, Void attachment) {
        System.out.println("客户端打印：" + new String(buffer.array()));
    }

    @Override
    public void failed(Throwable exc, Void attachment) {
        exc.printStackTrace();
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
});
Thread.sleep(10 * 1000);
```

### 五、总结
以上基本就是 IO 从 1.0 到目前版本（本文的版本）JDK 8 的核心使用操作了，
可以看出来 IO 作为比较常用的基础功能，发展变化的改动也很大，而且使用起来也越来越简单了，
IO 的操作也是比较好理解的，一个输入一个输出，掌握好了输入输出也就掌握好了 IO，
Socket 作为网络交互的集成功能，显然 NIO 的多路复用，给 Socket 带来了更多的活力和选择，
用户可以根据自己的实际场景选择相应的代码策略。

IO的方式通常分为几种，同步阻塞的BIO、同步非阻塞的NIO、异步非阻塞的AIO。

**Java BIO** 
同步并阻塞，服务器实现模式为一个连接一个线程，即客户端有连接请求时服务器端就需要启动一个线程进行处理，
如果这个连接不做任何事情会造成不必要的线程开销，当然可以通过线程池机制改善。
BIO方式适用于连接数目比较少且固定的架构，这种方式对服务器资源要求比较高，
并发局限于应用中，JDK1.4以前的唯一选择，但程序直观简单易理解。

**Java NIO**
同步非阻塞，服务器实现模式为一个I/O请求一个线程，即客户端发送的连接请求都会注册到多路复用器上，
多路复用器轮询到连接有I/O请求时才启动一个线程进行处理。
NIO方式适用于连接数目多且连接比较短（轻操作）的架构，比如聊天服务器，并发局限于应用中，
编程比较复杂，JDK1.4开始支持。

**Java AIO**
异步非阻塞，服务器实现模式为一个有效请求一个线程，客户端的I/O请求都是由OS先完成了再通知服务器应用去启动线程进行处理。
AIO方式使用于连接数目多且连接比较长（重操作）的架构，比如相册服务器，充分调用OS参与并发操作，编程比较复杂，JDK7开始支持。