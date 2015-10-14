package com.nmj.rx.ex1;

import rx.Observer;

public class MyObserver<T> implements Observer<T> {

	@Override
	public void onNext(T t) {
		System.out.println("onNext : " + t);
	}

	@Override
	public void onError(Throwable e) {
		System.out.println("Throwable : " + e);
	}

	@Override
	public void onCompleted() {
		System.out.println("onCompleted");
	}
	
}
