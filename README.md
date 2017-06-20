# Android线程池学习
### 一、为什么学习线程池
-------------
####  1、减少了创建线程的数量，提高了APP的性能
####  2、节省开销,防止并发线程过多，便于线程的管理
### 二、线程池的分类（四类）
-------------
* newCachedThreadPool 缓存型线程池:
如果池中没有线程可用，它将创建
一个线程并添加到线程池中，
线程池中尚未使用的60秒线程
将会终止并从线程池中移出，因此，
对于长期保持足够的空闲池不会消耗任何资源。

* newFixedThreadPool 固定数目的线程池
线程池中的数量初始化后（n个线程），
线程数量n不变，激活线程的最大数为n,
当需要的线程大于最大线程数量，
则其它线程在队列中排队等候，直到线程可用，
线程池的线程将一直存在，除非调用shutdown

* newScheduledThreadPool 调度型线程池
创建一个线程池后，线程数量是固定，
并一直存在线程池中，它可指定线程延时、定时
周期性的执行

* newSingleThreadExecutor 单线程池
创建线程池后，线程池有且只有一个线程，其它线程
在队列中排队等候。
与其他等效 newFixedThreadPool(1)
所返回的执行保证无需重新配置使用额外的线程。
### 三、线程池构造方法
```
ThreadPoolExecutor (int corePoolSize,
                int maximumPoolSize,
                long keepAliveTime,
                TimeUnit unit,
                BlockingQueue<Runnable> workQueue,
                ThreadFactory threadFactory)
```

corePoolSize 核心线程数量
maximumPoolSize 尚未运行的最大的线程数量
(即等候队列当中的线程数)
keepAliveTime 当线程数大于核心时,
这是多余的空闲线程等待新任务终止前的最长时间。
unit 非核心线程数等待时间的单位
workQueue 队列，用于存储已提交的任务但
未执行的任务即等候的任务
threadFactory创建线程的工厂




### 四、线程池的使用
-----------
#### 1、newCachedThreadPool
##### 第一步：初始化线程池
```
    //创建线程池
     executorService = Executors.newCachedThreadPool(threadFactory);
```

###### 源码分析 缓存型线程池特性
```
   public static ExecutorService newCachedThreadPool(ThreadFactory threadFactory) {
        return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                                      60L, TimeUnit.SECONDS,
                                      new SynchronousQueue<Runnable>(),
                                      threadFactory);
    }
```

从源码中我们发现缓存型线程池核心线程数是0，
非核心线程数是整型的最大值，
非核心线程的空闲线程等待时间是60s
单位是秒，存储非核心线程的队列SynchronousQueue

##### 第二步：创建任务(线程)
```
//任务
 private Runnable runnable1 = new Runnable() {
        @Override
        public void run() {
            try {
                Log.e("TAG","pb1:"+Thread.currentThread().getName());
                while(pb1.getProgress()< pb1.getMax()){
                    Thread.sleep(100);
                    pb1.setProgress(pb1.getProgress()+5);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };
```

##### 第三步： 提交任务
```
executorService.execute(runnable1);
```

#### 2、newFixedThreadPool
##### 第一步：初始化线程池
```
//初始化
 fixedExecutorService = Executors.newFixedThreadPool(corePoolSize,threadFactory);
```
###### 源码分析 newFixedThreadPool特性
```
 public static ExecutorService newFixedThreadPool(int nThreads, ThreadFactory threadFactory) {
        return new ThreadPoolExecutor(nThreads, nThreads,
                                      0L, TimeUnit.MILLISECONDS,
                                      new LinkedBlockingQueue<Runnable>(),
                                      threadFactory);
    }
```
非核心线程数与核心线程数相等，非核心线程数的等待时间是0毫秒，单位是毫秒，
表示非核心空闲线程不存在等待时间，即不会从缓存中移除
##### 第二步：创建任务(线程)
```
//任务
 private Runnable runnable1 = new Runnable() {
        @Override
        public void run() {
            try {
                Log.e("TAG","pb1:"+Thread.currentThread().getName());
                while(pb1.getProgress()< pb1.getMax()){
                    Thread.sleep(100);
                    pb1.setProgress(pb1.getProgress()+5);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };
```

##### 第三步： 提交任务
```
fixedExecutorService.execute(runnable1);
```

#### 3、newScheduledThreadPool
##### 第一步：初始化线程池
```
//调度型线程池
 scheduledExecutorService = Executors.newScheduledThreadPool(corePoolSize,factory);
```

###### 源码分析 调度型线程池特性
```
  public ScheduledThreadPoolExecutor(int corePoolSize,
                                       ThreadFactory threadFactory) {
        super(corePoolSize, Integer.MAX_VALUE,
              DEFAULT_KEEPALIVE_MILLIS, MILLISECONDS,
              new DelayedWorkQueue(), threadFactory);
    }

```
corePoolSize核心线程数，非核心线程数为整型的最大值，
非核心线程的空闲线程等待时间10L，单位是毫秒，
已提交但未执行的任务存储在DelayedWorkQueue队列中，
threadFactory是生产线程的线程池


##### 第二步：创建任务(线程)
```
//任务
 private Runnable runnable1 = new Runnable() {
        @Override
        public void run() {
            try {
                Log.e("TAG","pb1:"+Thread.currentThread().getName());
                while(pb1.getProgress()< pb1.getMax()){
                    Thread.sleep(100);
                    pb1.setProgress(pb1.getProgress()+5);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };
```
##### 第三步 提交任务
提交方式1：
```
//非延时周期的的任务提交
scheduledExecutorService.execute(runnable1);
```
提交方式2：
```
//延时任务提交执行
scheduledExecutorService.schedule(runnable1,10L,TimeUnit.SECONDS);
```
> 10L表示延时的时间，TimeUnit.SECONDS表示延时的时间单位
即延时10秒后提交执行任务

提交方式3：
```
scheduledExecutorService.scheduleAtFixedRate
(Runnable command, long initialDelay, long period, TimeUnit unit)
```
> command提交的任务，initialDelay延时的时间
period 任务启动后每过period时间再次启动任务
即周期性的执行任务
unit表示时间的单位

#### 4、newSingleThreadExecutor
##### 第一步：初始化线程池
```
//创建线程池
 singleExecutorService = Executors.newSingleThreadExecutor(factory);
```
源码分析 newSingleThreadExecutor线程池的特性
```
public static ExecutorService newSingleThreadExecutor(ThreadFactory threadFactory) {
        return new FinalizableDelegatedExecutorService
            (new ThreadPoolExecutor(1, 1,
                                    0L, TimeUnit.MILLISECONDS,
                                    new LinkedBlockingQueue<Runnable>(),
                                    threadFactory));
    }
```
核心线程数为1，非核心线程数为1，
非核心线程空闲线程等待时间0，即不从缓存中移除，
非核心线程的的队列采用LinkedBlockingQueue，
threadFactory表示生产线程的工厂
##### 第二步：创建任务(线程)
```
//任务
 private Runnable runnable1 = new Runnable() {
        @Override
        public void run() {
            try {
                Log.e("TAG","pb1:"+Thread.currentThread().getName());
                while(pb1.getProgress()< pb1.getMax()){
                    Thread.sleep(100);
                    pb1.setProgress(pb1.getProgress()+5);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };
```
##### 第三步 提交任务
```
singleExecutorService.execute(runnable1);
```

#### 对比LinkedBlockingQueue和ArrayBlockingQueue区别结论:
 (1)都是阻塞队列.
 (2)存储数据方式不一样:ArrayBlockingQueue使用数组方式,并且使用两个下标来表明取出数据的位置和加入数据的位置.
LinkedBlockingQueue使用的是单项链表方式存储数据,使用头和尾节点来指明链表取出数据和加入数据的地方,并且头节点不存储数据.
都通过变量来记录存储数据的数量:ArrayBlockingQueue使用int变量来记录存储数据数量,而LinkedBlockingQueue使用线程安全的AtomicInteger来记录数据数量,很显然AtomicInteger的效率更低.
(3)由于ArrayBlockingQueue采用数组方式存储数据,所以其最大容易是在定义ArrayBlockingQueue的时候就已经确定的.不能再次修改.
而LinkedBlockingQueue采用链表存储数据,所以其容易可以不用指定.
(4)向对来说,由于ArrayBlockingQueue采用数组来存储数据,所有在加入数据和获取数据时候效率都会更高.
(5)都是使用ReentrantLock来实现线程安全,不过LinkedBlockingQueue采用了两个重入锁,并且使用了 AtomicInteger,所以相对来说实现同步ArrayBlockingQueue更简单效率更高.


