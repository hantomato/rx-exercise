package com.nmj.rx.week1;

import rx.Observer;
import rx.Subscription;

/**
 * Observable interface.
 * Observable 은 Observe 를 파라미터로 받아서 subscribe 를 한다.
 * @author nmj
 *
 * @param <T>
 */
public interface IObservable<T> {
	public Subscription subscribe(Observer<T> observer);
}
