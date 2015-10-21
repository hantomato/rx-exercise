package com.nmj.rx.week2;

import java.util.LinkedList;
import java.util.Queue;

import com.nmj.rx.week1.IObservable;
import com.nmj.rx.week2.Test2.Job;

import rx.Observable;
import rx.functions.Func1;

public class exercise {

	public static String curThread() {
		return String.format("[%s] ",Thread.currentThread().getName());
	}
	
    // public static IObservable<TResult> Generate<TState, TResult> (
    //   TState initialState,
    //   Func<TState, bool> condition,
    //   Func<TState, TState> iterate,
    //   Func<TState, TResult> resultSelector)

	public static<TState, TResult> Observable<TResult> generate(TState initialState,
			Func1<TState, Boolean> condition,
			Func1<TState, TState> iterate,
			Func1<TState, TResult> resultSelector) {
		
		return Observable.create(subscriber -> {
			for (TState i = initialState; condition.call(i) && !subscriber.isUnsubscribed(); i = iterate.call(i)) {
				subscriber.onNext(resultSelector.call(i));
			}
			
			subscriber.onCompleted();
		});
	}
	
	
	public static Observable<Integer> Range(int seed, int count) {
		
		Observable<Integer> observable = generate(
				seed,
				value -> value < seed + count,
				value -> value + 1,
				value -> {
					System.out.println("emit:" + value);
					return value; 
				});
		
		return observable;
		
	}
	
	public static Observable<Integer> fibonacci() {
		
		class Fibona {
			int newValue = 0;
			int prev1 = 0;
			int prev2 = 1;

			public Observable<Integer> createFibonacci() {
				Observable<Integer> observable = Observable.create(
						subscriber -> {
							int n = 0;
							while (!subscriber.isUnsubscribed()) {
								if (n == 0) {
									subscriber.onNext(0);
								} else if(n == 1) {
									subscriber.onNext(1);
								} else {
									newValue = prev1 + prev2;
									subscriber.onNext(newValue);
									prev1 = prev2;
									prev2 = newValue;
								}
								n++;
							}
							subscriber.onCompleted();
						});
				return observable;
			}
		}
		
		return new Fibona().createFibonacci();
	}
	
	
	
	public static void main(String... argc) {
		System.out.println("main thread start - " + Thread.currentThread().getName());
		
		// 1. Generate를 이용하여 Range 재작성 해보기.
		Observable<Integer> obs1 = Range(5, 10).take(5);
		obs1.subscribe(
				a -> System.out.println(curThread() + "   onNext:" + a), 
				e -> System.out.println(curThread() + "   error"), 
				() -> System.out.println(curThread() + " onComplete"));
/////// result
//		emit:5
//		[main]    onNext:5
//		emit:6
//		[main]    onNext:6
//		emit:7
//		[main]    onNext:7
//		emit:8
//		[main]    onNext:8
//		emit:9
//		[main]    onNext:9
//		[main]  onComplete
		
		// 2. Fibonacci 수열을 emit하는 Observable 만들어보기
		Observable<Integer> obs2 = fibonacci();
		obs2.take(10).subscribe(
				a -> System.out.println(curThread() + "   onNext:" + a), 
				e -> System.out.println(curThread() + "   error"), 
				() -> System.out.println(curThread() + " onComplete"));
/////// result
//		[main]    onNext:0
//		[main]    onNext:1
//		[main]    onNext:1
//		[main]    onNext:2
//		[main]    onNext:3
//		[main]    onNext:5
//		[main]    onNext:8
//		[main]    onNext:13
//		[main]    onNext:21
//		[main]    onNext:34
//		[main]  onComplete
		
		System.out.println("main thread end - " + Thread.currentThread().getName());
	}
			
}
