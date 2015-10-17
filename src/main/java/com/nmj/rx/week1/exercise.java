package com.nmj.rx.week1;


import java.util.TimerTask;
import java.util.Timer;

import rx.Observer;
import rx.Subscription;
import rx.functions.Func1;

public class exercise {
	
	// 1. 전달된 Subscribe 함수를 이용하여 Observable 생성 
	public static IObservable<Integer> Create(Func1<Observer<Integer>, Subscription> subscribe) {
	
		IObservable<Integer> observable = new IObservable<Integer>() {
			@Override
			public Subscription subscribe(Observer<Integer> observer) {
				return subscribe.call(observer);
			}
		};
		return observable;
	}

	
	// 2. seed 부터 count 개의 정수를 emit하는 Observable 생성. ex. Range(3, 5) 이 3,4,5,6,7 emit.
	public static IObservable<Integer> Range(int seed, int count) {
		
		IObservable<Integer> observable = Create((observer) -> {
			int seedTemp = seed;
			int idx = count;
			while (0 < idx--) {
				observer.onNext(seedTemp++);
			}
			observer.onCompleted();
			return null;
		});
		return observable;
	}
	
	// 3. ms 밀리초 후에 0을 emit 하는 Observable 생성
	// rx는 기본적으로 NonBlock 으로 구현되어야 함.
	public static IObservable<Integer> Timer(int ms) {
		
		// 잘못된 예시.
//		IObservable<Integer> observable = Create(observer -> {
//			try {
//				Thread.sleep(ms);
//			} catch (InterruptedException e) {
//			}
//			
//			observer.onNext(0);;
//			observer.onCompleted();			
//			return null;
//		});

		// 올바른 예시
		IObservable<Integer> observable = Create(observer -> {
			Timer timer = new Timer();
			timer.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					this.cancel();
					observer.onNext(0);
					observer.onCompleted();					
				}
			}, ms, 10000);
			
			return null;
		});
		
		return observable;
	}
	
	// 4. ms 밀리초마다 0부터 시작하는 정수를 emit하는 Observable 생성
	public static IObservable<Integer> Interval(int ms) {
		
		
		IObservable<Integer> observable = Create(observer -> {
			Timer timer = new Timer();
			timer.scheduleAtFixedRate(new MyTimerTask(observer), ms, ms);			
			return null;
		});

		return observable;
	}
	
	public static class MyTimerTask extends TimerTask {
		Observer<Integer> observer;
		int idx = 0;
		
		public MyTimerTask(Observer<Integer> observer) {
			this.observer = observer;
		}
		
		@Override
		public void run() {
			observer.onNext(idx++);
		}
	}
	
	
		
	public static void main(String... argc) {
		System.out.println("main thread start - " + Thread.currentThread().getName());


		//////////////////////////////////////////////////
		// exercise 2
		System.out.println("///// exercise 2");
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
		System.out.println("///// exercise 3");
		IObservable<Integer> var2 = Timer(2000);
		Subscription sub = var2.subscribe(new Observer<Integer>() {

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

//		//////////////////////////////////////////////////
//		// exercise 4
		System.out.println("///// exercise 4");
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
				System.out.println(t + " - " + Thread.currentThread().getName());
				
			}
			
		});		
		
		System.out.println("main thread terminated - " + Thread.currentThread().getName());		
	}

}
