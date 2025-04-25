package br.com.webpublico.service;

import br.com.webpublico.domain.dto.*;
import br.com.webpublico.security.SecurityUtils;
import br.com.webpublico.webreportdto.dto.comum.RelatorioDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
public class LivroFiscalService extends AbstractWPService<LivroFiscalNfseDTO> {

    @Autowired
    private WebReportService webReportService;
    @Autowired
    private CadastroEconomicoService cadastroEconomicoService;

    @Override
    public String getTableName() {
        return null;
    }

    @Override
    public String getDefaltSearchFields() {
        return null;
    }

    @Override
    public ParameterizedTypeReference<List<LivroFiscalNfseDTO>> getResponseTypeList() {
        return null;
    }

    @Override
    public ParameterizedTypeReference<LivroFiscalNfseDTO> getResponseType() {
        return null;
    }

    public List<LivroFiscalCompetenciaNfseDTO> buscarCompetenciasLivroFiscal(ConsultaLivroFiscalNfseDTO consulta) {
        HttpEntity entity = new HttpEntity(consulta);
        String url = urlsProperties.getWebpublicoPathNfse() + "/competencias-livro-fiscal";
        url = UriComponentsBuilder.fromUriString(url).toUriString();
        ResponseEntity<List<LivroFiscalCompetenciaNfseDTO>> exchange = restTemplate.exchange(url, HttpMethod.POST,
                entity, new ParameterizedTypeReference<List<LivroFiscalCompetenciaNfseDTO>>() {
                });
        return exchange.getBody();
    }

    public WebReportDTO imprimirLivroFiscal(LivroFiscalCompetenciaNfseDTO dto) {
        return webReportService.gerarRelatorio(SecurityUtils.getCurrentLogin(), criarRelatorioDTO(dto));
    }

    private RelatorioDTO criarRelatorioDTO(LivroFiscalCompetenciaNfseDTO livroFiscalDTO) {
        PrestadorServicoNfseDTO prestador = cadastroEconomicoService.findById(livroFiscalDTO.getPrestadorId());
        RelatorioDTO relatorioDTO = new RelatorioDTO();
        relatorioDTO.adicionarParametro("MUNICIPIO", "PREFEITURA DE RIO BRANCO");
        relatorioDTO.adicionarParametro("SECRETARIA", "SECRETARIA MUNICIPAL DE FINANÇAS");
        relatorioDTO.adicionarParametro("CONTRIBUINTE", prestador.toString());
        relatorioDTO.adicionarParametro("PRESTADOR_ID", livroFiscalDTO.getPrestadorId());
        relatorioDTO.adicionarParametro("EXERCICIO", livroFiscalDTO.getExercicio());
        relatorioDTO.adicionarParametro("MES", livroFiscalDTO.getMes());
        relatorioDTO.adicionarParametro("TIPO_MOVIMENTO", livroFiscalDTO.getTipoMovimento().name());
        relatorioDTO.adicionarParametro("QUANTIDADE", livroFiscalDTO.getQuantidade());
        relatorioDTO.adicionarParametro("ISSQN_PROPRIO", livroFiscalDTO.getIssqnProprio());
        relatorioDTO.adicionarParametro("ISSQN_RETIDO", livroFiscalDTO.getIssqnRetido());
        relatorioDTO.adicionarParametro("ISSQN_PAGO", livroFiscalDTO.getIssqnPago());
        relatorioDTO.adicionarParametro("SALDO_ISSQN", livroFiscalDTO.getSaldoIssqn());
        relatorioDTO.adicionarParametro("VALOR_SERVICO", livroFiscalDTO.getValorServico());
        relatorioDTO.adicionarParametro("DETALHADO", livroFiscalDTO.getDetalhado());
        relatorioDTO.setNomeRelatorio("livro-fiscal.pdf");
        relatorioDTO.setApi("tributario/nfse/livro-fiscal/");
        return relatorioDTO;
    }
}

