package br.fametro.controle_parental.repositories;

import br.fametro.controle_parental.entities.Log;
import br.fametro.controle_parental.entities.UserFilho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface LogRepository extends JpaRepository<Log, Long> {

    @Query("SELECT l FROM Log l WHERE l.userFilho.id = :userId AND l.dataAcesso BETWEEN :startDate AND :endDate")
    List<Log> findLogsByUserIdAndDateRange(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    boolean existsByUserFilhoAndDetalhesAndDataAcessoAndHora(UserFilho userFilho, String detalhes, LocalDate dataAcesso, LocalTime hora);

}
