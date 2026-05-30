package com.portal.job;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableConfigServer
public class JobPortalConfigServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(JobPortalConfigServerApplication.class, args);
	}

}
