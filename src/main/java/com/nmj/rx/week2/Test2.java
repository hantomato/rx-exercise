package com.nmj.rx.week2;

import java.util.concurrent.TimeUnit;

import rx.Observable;

/**
 * filter
 * - first, last, take(count or time), takeLast(count or time)
 * - skip(count or time), skipLast(count or time)
 * - distinct 중복제거, elementAt(0 base index)
 * @author nmj
 *
 */
public class Test2 {

	class Job {
		private String curThread() {
			return String.format("[%s] ",Thread.currentThread().getName());
		}
		
		public void testFilteringObservable1() {
			System.out.println("--------------------\n// first");
			Observable.range(5, 10)
			.first()
			.subscribe(
					a -> System.out.println(curThread() + " onNext:" + a),
					error -> System.out.println(curThread() + "error:" + error),
					() -> System.out.println(curThread() + "complete:"));

			System.out.println("--------------------\n// last");
			Observable.range(5, 10)
			.last()
			.subscribe(
					a -> System.out.println(curThread() + " onNext:" + a),
					error -> System.out.println(curThread() + "error:" + error),
					() -> System.out.println(curThread() + "complete:"));

			System.out.println("--------------------\n// take - count");
			Observable.range(5, 10)
			.take(3)
			.subscribe(
					a -> System.out.println(curThread() + " onNext:" + a),
					error -> System.out.println(curThread() + "error:" + error),
					() -> System.out.println(curThread() + "complete:"));

			System.out.println("--------------------\n// take - time");
			Observable.timer(0, 200, TimeUnit.MILLISECONDS)
			.take(1, TimeUnit.SECONDS)
			.subscribe(
					a -> System.out.println(curThread() + " onNext:" + a + " - take - time"),
					error -> System.out.println(curThread() + "error:" + error + " - take - time"),
					() -> System.out.println(curThread() + "complete:" + " - take - time"));

			System.out.println("--------------------\n// takeLast - count");
			Observable.range(5, 10)
			.takeLast(3)
			.subscribe(
					a -> System.out.println(curThread() + " onNext:" + a),
					error -> System.out.println(curThread() + "error:" + error),
					() -> System.out.println(curThread() + "complete:"));

			System.out.println("--------------------\n// takeLast - time => 이건 말이 안됨.");
			Observable.timer(0, 200, TimeUnit.MILLISECONDS)
			.takeLast(1, TimeUnit.SECONDS)
			.subscribe(
					a -> System.out.println(curThread() + " onNext:" + a),
					error -> System.out.println(curThread() + "error:" + error),
					() -> System.out.println(curThread() + "complete:"));
			
		}
		
		public void testFilteringObservable2() {
			System.out.println("--------------------\n// skip");
			Observable.range(3, 5)
			.skip(2)
			.subscribe(
					a -> System.out.println(curThread() + " onNext:" + a),
					error -> System.out.println(curThread() + "error:" + error),
					() -> System.out.println(curThread() + "complete:"));

			System.out.println("--------------------\n// skipLast");
			Observable.range(3, 5)
			.skipLast(2)
			.subscribe(
					a -> System.out.println(curThread() + " onNext:" + a),
					error -> System.out.println(curThread() + "error:" + error),
					() -> System.out.println(curThread() + "complete:"));

			System.out.println("--------------------\n// distinct");
			Observable.from(new String[] {"bb", "aa", "aa", "cc", "bb", "a", "d"})
			.distinct()
			.subscribe(
					a -> System.out.println(curThread() + " onNext:" + a),
					error -> System.out.println(curThread() + "error:" + error),
					() -> System.out.println(curThread() + "complete:"));

			System.out.println("--------------------\n// elementAt - 0 index");
			Observable.range(3, 5)
			.elementAt(0) // 0 base
			.subscribe(
					a -> System.out.println(curThread() + " onNext:" + a),
					error -> System.out.println(curThread() + "error:" + error),
					() -> System.out.println(curThread() + "complete:"));

			System.out.println("--------------------\n// elementAt - 2 index");
			Observable.range(3, 5)
			.elementAt(2)
			.subscribe(
					a -> System.out.println(curThread() + " onNext:" + a),
					error -> System.out.println(curThread() + "error:" + error),
					() -> System.out.println(curThread() + "complete:"));
			
			System.out.println("--------------------\n// ignoreElements ");
			Observable.range(3, 5)
			.ignoreElements()
			.subscribe(
					a -> System.out.println(curThread() + " onNext:" + a),
					error -> System.out.println(curThread() + "error:" + error),
					() -> System.out.println(curThread() + "complete:"));
			
			
		}
	}
	
	public static void main(String... argc) {
		System.out.println("main thread start - " + Thread.currentThread().getName());
		
		Test2 test = new Test2(); 
		Job job = test.new Job();
//		job.testFilteringObservable1();
		job.testFilteringObservable2();
		
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
