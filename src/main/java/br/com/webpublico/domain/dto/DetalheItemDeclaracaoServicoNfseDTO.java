package br.com.webpublico.domain.dto;

import br.com.webpublico.domain.dto.DeclaracaoPrestacaoServicoNfseDTO;
import br.com.webpublico.domain.dto.NfseDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DetalheItemDeclaracaoServicoNfseDTO implements NfseDTO {

    private Long id;
    private BigDecimal quantidade;
    private BigDecimal valorServico;
    private String descricao;


    public DetalheItemDeclaracaoServicoNfseDTO() {
    }

    @JsonIgnore
    public static br.com.webpublico.domain.dto.DetalheItemDeclaracaoServicoNfseDTO copy(br.com.webpublico.domain.dto.DetalheItemDeclaracaoServicoNfseDTO itemDeclaracaoServico, DeclaracaoPrestacaoServicoNfseDTO declaracaoPrestacaoServico) {
        br.com.webpublico.domain.dto.DetalheItemDeclaracaoServicoNfseDTO item = new br.com.webpublico.domain.dto.DetalheItemDeclaracaoServicoNfseDTO();
        item.descricao = itemDeclaracaoServico.getDescricao();
        item.quantidade = itemDeclaracaoServico.getQuantidade();
        item.setValorServico(itemDeclaracaoServico.getValorServico());
        return item;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public BigDecimal getQuantidade() {
        return quantidade == null || quantidade.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ONE : quantidade;
    }

    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getValorServico() {
        return valorServico;
    }

    public void setValorServico(BigDecimal valorServico) {
        this.valorServico = valorServico;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }


    @Override
    public String toString() {
        return "ItemDeclaracaoServicoNfseDTO{" +
                "id=" + id +
                ", quantidade=" + quantidade +
                ", valorServico=" + valorServico +
                ", descricao='" + descricao + '\'' +
                '}';
    }
}
