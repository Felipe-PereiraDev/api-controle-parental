package br.fametro.controle_parental.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_url_bloqueadas")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UrlBloqueada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String url;

    private boolean desbloquear;

    @ManyToOne
    @JoinColumn(name = "id_user_filho", nullable = false)
    private UserFilho userFilho;


    public UrlBloqueada(String url, UserFilho userFilho) {
        this.url  = url;
        this.userFilho = userFilho;
    }


}
