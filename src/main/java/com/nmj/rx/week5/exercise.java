package com.nmj.rx.week5;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.internal.operators.OperatorFilter;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

/*

문제 요구 조건은 다음과 같습니다.
 
	1. 매초 주기적으로 DB의 현재 잭팟 누적 금액과 활성화 임계 금액을 읽는다.
	2. 매 30초마다 잭팟 누적금액을 전체 클라이언트에게 전송한다.
	3. 활성화/비활성화 상태가 변경되었을 때, 즉시 클라이언트에 알림을 전송한다.
		a. 누적금액 <= 임계금액 = 비활성화 상태
		b. 누적금액 > 임계금액  = 활성화 상태
	4. 초기 상태는 누적금액 0, 임계금액 0, 비활성화 상태로 가정한다.
 
누적금액을 읽는 함수는 다음과 같은 형태의 함수를 가정하시고, (아래 코드는 느낌만 보세요. 직접 비슷하게 작성하시면 됩니다.)

Void ReadAccMoney(Action<int> callback)
{
	Random r = new Random();
	callback(r.Next(100, 1000));
}
 
Void ReadLimitMoney(Action<int> callback)
{
	Random r = new Random();
	callback(r.Next(100, 1000));
}
 
클라이언트로 메시지 전송은 화면 출력으로 대체해 주세요.
 
같이 살펴본 함수들 외에 추가 함수가 필요할 수 있는데요, 아래 링크에서 찾아보고 적절한 것을 사용하시면 됩니다.
	http://reactivex.io/documentation.html

[해결]
  => 아래 2개의 Observable을 만들어서 merge 함.
  	1. 위에서 1번, 3번 작업하 Observable
  	2. 위에서 2번 작업하는 Observable

*/
public class exercise {
	
	
	public class JackpotObservable {
		private long mAccumulateMoney = 0;
		private long mCriticalMoney = 0;		
		private boolean prevActivate = false;
		
		public JackpotObservable() {
		}
		
		public Subscription subscribe(Action1 onNext) {
			Observable obs1 = Observable.interval(1, TimeUnit.SECONDS)
					.filter(v -> {
							readAccMoney(v1 -> mAccumulateMoney = v1);
							readLimitMoney(v1 -> mCriticalMoney = v1);
							return prevActivate != checkActiviate();
						})
					.map(v -> {
						prevActivate = !prevActivate;
						return prevActivate ? "비활성화 -> 활성화" : "활성화 -> 비활성화";
					});
			
			Observable obs2 = Observable.interval(30, TimeUnit.SECONDS)
					.map(v -> "잭팟 누적금액:" + mAccumulateMoney);
			
			return obs1.mergeWith(obs2).subscribe(onNext);
		}
		
		private boolean checkActiviate() {
			return mAccumulateMoney > mCriticalMoney;
		}
		
		private int getRandomValue() {
			// 100 ~ 1000 사이 값 추출.
			return (int)(Math.random() * (1000 - 100) + 100);
		}
		
		void readAccMoney(Action1<Integer> callback) {
			callback.call(getRandomValue());
		}
		
		void readLimitMoney(Action1<Integer> callback) {
			callback.call(getRandomValue());				
		}
		
	}
	
	
	public static void main(String... argc) throws IOException {
		exercise exer = new exercise();
		JackpotObservable jacpotObs = exer.new JackpotObservable();
		jacpotObs.subscribe(System.out::println);
		
		System.in.read();
	}
			
}
