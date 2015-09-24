package com.nmj.rx;

public class exercise1 {
	public static void main(String... argc) {
		System.out.println("main start");

		rx.Observable.just("first", "second", "third").map(v -> v + " time")
		.subscribe(v -> System.out.println(v));
		
		System.out.println("main end..");
		
		
	}

}
