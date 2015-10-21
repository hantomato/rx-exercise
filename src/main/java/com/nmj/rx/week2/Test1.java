package com.nmj.rx.week2;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.schedulers.Schedulers;

/** 
 * Observable 이 제공하는 각종 기능에 대해 테스트.
	Operators that originate new Observables.

	Create — create an Observable from scratch by calling observer methods programmatically
	Defer — do not create the Observable until the observer subscribes, and create a fresh Observable for each observer
	Empty/Never/Throw — create Observables that have very precise and limited behavior
	From — convert some other object or data structure into an Observable
	Interval — create an Observable that emits a sequence of integers spaced by a particular time interval
		Just — convert an object or a set of objects into an Observable that emits that or those objects
		Range — create an Observable that emits a range of sequential integers
	Repeat — 없네 
	Start — create an Observable that emits the return value of a function
	Timer — create an Observable that emits a single item after a given delay
 * @author nmj
 *
 */
public class Test1 {

	class Job {
		private String curThread() {
			return String.format("[%s] ",Thread.currentThread().getName());
		}
		public void testCreatingObservable() {
			
//			Observable.create ??

			System.out.println("--------------------\n// test empty");
			Observable.empty().subscribe(
					a -> System.out.println(curThread() + "onNext:" + a),
					error -> System.out.println(curThread() + "error:" + error),
					() -> System.out.println(curThread() + "complete:"));
			
			System.out.println("--------------------\n// test never");
			Observable.never().subscribe(
					a -> System.out.println(curThread() + "onNext:" + a),
					error -> System.out.println(curThread() + "error:" + error),
					() -> System.out.println(curThread() + "complete:"));
			
			System.out.println("--------------------\n// test just");
			Observable.just("aaa", "bbb", "ccc").subscribe(
					a -> System.out.println(curThread() + " onNext:" + a),
					error -> System.out.println(curThread() + "error:" + error),
					() -> System.out.println(curThread() + "complete:"));

			System.out.println("--------------------\n// test from");
			Observable.from(new String[] {"aa", "bb", "cc"}).subscribe(
					a -> System.out.println(curThread() + " onNext:" + a),
					error -> System.out.println(curThread() + "error:" + error),
					() -> System.out.println(curThread() + "complete:"));

			System.out.println("--------------------\n// test range");
			Observable.range(5, 3).subscribe(
					a -> System.out.println(curThread() + "onNext:" + a),
					error -> System.out.println(curThread() + "error:" + error),
					() -> System.out.println(curThread() + "complete:"));
			
			System.out.println("--------------------\n// test timer");
			Observable.timer(500, 1000, TimeUnit.MILLISECONDS).subscribe(
//			Observable.timer(500, 1000, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.immediate()).subscribe( // 결과 같음. 별도 쓰레드임..
					a -> System.out.println(curThread() + "test timer - onNext:" + a),
					error -> System.out.println(curThread() + "test timer - error:" + error),
					() -> System.out.println(curThread() + "test timer - complete:"));
			
			System.out.println("--------------------\n// test repeat");
			Observable.just("ccc", "ddd")
					.repeat(3)
					.subscribe(
					a -> System.out.println(curThread() + " onNext:" + a),
					error -> System.out.println(curThread() + "error:" + error),
					() -> System.out.println(curThread() + "complete:"));

			
			
//			System.out.println("--------------------\n// test repeat");
//			Observable.fr("ccc", "ddd").subscribe(
//					a -> System.out.println(curThread() + " onNext:" + a),
//					error -> System.out.println(curThread() + "error:" + error),
//					() -> System.out.println(curThread() + "complete:"));			
			
		}
	}
	
	public static void main(String... argc) {
		System.out.println("main thread start - " + Thread.currentThread().getName());
		
		Test1 test = new Test1(); 
		Job job = test.new Job();
		job.testCreatingObservable();
		
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
