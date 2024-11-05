package br.fametro.controle_parental.entities;

import br.fametro.controle_parental.dtos.CreateContaFilhoDTO;
import br.fametro.controle_parental.dtos.LoginRequestFilho;
import br.fametro.controle_parental.entities.enums.TipoSituacaoUsuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Entity
@Table(name = "tb_user_filho")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserFilho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String senha;

    @ManyToOne
    @JoinColumn(name = "id_responsavel")
    private UserResponsavel userResponsavel;

    @OneToMany(mappedBy = "userFilho", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UrlVisitada> urlVisitadas;

    @OneToMany(mappedBy = "userFilho", cascade = CascadeType.ALL, orphanRemoval = true)
    @Column(unique = true)
    private List<UrlBloqueada> urlBloqueadas;

    @OneToMany(mappedBy = "userFilho", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AtividadeApp> atividadeApps;

    @OneToMany(mappedBy = "userFilho", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BloquearUrl> urlBloqueada;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoSituacaoUsuario situacao;

    @OneToMany(mappedBy = "userFilho", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Log> logs;

    public UserFilho(CreateContaFilhoDTO data) {
        this.nome = data.nome();
        this.email = data.email();
        this.senha = data.senha();
    }

    public UserFilho(UserResponsavel userResponsavel, String nome, String emailResponsavel, String senha) {
        this.userResponsavel = userResponsavel;
        this.nome = nome;
        this.email = emailResponsavel;
        this.senha = senha;
    }

    public boolean existsUrl(String url){
        return this.getUrlBloqueadas().stream().anyMatch(urlBloqueada -> urlBloqueada.getUrl().equalsIgnoreCase(url));
    }

    public boolean isLoginCorreto(LoginRequestFilho loginRequest, PasswordEncoder passwordEncoder) {
        // Adicione logs para depuração
        System.out.println("Senha armazenada (hash): " + this.senha);
        System.out.println("Senha recebida: " + loginRequest.senha());

        // Verifique se a senha fornecida corresponde ao hash armazenado
        boolean matches = passwordEncoder.matches(loginRequest.senha(), this.senha);
        System.out.println("Senha correta? " + matches);

        return matches;
    }

    public List<String> getSitesBloqueados() {
        return this.urlBloqueadas.stream().map(
              UrlBloqueada::getUrl
        ).toList();
    }

    @Override
    public String toString() {
        return "UserFilho{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", senha='" + senha + '\'' +
                ", userResponsavel=" + userResponsavel +
                ", urlVisitadas=" + urlVisitadas +
                ", urlBloqueadas=" + urlBloqueadas +
                ", situacao=" + situacao +
                '}';
    }
}
