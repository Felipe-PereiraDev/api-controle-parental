package br.fametro.controle_parental.repositories;

import br.fametro.controle_parental.entities.UrlVisitada;
import br.fametro.controle_parental.entities.UserFilho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UrlVisitadaRepository extends JpaRepository<UrlVisitada, Long> {

    @Query("SELECT u FROM UrlVisitada u WHERE u.userFilho.id = :idFilho AND u.dataVisita >= :inicio AND u.dataVisita < :fim")
    List<UrlVisitada> findByUserFilhoAndDataVisita(
            @Param("idFilho") Long userFilho,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim);
}
