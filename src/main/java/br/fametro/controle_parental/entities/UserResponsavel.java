package br.fametro.controle_parental.entities;

import br.fametro.controle_parental.dtos.CreateContaResponsavelDTO;
import br.fametro.controle_parental.dtos.LoginRequestResponsavel;
import br.fametro.controle_parental.entities.enums.TipoSituacaoUsuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Entity
@Table(name = "tb_user_responsavel")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponsavel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String senha;

    @OneToMany(mappedBy = "userResponsavel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserFilho> userFilhos;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoSituacaoUsuario situacao;

    @OneToMany(mappedBy = "userResponsavel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BloquearUrl> bloquearUrl;

    public UserResponsavel(CreateContaResponsavelDTO data) {
        this.nome = data.nome();
        this.email = data.email();
        this.senha = data.senha();
    }

    public UserResponsavel(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.situacao = TipoSituacaoUsuario.ATIVO;

    }

    public boolean isLoginCorreto(LoginRequestResponsavel loginRequest, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(loginRequest.senha(), this.senha);
    }


}
