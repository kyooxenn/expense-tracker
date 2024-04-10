package com.proj.expensetracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ExpenseTrackerApplication {

	public static void main(String[] args) {


		System.out.println("merge");
		SpringApplication.run(ExpenseTrackerApplication.class, args);
	}



}
