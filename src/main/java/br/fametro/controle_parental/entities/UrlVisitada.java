package br.fametro.controle_parental.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_url_visitadas")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UrlVisitada {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String url;
    @Column(nullable = false)
    private LocalDateTime dataVisita;
    @Column(length = 200)
    private String conteudo;

    @ManyToOne
    @JoinColumn(name = "id_user_filho", nullable = false)
    private UserFilho userFilho;

    public UrlVisitada(String url, LocalDateTime dataVisita, String conteudo, UserFilho userFilho) {
        this.url = url;
        this.dataVisita = dataVisita;
        this.conteudo = conteudo;
        this.userFilho = userFilho;
    }
}
