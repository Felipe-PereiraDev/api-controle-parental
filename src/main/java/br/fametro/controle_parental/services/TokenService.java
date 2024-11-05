package br.fametro.controle_parental.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;

@Service
public class TokenService {
    @Autowired
    private JwtDecoder jwtDecoder;
    @Autowired
    private UserFilhoService filhoService;
    public Long getIdRespToken(String authHeader) {
        String token = authHeader.replace("Bearer ", "");

        Jwt jwt = jwtDecoder.decode(token);
        return Long.parseLong(jwt.getSubject());
    }

    public Long getIdFilhoToken(String authHeader) {
        String token = authHeader.replace("Bearer ", "");

        Jwt jwt = jwtDecoder.decode(token);
        System.out.println(jwt.toString());
        System.out.println("Id token: " +jwt.getId());
        System.out.println("Subjct: " +jwt.getSubject());
        return Long.parseLong(jwt.getSubject());
    }

}
