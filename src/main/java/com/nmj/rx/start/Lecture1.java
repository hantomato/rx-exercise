package com.nmj.rx.start;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Subscription;

/**
 * rx를 배워보자.
 * 
 * 흔히 알고 있는 Observer 패턴을 신문 구독을 예로 들어보겠다.
 * 우리가 신문사에 신문 구독을 하면 정기적으로 신문이 발송될것이다.
 * 
 * 여기에는 2개의 Object가 있다.
 *       
 *       						  Observer(독자)
 *       Observable                   ...
 *         (신문사)				  Observer(독자) N개 
 * 
 * 
 * 신문사를 Observable 라고 해보자.독자는 Observer 라고 한다. 독자는 여러명이 있을수 있다.
 * 특정 독자가 신문사에 구독을 신청한다.
 * 
 *       						  Observer(독자)
 *       Observable                   ...
 *         (신문사)	<------------ Observer(독자)
 * 					  Subscribe(구독요청)
 * 
 * 여기에 구독 요청 하는 것은 행위이다. Object가 아니다.
 * 구독요청하는 행위를 Subscribe 라고 하자.
 * 
 * 구독 요청이 이뤄진 후에는 신문사는 해당 독자에게 신문을 발송할것이다.
 * 근데 신문 종류는 다양할것이다. 일간 신문, 석간 신문, 일요일마다 발송하는 일요신문 등등..
 * 그리고 신문구독을 할때는 언제까지 구독하겠다고 기간을 정하겠지.
 * 즉, 구독 요청은 일종의 계약으로 봐야하고, 어떤 것을, 언제, 어떻게 전달받을지를 정하는 것이다.
 * 
 * 바로 위에서 설명한 것은 현실에서의 상황이고, rx의 세계에서는 계약서라는게 없다.
 * 신문사마다 이미 정해진 규칙이 있고, 독자는 원하는 규칙을 갖고있는 신문사를 선택하는 구조이다.
 * 구독 요청은 단순히 신문사가 규정하고 있는 규칙에 맞게 구독을 하겠다는 것이다.
 * 
 *  
 *      A Observable
 *      B Observable
 *       	....			      Observer(독자)
 *      Z Observable N                   ...
 *         (신문사)	              Observer(독자) N
 * 				
 *                                * 독자는 내 입맛에 맞는 신문사를 선택함. 
 *     * ex) A Observable : 일요신문 발간
 *           B Observable : 석간신문 발간
 *           Z Observable : 특별판을 딱 한번만 발간
 *
 * 자. 여기서 rx의 Observable 샘플 코드를 한번 살펴보자.   
 * 아래 ObservableExample() 을 살펴보자.
 * 세개의 Observable이 있고, 각각의 Observable은 수행하는 행위가 틀리고, 작업이 종료되는 시점도 틀리다. 
 * 독자는 필요한 Observable을 선택하면 되는 식이다.
 * 
 * 여태 배운것을 정리하면.
 * 세가지 용어를 배웠다. Observable, Observer, Subscribe.
 * Observable 혼자서는 아무 의미 없다. Observer 가 한개 이상이 Observable에 Subscribe 되있어야 한다.
 * 
 * 이제 Observer 에 대해 알아보자.
 * rx에서 Observer는 Observer interface의 구현체이다. 

	public interface Observer<T> {
	    void onCompleted();
	    void onError(Throwable e);
	    void onNext(T t);
	}
 * 
 * 이는 rx에서 세계에서 Observable과 약속한 규정 같은 것이다.
 * 
 * 신문사로 예를 들면, 독자가 조간 신문을 한달간 계약했다면, 
 * 신문사는 매일 1번씩 신문을 발송하며, 총 30번 발송하면, 계약이 종료될것이다.
 * 이를 rx의 코드로 설명하면, 
 * 매일 1번씩 onNext() 가 호출되고, 30번의 onNext()가 호출된 후 onCompleted() 가 호출된다.
 * 
 * Observer interface를 다시 살펴보자.
 * 
	public interface Observer<T> {
	    void onCompleted();			// 구독이 끝날때 한번 호출된다. 구독 종료됨.
	    void onError(Throwable e);	// 에러가 발생할때 한번 호출된다. 구독 종료됨.
	    void onNext(T t);			// 신문 발송(emit)을 의미.
	}

 * 이제 rx의 Observer 코드를 살펴보자. 
 * ObserverExample() 코드를 보고 오자.
 * 
 * 이제 Observable, Observer 의 개념에 대해 알아보았다.
 * 더불어서 Subscribe 라는 행위에 대해서도 살펴보았는데,
 * Observable 과 Observer 을 연결한다 또는 Observer를 Obserable에 attach 한다 라는 의미로 파악하면 되겠다. 
 * 
 * 
 * !! 구독 중간에 종료하기.
 * 
 * 현실에서 6달 짜리 신문을 구독했지만, 부득이하게 중도에 해약하고싶을때가 있을것이다.
 * rx의 세계에선 중도해약할려면 구독신청(Subscribe) 할때 받은 계약서 같은게 필요하다.
 * 여기서 계약서에 해당하는게 Subscription 이다. 
 * 이 Subscription 을 사용해서 '현재 구독중인지?'를 알수도 있고, 구독을 종료할수 있다.
 * 
 * unSubscribeExample() 코드를 살펴보자.
 * 
 * 여기까지 rx의 기초에 대해 살펴보았다.
 * 
 * @author tomatomb
 *
 */


public class Lecture1 {
	static Subscription sub = null;

	public static void ObservableExample() {
		// Observable example.
		
		// week 를 'Sunday' 부터 emit 하고 작업을 종료한다.
		Observable<String> emitWeekObservable = Observable.just(
				"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday");
		
		// 1부터 10까지 Integer를 emit 하고 작업을 종료한다.
		Observable<Integer> count1_10Observable = Observable.just(1,2,3,4,5,6,7,8,9,10);

		// 1초 간격으로 무한히 emit 한다. (숫자를 0부터 emit 하지만 이게 중요한건 아니다) 
		Observable<Long> timerObservable = Observable.interval(1000, TimeUnit.MILLISECONDS);
		
		// 근데 위의 Observable 들이 정상동작 하는지 확인하기 위해 Observer를 붙여서(Subscribe) 해서 확인을 해보자.
		// 아직은 출력되는 내용만 확인하고, 아래 코드는 한번 보고 넘어가자.
		emitWeekObservable.subscribe(a -> System.out.println(a));
		count1_10Observable.subscribe(a -> System.out.println(a));
		timerObservable.subscribe(a -> System.out.println(a));			
	}
	
	
	public static void ObserverExample() {
		// Observer example.

		// 어떤것을 emit 받으면 print 하는 Observer
		Observer PrintObserver = new Observer() {
			@Override
			public void onCompleted() {
				System.out.println("onCompleted");
			}

			@Override
			public void onError(Throwable e) {
				System.out.println("onError:" + e);
			}

			@Override
			public void onNext(Object t) {
				System.out.println("onNext:" + t);	// emit 된 t 를 출력하자.
			}
		};
		
		// 어떤것을 emit 받으면 서버 DB에 저장하는 Observer
		Observer SaveToServerObserver = new Observer() {
			@Override
			public void onCompleted() {
				System.out.println("onCompleted");
			}

			@Override
			public void onError(Throwable e) {
				System.out.println("onError:" + e);
			}

			@Override
			public void onNext(Object t) {
				// 여기서 서버 API를 호출하자. 귀찮으니 코드는 print로 대체.
				System.out.println("saveToServer:" + t);
			}
		};
		
		// Observable 이든, Observer든 간에 혼자서는 아무 의미가 없다. 둘이 연결되어야 의미있는 행위가 수행된다.
		// 위에 예제 코드에서 만든 Observable과 Observer를 연결해보자.
		
		Observable<String> emitWeekObservable = Observable.just(
				"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday");

		// week 를 출력하는 Observable 에다가 위에서 만든 Observer를 Subscribe 하였다.
		emitWeekObservable.subscribe(PrintObserver);
		emitWeekObservable.subscribe(SaveToServerObserver);
		
		// 이제 어떤 의미있는 행위가 수행된 것이다.	
	}
	
	public static void unSubscribeExample() {
		
		// 1초 간격으로 무한히 emit 한다.
		Observable<Long> timerObservable = Observable.interval(1000, TimeUnit.MILLISECONDS);
		
		// 어떤것을 emit 받으면 print 하는 Observer
		Observer PrintObserver = new Observer() {
			@Override
			public void onCompleted() {
				System.out.println("onCompleted");
			}

			@Override
			public void onError(Throwable e) {
				System.out.println("onError:" + e);
			}

			@Override
			public void onNext(Object t) {
				System.out.println("onNext:" + t);
				if (t instanceof Long && (Long)t == 5) {
					if (!sub.isUnsubscribed()) {	// 현재 구독중인지 아닌지 체크.
						sub.unsubscribe();			// 구독 종료.
					}
				}
			}
		};
		
		// Observable 에 Observer 를 subscribe(attach 같은거) 한다고 보면 된다.
		// subscribe 되자마 Observable 고유의 역할이 수행될것이다.
		sub = timerObservable.subscribe(PrintObserver);	
	}	
	
	public static void main(String... argc) throws IOException {
		ObservableExample();
//		ObserverExample();
//		unSubscribeExample();

		System.in.read();
	}
}
