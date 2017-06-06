package com.example.threadpoolstudy5;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import butterknife.InjectView;

public class MainActivity extends AppCompatActivity {

    @butterknife.InjectView(R.id.cache_btn_main)
    Button cacheBtnMain;
    @butterknife.InjectView(R.id.fixed_btn_main)
    Button fixedBtnMain;
    @butterknife.InjectView(R.id.sche_btn_main)
    Button scheBtnMain;
    @butterknife.InjectView(R.id.single_btn_main)
    Button singleBtnMain;
    @butterknife.InjectView(R.id.reset_btn_main)
    Button resetBtnMain;
    @butterknife.InjectView(R.id.pb1)
    ProgressBar pb1;
    @butterknife.InjectView(R.id.pb2)
    ProgressBar pb2;
    @butterknife.InjectView(R.id.pb3)
    ProgressBar pb3;
    @butterknife.InjectView(R.id.pb4)
    ProgressBar pb4;
    @butterknife.InjectView(R.id.pb5)
    ProgressBar pb5;
    @butterknife.InjectView(R.id.pb6)
    ProgressBar pb6;
    @butterknife.InjectView(R.id.pb7)
    ProgressBar pb7;
    @butterknife.InjectView(R.id.pb8)
    ProgressBar pb8;
    @butterknife.InjectView(R.id.pb9)
    ProgressBar pb9;
    @butterknife.InjectView(R.id.pb10)
    ProgressBar pb10;
    @InjectView(R.id.double_btn)
    Button doubleBtn;

    private final int corePoolSize = 3;//核心线程数
    private final int maximumPoolSize = 10;//尚未运行的（队列中）最大线程数
    private final int keepAliveTime = 5;//非核心空闲线程的等待的最长时间
    private final TimeUnit unit = TimeUnit.SECONDS;//keepAliveTime的单位
    //创建线程的工厂
    private static ThreadFactory threadFactory = new ThreadFactory() {
        AtomicInteger integer = new AtomicInteger();
        @Override
        public Thread newThread(@NonNull Runnable r) {
            return new Thread(r,"thread:"+integer.getAndIncrement());
        }
    };
    //用于存储已提交的任务但 未执行的任务即等候的任务
    private  BlockingQueue<Runnable> workQueue  =
            new ArrayBlockingQueue<Runnable>(maximumPoolSize);
    private ExecutorService executorService;
    private ExecutorService fixedExecutorService;
    private ScheduledExecutorService schuleExecutorService;
    private ExecutorService singleExecutorService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        butterknife.ButterKnife.inject(this);

        //创建线程池
        executorService = Executors.newCachedThreadPool(threadFactory);//缓存型
        fixedExecutorService = Executors.newFixedThreadPool(corePoolSize,threadFactory);//固定型的线程池
        schuleExecutorService = Executors.newScheduledThreadPool(corePoolSize,threadFactory);//调度型线程池
        singleExecutorService = Executors.newSingleThreadExecutor(threadFactory);//单线程池
    }

    @butterknife.OnClick({R.id.double_btn,R.id.cache_btn_main, R.id.fixed_btn_main, R.id.sche_btn_main, R.id.single_btn_main, R.id.reset_btn_main})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.double_btn:
                //二次下载
                singleExecutorService.execute(runnable6);
                singleExecutorService.execute(runnable7);
                singleExecutorService.execute(runnable8);
                singleExecutorService.execute(runnable9);
                singleExecutorService.execute(runnable10);
                break;
            case R.id.cache_btn_main:
                executorService.execute(runnable1);
                executorService.execute(runnable2);
                executorService.execute(runnable3);
                executorService.execute(runnable4);
                executorService.execute(runnable5);
                executorService.execute(runnable6);
                executorService.execute(runnable7);
                executorService.execute(runnable8);
                executorService.execute(runnable9);
//                executorService.execute(runnable10);

                break;
            case R.id.fixed_btn_main:
                fixedExecutorService.execute(runnable1);
                fixedExecutorService.execute(runnable2);
                fixedExecutorService.execute(runnable3);
                fixedExecutorService.execute(runnable4);
                fixedExecutorService.execute(runnable5);
                fixedExecutorService.execute(runnable6);
                fixedExecutorService.execute(runnable7);
                fixedExecutorService.execute(runnable8);
                fixedExecutorService.execute(runnable9);
                fixedExecutorService.execute(runnable10);
                break;
            case R.id.sche_btn_main:
                schuleExecutorService.execute(runnable1);
                schuleExecutorService.execute(runnable2);
                schuleExecutorService.execute(runnable3);
                schuleExecutorService.execute(runnable4);
                schuleExecutorService.execute(runnable5);
                schuleExecutorService.schedule(runnable6,10L,TimeUnit.SECONDS);
                schuleExecutorService.schedule(runnable7,20L,TimeUnit.SECONDS);
                schuleExecutorService.schedule(runnable8,30L,TimeUnit.SECONDS);
                schuleExecutorService.schedule(runnable9,40L,TimeUnit.SECONDS);
                schuleExecutorService.schedule(runnable10,50L,TimeUnit.SECONDS);
                break;
            case R.id.single_btn_main:
                singleExecutorService.execute(runnable1);
                singleExecutorService.execute(runnable2);
                singleExecutorService.execute(runnable3);
                singleExecutorService.execute(runnable4);
                singleExecutorService.execute(runnable5);
//                singleExecutorService.execute(runnable6);
//                singleExecutorService.execute(runnable7);
//                singleExecutorService.execute(runnable8);
//                singleExecutorService.execute(runnable9);
//                singleExecutorService.execute(runnable10);
                break;
            case R.id.reset_btn_main:
                break;
        }
    }


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
    private Runnable runnable2 = new Runnable() {
        @Override
        public void run() {
            try {
                Log.e("TAG","pb2:"+Thread.currentThread().getName());
                while(pb2.getProgress()< pb2.getMax()){
                    Thread.sleep(100);
                    pb2.setProgress(pb2.getProgress()+5);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };
    private Runnable runnable3 = new Runnable() {
        @Override
        public void run() {
            try {
                Log.e("TAG","pb3:"+Thread.currentThread().getName());
                while(pb3.getProgress()< pb3.getMax()){
                    Thread.sleep(100);
                    pb3.setProgress(pb3.getProgress()+5);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };
    private Runnable runnable4 = new Runnable() {
        @Override
        public void run() {
            try {
                Log.e("TAG","pb4:"+Thread.currentThread().getName());
                while(pb4.getProgress()< pb4.getMax()){
                    Thread.sleep(100);
                    pb4.setProgress(pb4.getProgress()+5);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };
    private Runnable runnable5 = new Runnable() {
        @Override
        public void run() {
            try {
                Log.e("TAG","pb5:"+Thread.currentThread().getName());
                while(pb5.getProgress()< pb5.getMax()){
                    Thread.sleep(100);
                    pb5.setProgress(pb5.getProgress()+5);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };
    private Runnable runnable6 = new Runnable() {
        @Override
        public void run() {
            try {
                Log.e("TAG","pb6:"+Thread.currentThread().getName());
                while(pb6.getProgress()< pb6.getMax()){
                    Thread.sleep(100);
                    pb6.setProgress(pb6.getProgress()+5);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };
    private Runnable runnable7 = new Runnable() {
        @Override
        public void run() {
            try {
                Log.e("TAG","pb7:"+Thread.currentThread().getName());
                while(pb7.getProgress()< pb7.getMax()){
                    Thread.sleep(100);
                    pb7.setProgress(pb7.getProgress()+5);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };
    private Runnable runnable8 = new Runnable() {
        @Override
        public void run() {
            try {
                Log.e("TAG","pb8:"+Thread.currentThread().getName());
                while(pb8.getProgress()< pb8.getMax()){
                    Thread.sleep(100);
                    pb8.setProgress(pb8.getProgress()+5);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };
    private Runnable runnable9 = new Runnable() {
        @Override
        public void run() {
            try {
                Log.e("TAG","pb9:"+Thread.currentThread().getName());
                while(pb9.getProgress()< pb9.getMax()){
                    Thread.sleep(100);
                    pb9.setProgress(pb9.getProgress()+5);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };
    private Runnable runnable10 = new Runnable() {
        @Override
        public void run() {
            try {
                Log.e("TAG","pb10:"+Thread.currentThread().getName());
                while(pb10.getProgress()< pb10.getMax()){
                    Thread.sleep(100);
                    pb10.setProgress(pb10.getProgress()+5);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };
}
