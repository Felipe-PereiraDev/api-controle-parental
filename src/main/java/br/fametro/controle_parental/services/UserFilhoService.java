package br.fametro.controle_parental.services;

import br.fametro.controle_parental.dtos.CreateContaFilhoDTO;
import br.fametro.controle_parental.entities.UrlBloqueada;
import br.fametro.controle_parental.entities.UserFilho;
import br.fametro.controle_parental.entities.UsuarioVerificador;
import br.fametro.controle_parental.entities.enums.TipoSituacaoUsuario;
import br.fametro.controle_parental.repositories.UserFilhoRepository;
import br.fametro.controle_parental.repositories.UserResponsavelRepository;
import br.fametro.controle_parental.repositories.UsuarioVerificadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class UserFilhoService {
    @Autowired
    private UserFilhoRepository filhoRepository;
    @Autowired
    private UserResponsavelRepository responsavelRepository;
    @Autowired
    private UserResponsavelService userResponsavelService;
    @Autowired
    private UsuarioVerificadorRepository usuarioVerificadorRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private EmailService emailService;


    public UserFilho salvar(CreateContaFilhoDTO data) {
        UserFilho conta_filho = new UserFilho(data);
        conta_filho.setSituacao(TipoSituacaoUsuario.PEDENTE);
        conta_filho.setSenha(passwordEncoder.encode(conta_filho.getSenha()));
        var conta_salva = filhoRepository.save(conta_filho);


        // usuario verificador
        UsuarioVerificador usuarioVerificador = new UsuarioVerificador();
        usuarioVerificador.setUserFilho(conta_filho);
        usuarioVerificador.setUuid(UUID.randomUUID());
        usuarioVerificador.setDataExpiracao(Instant.now().plusMillis(900000));
        usuarioVerificadorRepository.save(usuarioVerificador);
        emailService.EnviarVerificacaoEmailFilho(
                conta_filho.getEmail(),
                usuarioVerificador.getUuid().toString(),
                "Clique no link para verificar seu email:");
        return conta_salva;
    }

    public List<UrlBloqueada> listaUrlsBloqueadas(Long idFilho, Long idResp) {
        var conta_filho = isResponsavel(idFilho, idResp);
        return conta_filho.getUrlBloqueadas();
    }

    public UserFilho findById(Long id) {
        return filhoRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Boolean existsById (Long idFilho) {
        return filhoRepository.existsById(idFilho);
    }

    public UserFilho isResponsavel(Long idFilho, Long idResponsavel) {
        long idResp = userResponsavelService.findById(idResponsavel).getId();
        var userFilho = findById(idFilho);
        var id = userFilho.getUserResponsavel().getId();
        if (id  != idResp) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        return userFilho;
    }
}
