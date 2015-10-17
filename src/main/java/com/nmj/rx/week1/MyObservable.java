package com.nmj.rx.week1;

import rx.Observer;
import rx.Subscription;

/**
 * 옵저버블.
 * 이 옵저버블에 하나의 옵저버블을 subscribe 한다. 그러면 subscription 이 리턴된다.
 * 즉, 옵저버블과 옵저버가 연결되었음을 의미한다.
 * subscription 을 이용해서 나중에 unSubscribe 할수 있다. 
 * @author nmj
 *
 */
public class MyObservable implements IObservable<Integer> {

	@Override
	public Subscription subscribe(Observer<Integer> observer) {
		observer.onNext(1);
		observer.onNext(2);
		observer.onNext(3);
		observer.onCompleted();
		return null;
	}
}
