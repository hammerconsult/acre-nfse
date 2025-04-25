package br.com.webpublico.domain.dto;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class NoticiaNfseDTO extends AbstractEntity implements RowMapper<NoticiaNfseDTO> {

    private Date dataNoticia;
    private String titulo;
    private String conteudo;
    private Long idDetentor;
    private String imagem;

    public Date getDataNoticia() {
        return dataNoticia;
    }

    public void setDataNoticia(Date dataNoticia) {
        this.dataNoticia = dataNoticia;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public Long getIdDetentor() {
        return idDetentor;
    }

    public void setIdDetentor(Long idDetentor) {
        this.idDetentor = idDetentor;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    @Override
    public NoticiaNfseDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        NoticiaNfseDTO dto = new NoticiaNfseDTO();
        dto.setId(resultSet.getLong("ID"));
        dto.setDataNoticia(resultSet.getDate("DATANOTICIA"));
        dto.setTitulo(resultSet.getString("TITULO"));
        dto.setConteudo(resultSet.getString("CONTEUDO"));
        dto.setIdDetentor(resultSet.getLong("DETENTORARQUIVOCOMPOSICAO_ID"));
        return dto;
    }
}
