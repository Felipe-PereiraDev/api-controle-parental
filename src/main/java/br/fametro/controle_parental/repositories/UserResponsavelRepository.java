package br.fametro.controle_parental.repositories;

import br.fametro.controle_parental.entities.UserResponsavel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserResponsavelRepository extends JpaRepository<UserResponsavel, Long> {

    Optional<UserResponsavel> findByEmail(String email);
}
