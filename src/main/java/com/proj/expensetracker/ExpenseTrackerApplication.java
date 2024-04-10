package com.proj.expensetracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ExpenseTrackerApplication {

	public static void main(String[] args) {


		System.out.println("merge");
		System.out.println("mikee");
		System.out.println("mikee2");
		System.out.println("hello");

		System.out.println("hi");
		System.out.println("hi 2");
		SpringApplication.run(ExpenseTrackerApplication.class, args);
	}



}
