package br.com.webpublico.web.rest.dto;

import java.util.List;

public class ExportarDTO {

    List<Coluna> colunas;

    List<Object> linhas;

    public List<Coluna> getColunas() {
        return colunas;
    }

    public void setColunas(List<Coluna> colunas) {
        this.colunas = colunas;
    }

    public List<Object> getLinhas() {
        return linhas;
    }

    public void setLinhas(List<Object> linhas) {
        this.linhas = linhas;
    }

    public static class Coluna {
        String descricao;
        String valor;

        public String getDescricao() {
            return descricao;
        }

        public void setDescricao(String descricao) {
            this.descricao = descricao;
        }

        public String getValor() {
            return valor;
        }

        public void setValor(String valor) {
            this.valor = valor;
        }
    }

}


