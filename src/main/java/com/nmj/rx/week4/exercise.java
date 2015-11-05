package com.nmj.rx.week4;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.internal.operators.OperatorFilter;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

/*
select(=map), selectMany(flatMap) 배워봄.

1. selectMany로 filter, map 작성하라
 
*/
public class exercise {
	public static String curThread() {
		return String.format("[%s] ",Thread.currentThread().getName());
	}
	
	public static <T> Observable<T> filter(Observable<T> ob, Func1<? super T, Boolean> predicate) {
		return ob.flatMap(
				v -> {
					if (predicate.call(v)) {
						return Observable.just(v);
					}
					return null;
				});
	}
	
	public static <T, R> Observable<R> map(Observable<T> ob, Func1<? super T, ? extends R> func) {
		return ob.flatMap(
				v -> Observable.just(func.call(v))
				);
	}
	
	public static void main(String... argc) {
		System.out.println("main thread start - " + Thread.currentThread().getName());

		
		filter(Observable.range(0,  10), v -> v % 2 == 0)
			.subscribe(
					a -> System.out.println(curThread() + " onNext:" + a),
					error -> System.out.println(curThread() + "error:" + error),
					() -> System.out.println(curThread() + "complete:"));

		map(Observable.range(0,5), a -> "item:" + a)
			.subscribe(
					a -> System.out.println(curThread() + " onNext:" + a),
					error -> System.out.println(curThread() + "error:" + error),
					() -> System.out.println(curThread() + "complete:"));
			
		
		System.out.println("main thread end - " + Thread.currentThread().getName());
		

	}
			
}
