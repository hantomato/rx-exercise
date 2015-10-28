package com.nmj.rx.week3;

import java.util.HashSet;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.internal.operators.OperatorFilter;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

/*

public static IObservable<TSource> Where<TSource>(
        this IObservable<TSource> source,
        Func<TSource, bool> predicate
)
 
public static IObservable<TSource> SkipWhile<TSource>(
        this IObservable<TSource> source,
        Func<TSource, bool> predicate
)
 
public static IObservable<TSource> Distinct<TSource>(
        this IObservable<TSource> source
) 
 
*/
public class exercise {
	
//	private static Subscriber mSubscriber = null;
	private static boolean doSkip = true;
	
	public static<TSource> Observable<TSource> where(
        Observable<TSource> source,
        Func1<TSource, Boolean> predicate) {
		
		return Observable.create(subscriber -> {
			Subscriber mSubscriber = subscriber;
			source.subscribe(
					a -> {						
						if (predicate.call(a)) {
							mSubscriber.onNext(a);	
						}
					},
					e -> {
						mSubscriber.onError(e);
					},
					() -> {
						mSubscriber.onCompleted();
					});			
		});
	}

	public static String curThread() {
		return String.format("[%s] ",Thread.currentThread().getName());
	}
	
	// skipWhile : 조건이 false 가 되는 걸 만날때까지 버림. 조건이 true 된 것부터 emit 함.
	public static <TSource> Observable<TSource> skipWhile(
	        Observable<TSource> source,
	        Func1<TSource, Boolean> predicate) {
	
		return Observable.create(subscriber -> {
			Subscriber mSubscriber = subscriber;
			doSkip = true;
			source.subscribe(
					a -> {
						if (!doSkip) {
							mSubscriber.onNext(a);
						} else {
							if (predicate.call(a)) {
								; // skip	
							} else {
								doSkip = false;
								mSubscriber.onNext(a);
							}
						}
					},
					e -> {
						mSubscriber.onError(e);
					},
					() -> {
						mSubscriber.onCompleted();
					});			
		});
	}

	public static <TSource> Observable<TSource> distinct(
	        Observable<TSource> source) {
		
		return Observable.create(subscriber -> {
			Subscriber mSubscriber = subscriber;
			HashSet<TSource> setTemp = new HashSet<>();
			source.subscribe(
					a -> {
						if (!setTemp.contains(a)) {
							mSubscriber.onNext(a);
							setTemp.add(a);
						}
					},
					e -> {
						mSubscriber.onError(e);
					},
					() -> {
						mSubscriber.onCompleted();
					});			
		});
	}
	
	
	public static void main(String... argc) {
		System.out.println("main thread start - " + Thread.currentThread().getName());
		{
			System.out.println("test1 - where /////////");
			Observable ob = Observable.range(0,  10);
			Observable<Integer> newOb = where(ob, param -> param % 2 == 0);
			newOb.subscribe(a -> System.out.println("test1 onNext: " + a));
		}

		{
			System.out.println("test2 - skipWhile /////////");
			Observable ob = Observable.just(1, 2, 3, 4, 5, 6, 5, 6, 7, 8);
			Observable<Integer> newOb = skipWhile(ob, param -> param != 5);
			newOb.subscribe(a -> System.out.println("test2 onNext: " + a));			
			// 5, 6, 5, 6, 7, 8
		}
		
		{
			System.out.println("test3 - distinct /////////");
			Observable ob = Observable.just(1, 2, 3, 1, 2, 3, 4, 5);
			Observable<Integer> newOb = distinct(ob);
			newOb.subscribe(a -> System.out.println("test3 onNext: " + a));			
			// 1, 2, 3, 4, 5
		}
		
		System.out.println("main thread end - " + Thread.currentThread().getName());
		
//		main thread start - main
//		test1 - where /////////
//		test1 onNext: 0
//		test1 onNext: 2
//		test1 onNext: 4
//		test1 onNext: 6
//		test1 onNext: 8
//		test2 - skipWhile /////////
//		test2 onNext: 5
//		test2 onNext: 6
//		test2 onNext: 5
//		test2 onNext: 6
//		test2 onNext: 7
//		test2 onNext: 8
//		test3 - distinct /////////
//		test3 onNext: 1
//		test3 onNext: 2
//		test3 onNext: 3
//		test3 onNext: 4
//		test3 onNext: 5
//		main thread end - main
		
	}
			
}
