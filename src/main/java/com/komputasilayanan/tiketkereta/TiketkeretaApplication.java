package com.komputasilayanan.tiketkereta;

import io.camunda.zeebe.spring.client.annotation.Deployment;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Deployment(resources = "classpath:pemesanantiket.bpmn")
public class TiketkeretaApplication {

	public static void main(String[] args) {
		SpringApplication.run(TiketkeretaApplication.class, args);
	}

}
