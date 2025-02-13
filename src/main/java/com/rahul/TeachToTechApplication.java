package com.rahul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EntityScan("com.rahul.model")  // ✅ Ensures Hibernate scans the right package
//@EnableJpaRepositories("com.rahul.repository") // ✅ Ensures repositories are found
//@ComponentScan(basePackages = "com.rahul")
public class TeachToTechApplication {

	public static void main(String[] args) {
		SpringApplication.run(TeachToTechApplication.class, args);
	}

}
