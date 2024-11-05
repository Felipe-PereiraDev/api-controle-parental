package br.fametro.controle_parental.controller;

import br.fametro.controle_parental.services.UsuarioVerificadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/verify")
public class EmailVerificacaoController {
    @Autowired
    private UsuarioVerificadorService usuarioVerificadorService;

    @GetMapping
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String token) {
        boolean isVerified = usuarioVerificadorService.verifyUserByToken(token);

        if (isVerified) {
            return ResponseEntity.ok("Conta verificada com sucesso!");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token inválido ou expirado.");
        }
    }

    @GetMapping("/f")
    public ResponseEntity<String> verifyEmailFilho(@RequestParam("token") String token) {
        boolean isVerified = usuarioVerificadorService.verifyUserByTokenFilho(token);
        if (isVerified) {
            return ResponseEntity.ok("Conta verificada com sucesso!");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token inválido ou expirado.");
        }
    }

    @GetMapping("/f/add-user")
    public ResponseEntity<String> verificarEmailJuntarAFamilia(@RequestParam("token") String token) {
        boolean isVerified = usuarioVerificadorService.verifyUserByTokenJuntarFamilia(token);
        if (isVerified) {
            return ResponseEntity.ok("Você juntou a família");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token inválido ou expirado.");
        }
    }
}
