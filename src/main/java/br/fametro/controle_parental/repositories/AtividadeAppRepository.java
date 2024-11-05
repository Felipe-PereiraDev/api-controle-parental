package br.fametro.controle_parental.repositories;

import br.fametro.controle_parental.entities.AtividadeApp;
import br.fametro.controle_parental.entities.UserFilho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AtividadeAppRepository extends JpaRepository<AtividadeApp, Long> {
    List<AtividadeApp> findByUserFilhoAndAtivoTrue(UserFilho userFilho);
    Optional<AtividadeApp> findByNomeAndUserFilhoId(String nome, Long id_user_filho);
    void deleteAllByAtivoFalse();
    boolean existsByNomeAndUserFilho(String nome, UserFilho userFilho);
}
