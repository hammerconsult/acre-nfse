package br.com.webpublico.repository;

import br.com.webpublico.domain.dto.GraficoQuantitativoNotaNacionalDTO;
import br.com.webpublico.domain.dto.IntegracaoNotaNacionalDTO;
import br.com.webpublico.domain.dto.MensagemDfeAdnDTO;
import br.com.webpublico.domain.dto.RetornoDfeAdnDTO;
import br.com.webpublico.domain.dto.consultanfse.ParametroQuery;
import br.com.webpublico.domain.dto.consultanfse.QueryUtil;
import br.com.webpublico.domain.dto.enums.StatusProcessamentoDfeAdnDTO;
import br.com.webpublico.repository.mapper.IntegerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class NotaNacionalJDBCRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<RetornoDfeAdnDTO> buscarRetornosDfeAdn(Long notaFiscalId) {
        return jdbcTemplate.query(" select r.id, r.datahoraprocessamento, r.nsurecebimento, r.chaveacesso, r.statusprocessamento " +
                " from retornodfeadn r " +
                " where r.notafiscal_id = :id " +
                " order by r.id desc ", new Object[]{notaFiscalId}, new RetornoDfeAdnDTO());
    }

    public List<MensagemDfeAdnDTO> buscarMensagensDfeAdn(Long retornoId) {
        return jdbcTemplate.query(" select m.id, m.tipomensagem, m.codigo, m.descricao, m.complemento " +
                        " from mensagemdfeadn m " +
                        " where m.retornodfeadn_id = :id ", new Object[]{retornoId},
                new MensagemDfeAdnDTO());
    }

    private String getFromIntegracoes() {
        return " from (" +
                " select " +
                " r.id," +
                " ce.inscricaocadastral as inscricao_cadastral, " +
                " nf.id as id_nota_fiscal, " +
                " nf.numero as numero_nota_fiscal, " +
                " r.statusprocessamento as status, " +
                " r.chaveacesso as chave_acesso, " +
                " (select listagg(m.codigo||' - '||m.descricao|| " +
                "                       case " +
                "                           when m.complemento is not null then ' ('||coalesce(m.complemento, '')||') ' " +
                "                       end, ';') " +
                "               within group ( order by m.id ) " +
                "           from mensagemdfeadn m " +
                "        where m.retornodfeadn_id = r.id) as mensagem " +
                "   from notafiscal nf " +
                " inner join retornodfeadn r on r.id = (select max(sr.id) from retornodfeadn sr where sr.notafiscal_id = nf.id) " +
                " inner join cadastroeconomico ce on ce.id = nf.prestador_id) dados ";
    }

    public List<IntegracaoNotaNacionalDTO> buscarIntegracoes(Pageable pageable, List<ParametroQuery> parametros) throws Exception {
        StringBuilder sql = new StringBuilder(" select * " + getFromIntegracoes());

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValues(QueryUtil.montarParametroString(sql, parametros));
        sql.append(" order by dados.id ");
        parameters.addValue("offset", pageable.getOffset());
        parameters.addValue("limit", pageable.getPageSize());
        sql.append(" offset :offset rows fetch first :limit rows only ");

        return namedParameterJdbcTemplate.query(sql.toString(), parameters, new IntegracaoNotaNacionalDTO());
    }

    public Integer contarIntegracoes(List<ParametroQuery> parametros) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append(" select count(dados.id) as value ").append(getFromIntegracoes());

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValues(QueryUtil.montarParametroString(sql, parametros));

        List<Integer> query = namedParameterJdbcTemplate.query(sql.toString(),
                parameters, new IntegerMapper());
        if (query != null && !query.isEmpty()) {
            return query.stream().findFirst().get();
        }
        return 0;
    }

    public boolean existsIntegracaoComSucesso(Long notaFiscalId) {
        List<Integer> query = jdbcTemplate.query(" select count(1) as value " +
                        " from retornodfeadn r " +
                        " where r.notafiscal_id = :id " +
                        "   and r.statusprocessamento = :sucesso ",
                new Object[]{notaFiscalId, StatusProcessamentoDfeAdnDTO.PROCESSADO_COM_SUCESSO.name()},
                new IntegerMapper());
        if (query != null && !query.isEmpty()) {
            return query.stream().findFirst().get() > 0;
        }
        return false;
    }

    public List<GraficoQuantitativoNotaNacionalDTO> buscarQuantidadePorErro() {
        return jdbcTemplate.query(" select m.codigo||' - '||m.descricao||' '||coalesce(m.complemento, '') as descricao, " +
                        "                  count(1) as quantidade " +
                        "   from notafiscal nf " +
                        "  inner join retornodfeadn r on r.notafiscal_id = nf.id " +
                        "  inner join mensagemdfeadn m on m.retornodfeadn_id = r.id " +
                        " where r.id = (select max(sr.id) " +
                        "                  from retornodfeadn sr " +
                        "               where sr.notafiscal_id = nf.id) " +
                        "group by m.codigo||' - '||m.descricao||' '||coalesce(m.complemento, '') ",
                new GraficoQuantitativoNotaNacionalDTO());
    }

    public List<GraficoQuantitativoNotaNacionalDTO> buscarQuantidadePorStatus() {
        return jdbcTemplate.query(" select " +
                        "                  case " +
                        "                     when r.statusprocessamento = 'PROCESSADO_COM_ERROS' then 'Processado com erros'" +
                        "                     when r.statusprocessamento = 'PROCESSADO_COM_SUCESSO' then 'Processado com sucesso'" +
                        "                  end as descricao, " +
                        "                  count(1) as quantidade " +
                        "    from notafiscal nf " +
                        "   inner join retornodfeadn r on r.notafiscal_id = nf.id " +
                        " where r.id = (select max(sr.id) " +
                        "                  from retornodfeadn sr " +
                        "               where sr.notafiscal_id = nf.id) " +
                        " group by r.statusprocessamento ",
                new GraficoQuantitativoNotaNacionalDTO());
    }

    public List<Long> buscarIdsNotasPorCodigoErro(String erro) {
        return jdbcTemplate.query(" select nf.id " +
                        "   from notafiscal nf " +
                        "  inner join retornodfeadn r on r.notafiscal_id = nf.id " +
                        "  inner join mensagemdfeadn m on m.retornodfeadn_id = r.id " +
                        " where r.id = (select max(sr.id) " +
                        "                  from retornodfeadn sr " +
                        "               where sr.notafiscal_id = nf.id) " +
                        "  and not exists (select 1 from retornodfeadn sr" +
                        "                  where sr.notafiscal_id = nf.id" +
                        "                    and sr.statusProcessamento = :status)" +
                        "  and trim(lower(m.codigo)) = trim(lower(:erro)) ",
                new Object[]{StatusProcessamentoDfeAdnDTO.PROCESSADO_COM_SUCESSO.name(), erro},
                (resultSet, i) -> resultSet.getLong("id"));
    }
}
