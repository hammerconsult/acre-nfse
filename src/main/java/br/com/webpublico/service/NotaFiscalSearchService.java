package br.com.webpublico.service;

import br.com.webpublico.domain.dto.ConsultaGenericaNfseDTO;
import br.com.webpublico.domain.dto.ResumoDMSDTO;
import br.com.webpublico.domain.dto.enums.TipoMovimentoMensalNfseDTO;
import br.com.webpublico.domain.dto.search.NotaFiscalSearchDTO;
import br.com.webpublico.repository.AbstractJDBCRepository;
import br.com.webpublico.repository.NotaFiscalConsultaJDBCRepository;
import br.com.webpublico.repository.mongo.NotaFiscalSearchMongoRepository;
import br.com.webpublico.web.rest.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotaFiscalSearchService extends AbstractService<NotaFiscalSearchDTO> {

    @Autowired
    private NotaFiscalConsultaJDBCRepository repository;
    @Autowired
    private ConfiguracaoService configuracaoService;
    @Autowired
    private NotaFiscalSearchMongoRepository notaFiscalSearchMongoRepository;

    @Override
    public AbstractJDBCRepository getRepository() {
        return repository;
    }

    public Page<NotaFiscalSearchDTO> consultarUtimasDezNotasFiscais(Long prestador, ConsultaGenericaNfseDTO consultaGenerica) throws Exception {
        Pageable pageable = PaginationUtil.generatePageRequest(consultaGenerica.getOffset(), consultaGenerica.getLimit());
        Page<NotaFiscalSearchDTO> all;
        if (configuracaoService.getConfiguracaoFromServer().getUtilizaBancoCache()) {
            all = notaFiscalSearchMongoRepository.findByidPrestadorOrderByIdDesc(prestador, pageable);
            if (all.getTotalElements() > 0) {
                return all;
            }
        }
        List<NotaFiscalSearchDTO> dtos = repository.consultar(pageable, consultaGenerica.getParametrosQuery(), "order by obj.numero desc");
        Integer count = repository.contar(consultaGenerica.getParametrosQuery());
        if (configuracaoService.getConfiguracaoFromServer().getUtilizaBancoCache()) {
            notaFiscalSearchMongoRepository.saveAll(dtos);
        }
        return new PageImpl<>(dtos, pageable, count);
    }

    public List<NotaFiscalSearchDTO> buscarNotasSemDeclararPorCompetencia(Long prestadorId,
                                                                          int mes,
                                                                          int ano,
                                                                          TipoMovimentoMensalNfseDTO tipoMovimento,
                                                                          boolean somenteComIssDevido, int page, int size) {
        return repository.buscarNotasPorCompetenciaAndTipo(prestadorId, ano, mes,
                tipoMovimento, false, somenteComIssDevido, page, size);
    }

    public ResumoDMSDTO buscarResumoSemDeclararPorCompetencia(Long prestadorId,
                                                              int mes,
                                                              int ano,
                                                              TipoMovimentoMensalNfseDTO tipoMovimento,
                                                              boolean somenteComIssDevido) {
        return repository.buscarResumoPorCompetenciaAndTipo(prestadorId, ano, mes,
                tipoMovimento, false, somenteComIssDevido);
    }

    public ResumoDMSDTO buscarResumoPorDMS(Long idDMS) {
        return repository.buscarResumoPorDMS(idDMS);
    }

    public List<NotaFiscalSearchDTO> findByCalculo(Long calculoId) {
        return repository.findByCalculo(calculoId);
    }

    public List<NotaFiscalSearchDTO> buscarNotasByDeclaracaoMensalServico(Long idDms) {
        return repository.buscarNotasByDeclaracaoMensalServico(idDms);
    }
}
