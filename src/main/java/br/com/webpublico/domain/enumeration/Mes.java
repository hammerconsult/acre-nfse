package br.com.webpublico.domain.enumeration;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Mes {

    JANEIRO("Janeiro", 1),
    FEVEREIRO("Fevereiro", 2),
    MARCO("Março", 3),
    ABRIL("Abril", 4),
    MAIO("Maio", 5),
    JUNHO("Junho", 6),
    JULHO("Julho", 7),
    AGOSTO("Agosto", 8),
    SETEMBRO("Setembro", 9),
    OUTUBRO("Outubro", 10),
    NOVEMBRO("Novembro", 11),
    DEZEMBRO("Dezembro", 12);

    private String descricao;
    private int numeroMes;

    private Mes(String descricao, int numeroMes) {
        this.descricao = descricao;
        this.numeroMes = numeroMes;
    }

    public String getDescricao() {
        return descricao;
    }

    public Integer getNumeroMes() {
        return numeroMes;
    }

    public void setNumeroMes(int numeroMes) {
        this.numeroMes = numeroMes;
    }

    @Override
    public String toString() {
        return descricao;
    }

    public static Mes getMesToInt(int mes) {
        switch (mes) {
            case 1:
                return Mes.JANEIRO;
            case 2:
                return Mes.FEVEREIRO;
            case 3:
                return Mes.MARCO;
            case 4:
                return Mes.ABRIL;
            case 5:
                return Mes.MAIO;
            case 6:
                return Mes.JUNHO;
            case 7:
                return Mes.JULHO;
            case 8:
                return Mes.AGOSTO;
            case 9:
                return Mes.SETEMBRO;
            case 10:
                return Mes.OUTUBRO;
            case 11:
                return Mes.NOVEMBRO;
            case 12:
                return Mes.DEZEMBRO;
            default:
                return null;
        }
    }
}
