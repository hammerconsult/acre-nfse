package br.com.webpublico.domain.dto;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class MensagemContribuinteUsuarioDocumentoNfseDTO implements RowMapper<MensagemContribuinteUsuarioDocumentoNfseDTO>, BatchPreparedStatementSetter {

    private Long id;
    private MensagemContribuinteDocumentoNfseDTO documento;
    private Long idMensagemContribuinteUsuario;
    private ArquivoNfseDTO arquivo;

    //para upload
    private String file;
    private String fileName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MensagemContribuinteDocumentoNfseDTO getDocumento() {
        return documento;
    }

    public void setDocumento(MensagemContribuinteDocumentoNfseDTO documento) {
        this.documento = documento;
    }

    public Long getIdMensagemContribuinteUsuario() {
        return idMensagemContribuinteUsuario;
    }

    public void setIdMensagemContribuinteUsuario(Long idMensagemContribuinteUsuario) {
        this.idMensagemContribuinteUsuario = idMensagemContribuinteUsuario;
    }

    public ArquivoNfseDTO getArquivo() {
        return arquivo;
    }

    public void setArquivo(ArquivoNfseDTO arquivo) {
        this.arquivo = arquivo;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public MensagemContribuinteUsuarioDocumentoNfseDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        MensagemContribuinteUsuarioDocumentoNfseDTO dto = new MensagemContribuinteUsuarioDocumentoNfseDTO();
        if (resultSet.getLong("id") != 0) {
            dto.setId(resultSet.getLong("id"));
        }
        dto.setDocumento(new MensagemContribuinteDocumentoNfseDTO());
        dto.getDocumento().setId(resultSet.getLong("documento_id"));
        dto.getDocumento().setDescricaoDocumento(resultSet.getString("documento_descricao"));
        dto.getDocumento().setObrigatorio(resultSet.getBoolean("documento_obrigatorio"));
        dto.setIdMensagemContribuinteUsuario(resultSet.getLong("mensagemcontribuinteusuario_id"));
        if (resultSet.getLong("arquivo_id") != 0) {
            dto.setArquivo(new ArquivoNfseDTO());
            dto.getArquivo().setId(resultSet.getLong("arquivo_id"));
        }
        return dto;
    }

    @Override
    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
        preparedStatement.setLong(1, id);
        preparedStatement.setLong(2, idMensagemContribuinteUsuario);
        preparedStatement.setLong(3, documento.getId());
        if (arquivo != null) {
            preparedStatement.setLong(4, arquivo.getId());
        } else {
            preparedStatement.setNull(4, Types.NULL);
        }
    }

    @Override
    public int getBatchSize() {
        return 1;
    }
}
