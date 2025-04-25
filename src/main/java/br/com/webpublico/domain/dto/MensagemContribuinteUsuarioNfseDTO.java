package br.com.webpublico.domain.dto;

import br.com.webpublico.domain.dto.enums.TipoMensagemContribuinteNfseDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class MensagemContribuinteUsuarioNfseDTO extends AbstractEntity implements RowMapper<MensagemContribuinteUsuarioNfseDTO> {

    private MensagemContribuinteNfseDTO mensagem;
    private Long id;
    private UserNfseDTO lidaPor;
    private Boolean lida;
    private Date dataLeitura;
    private String resposta;
    private List<MensagemContribuinteUsuarioDocumentoNfseDTO> documentos;

    public MensagemContribuinteNfseDTO getMensagem() {
        return mensagem;
    }

    public void setMensagem(MensagemContribuinteNfseDTO mensagem) {
        this.mensagem = mensagem;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public UserNfseDTO getLidaPor() {
        return lidaPor;
    }

    public void setLidaPor(UserNfseDTO lidaPor) {
        this.lidaPor = lidaPor;
    }

    public Boolean getLida() {
        return lida;
    }

    public void setLida(Boolean lida) {
        this.lida = lida;
    }

    public Date getDataLeitura() {
        return dataLeitura;
    }

    public void setDataLeitura(Date dataLeitura) {
        this.dataLeitura = dataLeitura;
    }

    public String getResposta() {
        return resposta;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
    }

    public List<MensagemContribuinteUsuarioDocumentoNfseDTO> getDocumentos() {
        return documentos;
    }

    public void setDocumentos(List<MensagemContribuinteUsuarioDocumentoNfseDTO> documentos) {
        this.documentos = documentos;
    }

    @Override
    public MensagemContribuinteUsuarioNfseDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        MensagemContribuinteUsuarioNfseDTO dto = new MensagemContribuinteUsuarioNfseDTO();
        dto.setMensagem(new MensagemContribuinteNfseDTO());
        dto.getMensagem().setId(resultSet.getLong("mensagem_id"));
        dto.getMensagem().setTipo(TipoMensagemContribuinteNfseDTO.valueOf(resultSet.getString("mensagem_tipo")));
        dto.getMensagem().setEmitidaEm(resultSet.getDate("mensagem_emitidaem"));
        dto.getMensagem().setTitulo(resultSet.getString("mensagem_titulo"));
        dto.getMensagem().setConteudo(resultSet.getString("mensagem_conteudo"));
        if (resultSet.getLong("mensagem_detentor_id") != 0) {
            dto.getMensagem().setDetentorArquivoComposicao(new DetentorArquivoComposicaoNfseDTO());
            dto.getMensagem().getDetentorArquivoComposicao().setId(resultSet.getLong("mensagem_detentor_id"));
        }
        dto.setId(resultSet.getLong("id"));
        dto.setLida(resultSet.getBoolean("lida"));
        dto.setDataLeitura(resultSet.getDate("dataleitura"));
        dto.setResposta(resultSet.getString("resposta"));
        return dto;
    }
}
