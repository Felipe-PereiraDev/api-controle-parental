package br.fametro.controle_parental.controller;

import br.fametro.controle_parental.dtos.LoginRequestFilho;
import br.fametro.controle_parental.dtos.LoginRequestResponsavel;
import br.fametro.controle_parental.dtos.LoginResponse;
import br.fametro.controle_parental.entities.enums.TipoSituacaoUsuario;
import br.fametro.controle_parental.repositories.UserFilhoRepository;
import br.fametro.controle_parental.repositories.UserResponsavelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

@RestController
@RequestMapping("/auth")
public class TokenController {
    @Autowired
    private JwtEncoder jwtEncoder;
    @Autowired
    private UserResponsavelRepository responsavelRepository;
    @Autowired
    private UserFilhoRepository filhoRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private JwtDecoder jwtDecoder;


    @PostMapping("/login/r")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequestResponsavel loginRequest) {
        System.out.println(loginRequest.email() + "  "  + loginRequest.senha());
        var user = responsavelRepository.findByEmail(loginRequest.email()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );

        if (!user.getSituacao().equals(TipoSituacaoUsuario.ATIVO)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        if (!user.isLoginCorreto(loginRequest, passwordEncoder)) {
            throw new BadCredentialsException("senha ou usuário inválido!");
        }

        var now = Instant.now();
        var expiresIn = 3600L;

        var claims = JwtClaimsSet.builder()
                .issuer("api-controle-parental")
                .subject(user.getId().toString())
                .claim("role", "userResp")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresIn))
                .build();

        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        return ResponseEntity.ok(new LoginResponse(jwtValue, expiresIn));
    }

    @PostMapping("/login/f")
    public ResponseEntity<LoginResponse> loginFilho(@RequestBody LoginRequestFilho loginRequest) {
        var user = filhoRepository.findByEmail(loginRequest.email()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );

        if (!user.getSituacao().equals(TipoSituacaoUsuario.ATIVO)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }


        if (!user.isLoginCorreto(loginRequest, passwordEncoder)) {
            throw new BadCredentialsException("senha ou usuário inválido!");
        }

        var now = Instant.now();
        var expiresIn = 10000000L;

        var claims = JwtClaimsSet.builder()
                .issuer("api-controle-parental")
                .subject(user.getId().toString())
                .claim("idResp", user.getUserResponsavel().getId())
                .claim("role", "userFilho")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresIn))
                .build();
        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        System.out.println(jwtValue);
        return ResponseEntity.ok(new LoginResponse(jwtValue, expiresIn));
    }

}
