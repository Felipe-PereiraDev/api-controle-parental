package br.fametro.controle_parental.entities.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TipoEvento {
    APP_UTILIZADO("A", "AppUtilizado");
    private String codigo;
    private String descricao;

    private TipoEvento(String codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    @JsonValue
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @JsonCreator
    public static TipoEvento ofValor(String codigo) {
        if (codigo.equals("A")) {
            return APP_UTILIZADO;
        } else {
            return null;
        }
    }
}
