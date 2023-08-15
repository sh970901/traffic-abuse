package com.totoro.AntiAbuse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AntiAbuseApplication {
	//TODO 컨트롤러 리팩토링, 큐를 이용한 비동기 처리 및 입력값 화면 개발, Couchbase 인덱스 및 Limit 도큐먼트 시간 설정
	public static void main(String[] args) {
		SpringApplication.run(AntiAbuseApplication.class, args);
	}

}
