package com.sy.banking;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.sy.banking.auth.mapper")
@MapperScan("com.sy.banking.account.mapper")
@MapperScan("com.sy.banking.transfer.mapper")
@EnableScheduling //이자 스케줄러
public class BankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankingApplication.class, args);
		System.out.println("\u001B[0m" + "ㅇㅋ");
	}

}