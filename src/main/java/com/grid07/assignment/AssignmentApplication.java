package com.grid07.assignment;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;



//@EnableScheduling helps springboot to run thr background jobs,crontasks scheduling methods
@SpringBootApplication
@EnableScheduling
public class AssignmentApplication {

	public static void main(String[] args) {
	    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		SpringApplication.run(AssignmentApplication.class, args);


		
	}

}
