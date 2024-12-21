package com.ltms.backend;

import com.ltms.backend.rol.Rol;
import com.ltms.backend.rol.RolRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")

public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	@Bean
	public CommandLineRunner runner(RolRepository rolRepository){
		return args -> {
			if (rolRepository.findByName("ROLE_USER").isEmpty()){
				rolRepository.save(
						Rol.builder().name("ROLE_USER").build()
				);
			}
			if (rolRepository.findByName("ROLE_ADMIN").isEmpty()){
				rolRepository.save(
						Rol.builder().name("ROLE_ADMIN").build()
				);
			}
		};
	}
}
