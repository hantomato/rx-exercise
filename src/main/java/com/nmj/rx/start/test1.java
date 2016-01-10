package com.nmj.rx.start;

/**
 * @author tomatomb
 *
 */
public class test1 {

	public static class MyObserve<T> implements rx.Observer {

		@Override
		public void onCompleted() {
			// TODO Auto-generated method stub
			System.out.println("MyObserve onCompleted..");
		}

		@Override
		public void onError(Throwable e) {
			// TODO Auto-generated method stub
			System.out.println("MyObserve onError..");
		}

		@Override
		public void onNext(Object t) {
			// TODO Auto-generated method stub
			System.out.println("MyObserve onNext..:" + t);
			
		}

		
	};
	
	public static void main(String... argc) {
		System.out.println("start");
		
		
		
//		rx.subjects.Subject<String, Integer> sub1 = null;// = new rx.subjects.
//		rx.subjects.BehaviorSubject<String, Integer> sub1 = new rx.subjects.BehaviorSubjectTest();
		rx.subjects.BehaviorSubjectTest sub2 = new rx.subjects.BehaviorSubjectTest();
		sub2.testSubscribeThenOnComplete();
		
		rx.subjects.BehaviorSubject<String> behaviorSubject = rx.subjects.BehaviorSubject.create();
		behaviorSubject.onNext("111");
		behaviorSubject.onNext("222");
		
		MyObserve<Integer> mmm = new MyObserve<Integer>();
		
		behaviorSubject.subscribe(mmm);
		behaviorSubject.onNext("333");
//		rx.Observer<String> obs1 = new MyObserver<String>();
		
		rx.Observer<String> observer1 = new rx.Observer() {

			@Override
			public void onCompleted() {
				// TODO Auto-generated method stub
				System.out.println("onCompleted..");
			}

			@Override
			public void onError(Throwable e) {
				// TODO Auto-generated method stub
				System.out.println("onError..");
			}

			@Override
			public void onNext(Object t) {
				// TODO Auto-generated method stub
				System.out.println("onNext..:" + t);
				
			}
			
		};
		
		behaviorSubject.subscribe(observer1);
		
		
		behaviorSubject.onNext("aaa");
		behaviorSubject.onNext("bbb");
		behaviorSubject.onCompleted();
	}
}
