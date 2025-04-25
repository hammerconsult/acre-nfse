package br.com.webpublico.repository;

import br.com.webpublico.domain.dto.ServicoNfseDTO;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ServicoJDBCRepository extends AbstractJDBCRepository<ServicoNfseDTO> {

    @Override
    public String getSelect() {
        return " select " +
                "    obj.id as id, " +
                "    obj.codigo as codigo, " +
                "    obj.nome as nome, " +
                "    obj.aliquotaisshomologado as aliquotaisshomologado, " +
                "    obj.construcaocivil as construcaocivil, " +
                "    obj.permiterecolhimentofora as permiterecolhimentofora, " +
                "    obj.permitededucao as permitededucao, " +
                "    obj.percentualdeducao as percentualdeducao, " +
                "    obj.exclusivosimplesnacional as exclusivosimplesnacional, " +
                "    obj.vetadolc1162003 as vetadolc1162003," +
                "    obj.anexolei1232006_id as anexolei1232006_id," +
                "    obj.permitealteraranexolei1232006 as permitealteraranexolei1232006 ";
    }

    @Override
    public String getFrom() {
        return "   from servico obj ";
    }

    @Override
    public RowMapper<ServicoNfseDTO> newRowMapper() {
        return new ServicoNfseDTO();
    }

    @Override
    public ServicoNfseDTO insert(ServicoNfseDTO dto) {
        return dto;
    }

    @Override
    public ServicoNfseDTO update(ServicoNfseDTO dto) {
        return dto;
    }

    @Override
    public void remove(ServicoNfseDTO dto) {
    }

    public List<ServicoNfseDTO> findByIdCadastro(Long idCadastro) {
        return jdbcTemplate.query(getSelect() + getFrom() +
                        "  inner join cadastroeconomico_servico ce_s on ce_s.servico_id = obj.id " +
                        " where ce_s.cadastroeconomico_id = ? ", new Object[]{idCadastro},
                new ServicoNfseDTO());
    }
}
