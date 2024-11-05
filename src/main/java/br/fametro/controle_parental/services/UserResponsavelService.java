package br.fametro.controle_parental.services;


import br.fametro.controle_parental.dtos.CreateContaResponsavelDTO;
import br.fametro.controle_parental.dtos.ResponseUserFilhoDTO;
import br.fametro.controle_parental.entities.UserFilho;
import br.fametro.controle_parental.entities.UserResponsavel;
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
public class UserResponsavelService {
    @Autowired
    private UserResponsavelRepository responsavelRepository;

    @Autowired
    private UsuarioVerificadorRepository usuarioVerificadorRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private UserFilhoRepository filhoRepository;

    public void salvar(CreateContaResponsavelDTO data) {
        UserResponsavel conta = new UserResponsavel(data);
        conta.setSenha(encoder.encode(conta.getSenha()));
        conta.setSituacao(TipoSituacaoUsuario.PEDENTE);
        responsavelRepository.save(conta);

        UsuarioVerificador usuarioVerificador = new UsuarioVerificador();
        usuarioVerificador.setUserResponsavel(conta);
        usuarioVerificador.setUuid(UUID.randomUUID());
        usuarioVerificador.setDataExpiracao(Instant.now().plusMillis(900000));
        usuarioVerificadorRepository.save(usuarioVerificador);
        emailService.EnviarVerificacaoEmail(conta.getEmail(), usuarioVerificador.getUuid().toString());
    }

    public List<ResponseUserFilhoDTO> listarFilhos(Long id) {
        var conta_resp = findById(id);
        return conta_resp.getUserFilhos().stream()
                .map(ResponseUserFilhoDTO::new).toList();
    }

    public List<UserResponsavel> listar() {
        return responsavelRepository.findAll();
    }

    public UserResponsavel findById(Long id) {
        return responsavelRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public void addFilho(String email, Long idResp) {
        var conta_filho = filhoRepository.findByEmail(email).orElseThrow(()
                -> new ResponseStatusException((HttpStatus.NOT_FOUND)));
        var conta_pai = findById(idResp);
        // mandar uma verificação no email do filho
        UsuarioVerificador usuarioVerificador = new UsuarioVerificador();
        usuarioVerificador.setUserFilho(conta_filho);
        usuarioVerificador.setUuid(UUID.randomUUID());
        usuarioVerificador.setUserResponsavel(conta_pai);
        usuarioVerificador.setDataExpiracao(Instant.now().plusMillis(900000));
        usuarioVerificadorRepository.save(usuarioVerificador);
        emailService.EnviarVerificacaoEmailJuntarFamilia(
                email,
                usuarioVerificador.getUuid().toString(),
                "Você deseja se juntar a família de " + conta_pai.getNome() + " ?\n clique no link para confirmar:");
    }

    public UserResponsavel findByEmail(String email){
        return responsavelRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
