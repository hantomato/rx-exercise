package com.nmj.rx.ex1;


import java.io.PrintStream;
import java.util.ArrayList;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.Observable.OnSubscribe;
import rx.functions.Action1;
import rx.functions.Func1;

public class exercise1 {
	
	
	

	public static void WriteLine(Integer i) {
		System.out.println(i);
	}
	
	
	// 1. 전달된 Subscribe 함수를 이용하여 Observable 생성 
	public static IObservable<Integer> Create(Func1<Observer<Integer>, Subscription> f) {
	
		IObservable<Integer> temp = new IObservable<Integer>() {
			@Override
			public Subscription subscribe(Observer<Integer> observer) {
				
				return f.call(observer);
			}
		};
		
		return temp;
	}

	
	// 2. seed 부터 count 개의 정수를 emit하는 Observable 생성. ex. Range(3, 5) 이 3,4,5,6,7 emit.
	public static IObservable<Integer> Range(int seed, int count) {

		IObservable<Integer> temp = new IObservable<Integer>() {
			@Override
			public Subscription subscribe(Observer<Integer> observer) {
				
				int seedTemp = seed;
				int idx = count;
				while (0 < idx--) {
					observer.onNext(seedTemp++);
				}
				observer.onCompleted();
				return null;
			}
		};
		
		return temp;
	}
	
	// 3. ms 밀리초 후에 0을 emit 하는 Observable 생성
	public static IObservable<Integer> Timer(int ms) {
		
		IObservable<Integer> temp = new IObservable<Integer>() {

			@Override
			public Subscription subscribe(Observer<Integer> observer) {
				try {
					Thread.sleep(ms);
				} catch (InterruptedException e) {
				}
				
				observer.onNext(0);;
				observer.onCompleted();
				return null;
			}
		};
		
		return temp;
	}
	
	// 4. ms 밀리초마다 0부터 시작하는 정수를 emit하는 Observable 생성
	public static IObservable<Integer> Interval(int ms) {
		IObservable<Integer> temp = new IObservable<Integer>() {

			@Override
			public Subscription subscribe(Observer<Integer> observer) {
				int i = 0;
				while (i<10) {
					
					try {
						Thread.sleep(ms);
					} catch (InterruptedException e) {
					}
					observer.onNext(i++);
				}
				return null;
			}
			
		};
		return temp;
	}
	
	
	
	public static void main(String... argc) {
		System.out.println("main start");

		//////////////////////////////////////////////////
		// exercise 2
		IObservable<Integer> var1 = Range(0, 5);
//		var1.subscribe(exercise1::WriteLine);
		var1.subscribe(new Observer<Integer>() {
			@Override
			public void onCompleted() {
				
			}

			@Override
			public void onError(Throwable e) {
				
			}

			@Override
			public void onNext(Integer t) {
				System.out.println(t);
			}
			
		});
		// result
		//		0
		//		1
		//		2
		//		3
		//		4

		//////////////////////////////////////////////////
		// exercise 3
		IObservable<Integer> var2 = Timer(1000);
		var2.subscribe(new Observer<Integer>() {

			@Override
			public void onCompleted() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onError(Throwable e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onNext(Integer t) {
				System.out.println(t);
				
			}
			
		});
		// result
		//		0

		//////////////////////////////////////////////////
		// exercise 4
		IObservable<Integer> var3 = Interval(1000);
		var3.subscribe(new Observer<Integer>() {

			@Override
			public void onCompleted() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onError(Throwable e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onNext(Integer t) {
				System.out.println(t);
				
			}
			
		});		
		
		
	}

}
