package br.fametro.controle_parental.services;

import br.fametro.controle_parental.entities.AtividadeApp;
import br.fametro.controle_parental.entities.Log;
import br.fametro.controle_parental.entities.UserFilho;
import br.fametro.controle_parental.entities.enums.TipoEvento;
import br.fametro.controle_parental.repositories.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class LogService {
    @Autowired
    private LogRepository logRepository;

    public void salvar(AtividadeApp app, UserFilho userFilho) {
        LocalDateTime agora = LocalDateTime.now();
        Duration duracao = Duration.between(app.getHora_inicio(), agora);
        long minutos = duracao.toMinutes();
        if (minutos != 0) {
            LocalTime horaInicio = app.getHora_inicio().toLocalTime();
            LocalDate data = app.getHora_inicio().toLocalDate();
            System.out.println("minutos: " + minutos);
            Log log = new Log(data, TipoEvento.APP_UTILIZADO, app.getNome(), minutos, userFilho, horaInicio);
            logRepository.save(log);
        }
    }

    public boolean existAppAndDataEHora(UserFilho contaFilho, String nomeAtividade, LocalDateTime dataEHora) {
        LocalTime horaInicio = dataEHora.toLocalTime();
        LocalDate data = dataEHora.toLocalDate();
        System.out.println("função existsByUserFilhoAndDetalhesAndDataAcessoAndHora chamada");
        return logRepository.
                existsByUserFilhoAndDetalhesAndDataAcessoAndHora(contaFilho, nomeAtividade, data, horaInicio);
    }
}
