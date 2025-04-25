package br.com.webpublico.domain.dto;

import br.com.webpublico.domain.dto.enums.StatusProcessamentoDfeAdnDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class RetornoDfeAdnDTO extends AbstractEntity implements RowMapper<RetornoDfeAdnDTO> {

    private Date dataHoraProcessamento;
    private String nsuRecebimento;
    private String chaveAcesso;
    private StatusProcessamentoDfeAdnDTO statusProcessamento;

    public Date getDataHoraProcessamento() {
        return dataHoraProcessamento;
    }

    public void setDataHoraProcessamento(Date dataHoraProcessamento) {
        this.dataHoraProcessamento = dataHoraProcessamento;
    }

    public String getNsuRecebimento() {
        return nsuRecebimento;
    }

    public void setNsuRecebimento(String nsuRecebimento) {
        this.nsuRecebimento = nsuRecebimento;
    }

    public String getChaveAcesso() {
        return chaveAcesso;
    }

    public void setChaveAcesso(String chaveAcesso) {
        this.chaveAcesso = chaveAcesso;
    }

    public StatusProcessamentoDfeAdnDTO getStatusProcessamento() {
        return statusProcessamento;
    }

    public void setStatusProcessamento(StatusProcessamentoDfeAdnDTO statusProcessamento) {
        this.statusProcessamento = statusProcessamento;
    }

    @Override
    public RetornoDfeAdnDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        RetornoDfeAdnDTO dto = new RetornoDfeAdnDTO();
        dto.setId(resultSet.getLong("id"));
        dto.setDataHoraProcessamento(resultSet.getDate("datahoraprocessamento"));
        dto.setNsuRecebimento(resultSet.getString("nsurecebimento"));
        dto.setChaveAcesso(resultSet.getString("chaveacesso"));
        dto.setStatusProcessamento(StatusProcessamentoDfeAdnDTO.valueOf(resultSet.getString("statusprocessamento")));
        return dto;
    }
}
