package br.fametro.controle_parental.entities;

import br.fametro.controle_parental.dtos.RequestAtividadeApp;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_atividade_apps")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AtividadeApp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nome;

    @Column(nullable = false)
    private LocalDateTime hora_inicio;

    @Column(nullable = false)
    private boolean ativo;

    @ManyToOne()
    @JoinColumn(name = "id_user_filho", nullable = false)
    private UserFilho userFilho;

    public AtividadeApp(RequestAtividadeApp data, UserFilho userFilho) {
        System.out.println("nome da url exe:" + data.nome());
        this.nome = data.nome().replace(".exe", "");
        this.hora_inicio = data.hora_inicio();
        this.ativo = data.ativo();
        this.userFilho = userFilho;
    }

    public AtividadeApp(String nome, LocalDateTime data, UserFilho contaFilho) {
        this.nome = nome.replace(".exe", "");
        this.hora_inicio = data;
        this.ativo = true;
        this.userFilho = contaFilho;
    }
}
