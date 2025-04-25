package br.com.webpublico.domain.dto;

import br.com.webpublico.DateUtils;
import br.com.webpublico.domain.dto.enums.GravidadeNfseDTO;
import br.com.webpublico.domain.dto.enums.TipoNotificacaoNfseDTO;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class NotificacaoNfseDTO extends AbstractEntity implements BatchPreparedStatementSetter {
    private Long id;
    private String titulo;
    private String descricao;
    private String link;
    private GravidadeNfseDTO gravidade;
    private TipoNotificacaoNfseDTO tipoNotificacao;
    private Date criadoEm;

    public NotificacaoNfseDTO() {
        this.criadoEm = new Date();
    }

    public NotificacaoNfseDTO(String titulo, String descricao, String link,
                              GravidadeNfseDTO gravidade, TipoNotificacaoNfseDTO tipoNotificacao) {
        this();
        this.titulo = titulo;
        this.descricao = descricao;
        this.link = link;
        this.gravidade = gravidade;
        this.tipoNotificacao = tipoNotificacao;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public GravidadeNfseDTO getGravidade() {
        return gravidade;
    }

    public void setGravidade(GravidadeNfseDTO gravidade) {
        this.gravidade = gravidade;
    }

    public TipoNotificacaoNfseDTO getTipoNotificacao() {
        return tipoNotificacao;
    }

    public void setTipoNotificacao(TipoNotificacaoNfseDTO tipoNotificacao) {
        this.tipoNotificacao = tipoNotificacao;
    }

    public Date getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(Date criadoEm) {
        this.criadoEm = criadoEm;
    }

    @Override
    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
        AtomicInteger parameterIndex = new AtomicInteger(0);
        preparedStatement.setLong(parameterIndex.addAndGet(1), id);
        preparedStatement.setString(parameterIndex.addAndGet(1), titulo);
        preparedStatement.setString(parameterIndex.addAndGet(1), descricao);
        preparedStatement.setString(parameterIndex.addAndGet(1), link);
        preparedStatement.setString(parameterIndex.addAndGet(1), gravidade.name());
        preparedStatement.setBoolean(parameterIndex.addAndGet(1), Boolean.FALSE);
        preparedStatement.setString(parameterIndex.addAndGet(1), tipoNotificacao.name());
        preparedStatement.setDate(parameterIndex.addAndGet(1), DateUtils.toSQLDate(criadoEm));
    }

    @Override
    public int getBatchSize() {
        return 1;
    }
}
