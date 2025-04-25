package br.com.webpublico.service;

import br.com.webpublico.domain.dto.*;
import br.com.webpublico.domain.dto.consultanfse.Operador;
import br.com.webpublico.domain.dto.consultanfse.ParametroQuery;
import br.com.webpublico.domain.dto.consultanfse.ParametroQueryCampo;
import br.com.webpublico.domain.dto.enums.TipoRelatorioNfseDTO;
import br.com.webpublico.repository.ItemDeclaracaoServicoJDBCRepository;
import br.com.webpublico.repository.RpsJDBCRepository;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

@Service
@Transactional
public class RpsService extends AbstractWPService<RpsNfseDTO> {

    private final Logger log = LoggerFactory.getLogger(RpsService.class);

    @Autowired
    private RpsJDBCRepository rpsJDBCRepository;
    @Autowired
    private CadastroEconomicoService cadastroEconomicoService;
    @Autowired
    private DadosPessoaisService dadosPessoaisService;
    @Autowired
    private ItemDeclaracaoServicoJDBCRepository itemDeclaracaoServicoJDBCRepository;
    @Autowired
    private ReportService reportService;


    @Override
    public String getTableName() {
        return "Rps";
    }

    @Override
    public String getDefaltSearchFields() {
        return "numero, serie";
    }

    public RpsNfseDTO buscarPorCnpjNumeroSerie(String cnpj, String numero, String serie) {
        return rpsJDBCRepository.findByCnpjNumeroSerie(cnpj, numero, serie);
    }

    public RpsNfseDTO buscarPorCmcNumeroSerie(String cmc, String numero, String serie) {
        return rpsJDBCRepository.findByCmcNumeroSerie(cmc, numero, serie);
    }

    @Override
    public ParameterizedTypeReference<List<RpsNfseDTO>> getResponseTypeList() {
        return new ParameterizedTypeReference<List<RpsNfseDTO>>() {
        };
    }


    public ParameterizedTypeReference<List<LoteRpsNfseDTO>> getResponseTypeListLoteRps() {
        return new ParameterizedTypeReference<List<LoteRpsNfseDTO>>() {
        };
    }

    public ParameterizedTypeReference<List<LoteRpsNfseDTO>> getResponseTypeLoteList() {
        return new ParameterizedTypeReference<List<LoteRpsNfseDTO>>() {
        };
    }

    @Override
    public ParameterizedTypeReference<RpsNfseDTO> getResponseType() {
        return new ParameterizedTypeReference<RpsNfseDTO>() {
        };
    }

    public ParameterizedTypeReference<LoteRpsNfseDTO> getResponseTypeLote() {
        return new ParameterizedTypeReference<LoteRpsNfseDTO>() {
        };
    }

    public void delete(Long id) {
        String url = urlsProperties.getWebpublicoPathNfse() + "/rps";
        url = UriComponentsBuilder.fromUriString(url)
                .queryParam("id", id)
                .toUriString();

        restTemplate.delete(url);
    }

    public RetornoImportacaoLoteRpsDTO importar(LoteImportacaoRPSNfseDTO lote) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<LoteImportacaoRPSNfseDTO> httpEntity = new HttpEntity<>(lote);
        ResponseEntity<RetornoImportacaoLoteRpsDTO> responseEntity = restTemplate.postForEntity(urlsProperties.getRpsService() +
                "/api/lote-rps/importar-xml", httpEntity, RetornoImportacaoLoteRpsDTO.class);
        return responseEntity.getBody();
    }

    public void gerarRelatorioRps(HttpServletResponse response, EmissaoRelatorioRpsDTO emissaoRelatorioRps) {
        try {
            List<RpsNfseDTO> listRps = rpsJDBCRepository.consultarRps(null, emissaoRelatorioRps.getConsultaGenerica().getParametrosQuery(),
                    emissaoRelatorioRps.getConsultaGenerica().getOrderBy());
            listRps.stream().forEach(rps -> preencherRPS(rps));
            byte[] bytes = gerarBytesRelatorioRps(listRps, emissaoRelatorioRps.getTipoRelatorio(),
                    emissaoRelatorioRps.getCriteriosUtilizados(), false);
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline; filename=RelatorioRPS.pdf");
            response.setContentLength(bytes.length);
            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(bytes, 0, bytes.length);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            log.error("Erro ao gerar relatório e notas emitidas {} ", e);
        }
    }

    public byte[] gerarBytesRelatorioRps(List<RpsNfseDTO> listRps,
                                         TipoRelatorioNfseDTO tipoRelatorio,
                                         String criteriosUtilizados,
                                         boolean excel) {
        byte[] bytes = new byte[0];
        try {
            String jrxml = "";
            if (TipoRelatorioNfseDTO.RESUMIDO.equals(tipoRelatorio)) {
                jrxml = "RelatorioRps.jrxml";
            } else {
                jrxml = "RelatorioRpsDetalhado.jrxml";
            }
            HashMap<String, Object> parametros = new HashMap<>();
            reportService.parametrosDefault(parametros);
            parametros.put("FILTROS", criteriosUtilizados);
            parametros.put("SUBREPORT_SERVICOS", reportService.compilarJrxml("RelatorioRpsDetalhado_servicos.jrxml"));
            return reportService.gerarRelatorio(jrxml, parametros, listRps, excel);
        } catch (Exception e) {
            log.error("Erro ao gerar o relatorio de rps {}", e);
        }
        return bytes;
    }

    public Page<RpsNfseDTO> buscarRps(Pageable pageable,
                                      Long idLote,
                                      Long idPrestador,
                                      String filtro) throws Exception {
        List<ParametroQuery> parametros = Lists.newArrayList();

        if (idLote != null)
            parametros.add(new ParametroQuery(" and ",
                    Lists.newArrayList(new ParametroQueryCampo(" R.LOTERPS_ID", Operador.IGUAL, idLote))));

        if (idPrestador != null)
            parametros.add(new ParametroQuery(" and ",
                    Lists.newArrayList(new ParametroQueryCampo(" R.PRESTADOR_ID", Operador.IGUAL, idPrestador))));

        if (filtro != null) {
            filtro = "%" + filtro.trim().toLowerCase() + "%";
            parametros.add(new ParametroQuery(" or ",
                    Lists.newArrayList(new ParametroQueryCampo(" LOWER(R.NUMERO) ", Operador.LIKE, filtro),
                            new ParametroQueryCampo(" LOWER(R.SERIE) ", Operador.LIKE, filtro),
                            new ParametroQueryCampo(" TO_CHAR(N.NUMERO) ", Operador.LIKE, filtro))));
        }

        List<RpsNfseDTO> dtos = rpsJDBCRepository.consultarRps(pageable, parametros, null);

        Integer count = rpsJDBCRepository.contarRps(parametros);

        return new PageImpl<>(dtos, pageable, count);
    }

    public Page<RpsNfseDTO> consultarRps(Pageable pageable,
                                         List<ParametroQuery> parametros, String orderBy) throws Exception {
        List<RpsNfseDTO> dtos = rpsJDBCRepository.consultarRps(pageable, parametros, orderBy);

        Integer count = rpsJDBCRepository.contarRps(parametros);

        return new PageImpl<>(dtos, pageable, count);
    }

    public RpsNfseDTO findById(Long id) {
        RpsNfseDTO rps = rpsJDBCRepository.findById(id);
        preencherRPS(rps);
        return rps;
    }

    private void preencherRPS(RpsNfseDTO dto) {
        if (dto != null) {
            if (dto.getPrestador() != null) {
                dto.setPrestador(cadastroEconomicoService.findById(dto.getPrestador().getId()));
            }
            if (dto.getDadosPessoaisPrestador() != null && dto.getDadosPessoaisPrestador().getId() != null) {
                dto.setDadosPessoaisPrestador(dadosPessoaisService.findById(dto.getDadosPessoaisPrestador().getId()));
            }
            if (dto.getDadosPessoaisTomador() != null && dto.getDadosPessoaisTomador().getId() != null) {
                dto.setDadosPessoaisTomador(dadosPessoaisService.findById(dto.getDadosPessoaisTomador().getId()));
            }
            dto.setItens(itemDeclaracaoServicoJDBCRepository.findByRPS(dto.getId()));
        }
    }

}
