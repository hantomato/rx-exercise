package com.nmj.rx.week4;

import java.util.concurrent.TimeUnit;

import rx.Observable;

/** 
Map(=select) 에 대해 알아보자.
    public final <R> Observable<R> map(Func1<? super T, ? extends R> func) {

	* Observable<R>을 리턴한다.
	* 파라미터로 Func1을 받는데.. 이건.. Func1<T,R> 임.

flatMap(=selectMany)
    public final <R> Observable<R> flatMap(Func1<? super T, ? extends Observable<? extends R>> func) {


 */
public class Test1 {

	class Job {
		
		private String curThread() {
			return String.format("[%s] ",Thread.currentThread().getName());
		}
		
		public void testMap() {
	
			Observable.range(0, 5)
			.map(i -> {
				return "item " + (i * 2);	// 리턴형은 source와 틀림.
			})
			.takeLast(3)
			.subscribe(
					a -> System.out.println(curThread() + " onNext:" + a),
					error -> System.out.println(curThread() + "error:" + error),
					() -> System.out.println(curThread() + "complete:"));
			
		}

		public void testMap2() {

			Observable.interval(1, 1000, TimeUnit.MILLISECONDS)
			.map(i -> {
				return "item " + (i * 2);	// 리턴형은 source와 틀림.
			})
			.take(3)
			.subscribe(
					a -> System.out.println(curThread() + " onNext:" + a),
					error -> System.out.println(curThread() + "error:" + error),
					() -> System.out.println(curThread() + "complete:"));
			
		}
		
		public void testFlatMap() {
			Observable.range(0, 5)
			.flatMap(i -> {
				return Observable.range(i, 2);
			})
			.subscribe(
					a -> System.out.println(curThread() + " onNext:" + a),
					error -> System.out.println(curThread() + "error:" + error),
					() -> System.out.println(curThread() + "complete:"));
		}

		public void testFlatMap2() {
			Observable.range(1, 3)
			.flatMap(i -> {
				return Observable.interval(200, 200, TimeUnit.MILLISECONDS)
						.map(j ->j + i * 10)
						.take(5 - i);
			})
			.subscribe(
					a -> System.out.println(curThread() + " onNext:" + a),
					error -> System.out.println(curThread() + "error:" + error),
					() -> System.out.println(curThread() + "complete:"));
			
//		[RxComputationThreadPool-4]  onNext:20
//		[RxComputationThreadPool-4]  onNext:10
//		[RxComputationThreadPool-4]  onNext:30
//		[RxComputationThreadPool-3]  onNext:11
//		[RxComputationThreadPool-3]  onNext:21
//		[RxComputationThreadPool-3]  onNext:31
//		[RxComputationThreadPool-4]  onNext:22
//		[RxComputationThreadPool-3]  onNext:12
//		[RxComputationThreadPool-3]  onNext:13		
		}
		
	}
	
	public static void main(String... argc) {
		System.out.println("main thread start - " + Thread.currentThread().getName());
		
		Test1 test = new Test1(); 
		Job job = test.new Job();
//		job.testMap();
//		job.testMap2();
//		job.testFlatMap();
		job.testFlatMap2();
		
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
