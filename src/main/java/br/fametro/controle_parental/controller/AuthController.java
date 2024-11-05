package br.fametro.controle_parental.controller;

import br.fametro.controle_parental.dtos.CreateContaFilhoDTO;
import br.fametro.controle_parental.dtos.CreateContaResponsavelDTO;
import br.fametro.controle_parental.services.UserFilhoService;
import br.fametro.controle_parental.services.UserResponsavelService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserResponsavelService userResponsavelService;
    @Autowired
    private UserFilhoService userFilhoService;

    @PostMapping("/user/registrar")
    @Transactional
    public ResponseEntity<?> adicionarNovoUserResponsavel(@RequestBody CreateContaResponsavelDTO data) {
        userResponsavelService.salvar(data);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/user/f/registrar")
    public ResponseEntity<?> adicionarNovoUserFilho(@RequestBody CreateContaFilhoDTO data) {
        userFilhoService.salvar(data);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
