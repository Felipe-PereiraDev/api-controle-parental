package br.fametro.controle_parental.repositories;

import br.fametro.controle_parental.entities.UserFilho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserFilhoRepository extends JpaRepository<UserFilho, Long> {
    Optional<UserFilho> findByEmail(String email);
}
