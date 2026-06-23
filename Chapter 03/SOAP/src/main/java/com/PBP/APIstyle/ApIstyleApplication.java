package com.PBP.APIstyle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApIstyleApplication {

	public static void main(String[] args) {

		SpringApplication.run(ApIstyleApplication.class, args);
		System.out.println("✅ SOAP service running at http://localhost:8080/ws/profiles.wsdl");
	}

}
