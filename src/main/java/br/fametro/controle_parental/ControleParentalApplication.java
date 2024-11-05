package br.fametro.controle_parental;

import br.fametro.controle_parental.repositories.UserFilhoRepository;
import br.fametro.controle_parental.repositories.UserResponsavelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ControleParentalApplication {
	@Autowired
	private UserResponsavelRepository userResponsavelRepository;
	@Autowired
	private UserFilhoRepository userFilhoRepository;
	public static void main(String[] args) {
		SpringApplication.run(ControleParentalApplication.class, args);

	}





}
