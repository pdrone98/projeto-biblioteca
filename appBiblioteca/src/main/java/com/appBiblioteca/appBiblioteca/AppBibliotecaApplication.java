package com.appBiblioteca.appBiblioteca;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.sql.SQLOutput;

@SpringBootApplication
@ComponentScan(basePackages = {"com.appBiblioteca.appBiblioteca"} )
public class AppBibliotecaApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppBibliotecaApplication.class, args);
	}

}
