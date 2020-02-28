## Fork Join 框架

1. 什么是fork/join框架
    
    fork/join框架是ExecutorService接口的一个实现，可以帮助开发人员充分利用多核处理器的优势，编写出并行执行的程序，提高应用程序的性能；
    设计的目的是为了处理那些可以被递归拆分的任务。
    
    fork/join框架与其它ExecutorService的实现类相似，会给线程池中的线程分发任务，
    不同之处在于它使用了工作窃取算法，所谓工作窃取，指的是对那些处理完自身任务的线程，会从其它线程窃取任务执行。
    
    fork/join框架的核心是ForkJoinPool类，该类继承了AbstractExecutorService类。ForkJoinPool实现了工作窃取算法并且能够执行 ForkJoinTask任务。

2. 基本使用方法

    在使用fork/join框架之前，我们需要先对任务进行分割，任务分割代码应该跟下面的伪代码类似：
    ```java
        if (任务足够小){
          直接执行该任务;
        }else{
          将任务一分为二;
          执行这两个任务并等待结果;
        }
    ```

    首先，我们会在ForkJoinTask的子类中封装以上代码，不过一般我们会使用更加具体的ForkJoinTask类型，如 RecursiveTask（可以返回一个结果）或RecursiveAction（无返回）。

    当写好ForkJoinTask的子类后，创建该对象，该对象代表了所有需要完成的任务；然后将这个任务对象传给ForkJoinPool实例的invoke()去执行即可。

![参考链接1](http://www.cnblogs.com/chenpi/p/5581198.html)
![参考链接2](http://www.cnblogs.com/chenpi/p/5581198.html)
