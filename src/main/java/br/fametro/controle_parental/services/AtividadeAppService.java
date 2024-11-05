package br.fametro.controle_parental.services;

import br.fametro.controle_parental.dtos.AppsAtualizarEstadoDTO;
import br.fametro.controle_parental.dtos.RequestAtividadeApp;
import br.fametro.controle_parental.entities.AtividadeApp;
import br.fametro.controle_parental.entities.UserFilho;
import br.fametro.controle_parental.repositories.AtividadeAppRepository;
import br.fametro.controle_parental.repositories.UserResponsavelRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AtividadeAppService {
    @Autowired
    private AtividadeAppRepository atividadeAppRepository;
    @Autowired
    private UserFilhoService userFilhoService;
    @Autowired
    private UserResponsavelRepository userResponsavelRepository;
    @Autowired
    private LogService logService;

    public List<AtividadeApp> listarAppsAtivos(Long idResp, Long idFilho) {
        var userFilho = userFilhoService.isResponsavel(idFilho, idResp);
        System.out.println("função listar apps ativos foi chamada");
        return atividadeAppRepository.findByUserFilhoAndAtivoTrue(userFilho);
    }

    public void salvar(RequestAtividadeApp data, Long idFilho) {
        var conta_filho = userFilhoService.findById(idFilho);
        String nome_atividade = data.nome().replace(".exe", "");
        if (atividadeAppRepository.existsByNomeAndUserFilho(nome_atividade, conta_filho)) {
            if (logService.existAppAndDataEHora(conta_filho, nome_atividade, data.hora_inicio())) {
                atividadeAppRepository.findByNomeAndUserFilhoId(nome_atividade, conta_filho.getId())
                        .ifPresent(app -> {
                            app.setHora_inicio(LocalDateTime.now());
                            atividadeAppRepository.save(app);
                        });
            } else {
                atividadeAppRepository.findByNomeAndUserFilhoId(nome_atividade, conta_filho.getId())
                        .ifPresent(app -> {
                            app.setHora_inicio(data.hora_inicio());
                            atividadeAppRepository.save(app);
                        });
            }
        } else if (logService.existAppAndDataEHora(conta_filho, nome_atividade, data.hora_inicio())) {
            boolean  a = logService.existAppAndDataEHora(conta_filho, nome_atividade, data.hora_inicio());
            AtividadeApp app = new AtividadeApp(data.nome(), LocalDateTime.now(), conta_filho);
            atividadeAppRepository.save(app);
        } else {
            var listar_a_salvar = new AtividadeApp(data, conta_filho);
            atividadeAppRepository.save(listar_a_salvar);
        }

    }

    // Método executado ao iniciar o backend
    @PostConstruct
    public void apagarAtividades() {
        atividadeAppRepository.deleteAll();
        System.out.println("Todas as atividades foram apagadas do banco de dados na inicialização.");
    }

    @Transactional
    public void atualizarEstado(Long idFIlho, AppsAtualizarEstadoDTO data) {
        System.out.println(data);
        var userfilho = userFilhoService.findById(idFIlho);
        var app_aberto = atividadeAppRepository.findByNomeAndUserFilhoId(data.nome().replace(".exe", ""), idFIlho)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        app_aberto.setAtivo(false);
        if (!app_aberto.isAtivo()) {
            logService.salvar(app_aberto, userfilho);
        }
        atividadeAppRepository.save(app_aberto);
        atividadeAppRepository.deleteAllByAtivoFalse();
    }
}
