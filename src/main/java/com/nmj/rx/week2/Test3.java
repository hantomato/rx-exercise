package com.nmj.rx.week2;

import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Subscriber;
import rx.functions.Action1;

import com.nmj.rx.week2.Test2.Job;
/*
 * Observable.create 에 대해 알아보자

    public final static <T> Observable<T> create(OnSubscribe<T> f);
	public interface OnSubscribe<T> extends Action1<Subscriber<? super T>> {
	
 */
public class Test3 {
	
	private String curThread() {
		return String.format("[%s] ",Thread.currentThread().getName());
	}

	class Job {
		private String curThread() {
			return String.format("[%s] ",Thread.currentThread().getName());
		}
		
		public void testCreate1() {
			// create
			Observable<String> observable = Observable.create(new OnSubscribe<String>() {
				@Override
				public void call(Subscriber<? super String> t) {
					// TODO Auto-generated method stub
					t.onNext("aaa");
					t.onNext("bbb");
					t.onNext("ccc");
					t.onCompleted();
				}
				
			});
			
			observable.subscribe(a -> System.out.println(curThread() + "   onNext:" + a), 
					e -> {}, 
					() -> System.out.println(curThread() + " onComplete"));
		}
		
		public void testCreate2() {
			Observable<Integer> observable = Observable.create(
					subscriber -> {
						subscriber.onNext(1);
						subscriber.onNext(2);
						subscriber.onCompleted();
					});
			
			observable.subscribe(a -> System.out.println(curThread() + "   onNext:" + a), 
					e -> {}, 
					() -> System.out.println(curThread() + " onComplete"));
		}
	}
	
//    public interface OnSubscribe<T> extends Action1<Subscriber<? super T>> {
//        // cover for generics insanity
//    }
	
	
	public static void main(String... argc) {
		System.out.println("main thread start - " + Thread.currentThread().getName());
		
		Test3 test = new Test3(); 
		Job job = test.new Job();
		job.testCreate2();
		
		// main thread 가 5초 동안은 살아있게하기 위함.
		for (int i=0; i<5; ++i) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("main thread end - " + Thread.currentThread().getName());
		
	}
}
