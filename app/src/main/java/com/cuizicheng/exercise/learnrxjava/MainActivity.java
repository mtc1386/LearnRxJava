package com.cuizicheng.exercise.learnrxjava;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.internal.operators.OperatorMerge;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    static final String TAG = "rx";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        exercise6();
    }

    void logD(String s) {
        Log.d(TAG, s);
    }


    private void exercise6() {
        //flatmap 内部实现：通过map将时间数据转换为Observable，lift(OperatorMerge),负责将收到的多个Observable汇集成一个，
        //注意:实际是OperatorMerge 完成的将多个Observable汇集一个的工作，lift()本身是产生新Observable，当它被订阅时，通知当前Observable把数据传递给Operator产生的Subscriber处理。
        Observable.just(10, 20, 30).map(new Func1<Integer, Observable<String>>() {
            @Override
            public Observable<String> call(Integer integer) {
                logD("call " + integer.toString());
                return Observable.just(integer.toString());
            }
        }).lift(OperatorMerge.<String>instance(false)).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                logD(s);
            }
        });
    }


    private void exercise5() {
        //flatmap
        Observable.just(1, 2, 3).flatMap(new Func1<Integer, Observable<String>>() {
            @Override
            public Observable<String> call(Integer integer) {
                String num = integer.toString();
                String[] nums = {num, num + num, num + num + num};
                return Observable.from(nums);
            }
        }).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                logD(s);
            }
        });
    }

    private void exercise4() {
        //对象变换
        Observable.just(1, 2, 3).map(new Func1<Integer, Integer>() {
            @Override
            public Integer call(Integer integer) {
                return integer + 4;
            }
        }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                logD(integer.toString());
            }
        });
    }


    private void exercise3() {
        //切换线程
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                logD("in OnSubscrible.call Thread id " + Thread.currentThread().getId());
                subscriber.onNext("A");
                subscriber.onNext("B");
                subscriber.onNext("C");
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        logD("Thread id: " + Thread.currentThread().getId() + ", " + s);
                    }
                });
    }


    private void exercise2() {
        //简化观察者回调
        String[] words = new String[]{"Zerg", "Terran", "Protoss"};
        //Action1 可充当onNext,onError
        Observable.from(words).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                logD(s);
            }
        });

        //Action0 可充当onCompleted
        Observable.from(words).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {

            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {

            }
        }, new Action0() {
            @Override
            public void call() {
                logD("exercise2 onCompeted");
            }
        });
    }

    private void exercise1() {
        //简化产生事件
        String words = "a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z";
        Observable.from(words.split(",")).subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {
                logD("exercise1 Completed");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                logD(s);
            }
        });
    }

    private void exercise0() {
        Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                //产生事件,发送
                subscriber.onNext("1");
                subscriber.onNext("2");
                subscriber.onNext("3");
                subscriber.onCompleted();
            }
        }).subscribe(new Observer<String>() {

            @Override
            public void onCompleted() {
                logD("exercise0 completed");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                logD(s);
            }
        });
    }


}
