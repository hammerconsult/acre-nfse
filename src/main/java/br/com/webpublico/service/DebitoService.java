package br.com.webpublico.service;

import br.com.webpublico.domain.dto.DebitoNfseDTO;
import br.com.webpublico.repository.DebitoJDBCRepository;
import br.com.webpublico.tributario.consultadebitos.ConsultaParcela;
import br.com.webpublico.tributario.consultadebitos.ResultadoParcela;
import br.com.webpublico.tributario.consultadebitos.enums.SituacaoParcelaDTO;
import br.com.webpublico.tributario.consultadebitos.services.ConsultaDebitosService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

@Service
@Transactional
public class DebitoService implements Serializable {

    private final DebitoJDBCRepository repository;

    private ConsultaDebitosService consultaDebitosService;

    public DebitoService(DebitoJDBCRepository repository) {
        this.repository = repository;
    }

    public ConsultaDebitosService getConsultaDebitosService() {
        if (consultaDebitosService == null) {
            consultaDebitosService = ConsultaDebitosService.buildFromJdbcTemplate(repository.getJdbcTemplate());
        }
        return consultaDebitosService;
    }

    public DebitoNfseDTO gravarDebito(DebitoNfseDTO debito) {
        return repository.gravarDebito(debito);
    }

    public boolean hasDebitoPago(Long idCalculo) {
        return repository.hasDebitoPago(idCalculo);
    }

    public void cancelarDebito(Long idCalculo) {
        List<Long> idsParcelas = repository.buscarIdsParcelas(idCalculo);
        for (Long idParcela : idsParcelas) {
            repository.inserirSituacaoParcelaValorDivida(new Date(),
                    SituacaoParcelaDTO.CANCELAMENTO, idParcela, BigDecimal.ZERO,
                    "Cancelamento de DMS");
        }
    }

    public List<ResultadoParcela> buscarParcelasPorCalculoId(Long calculoId) {
        HashSet<ResultadoParcela> parcelasFinais = new HashSet<>();
        ConsultaParcela consultaParcela = new ConsultaParcela(getConsultaDebitosService());
        consultaParcela.addParameter(ConsultaParcela.Campo.CALCULO_ID, ConsultaParcela.Operador.IGUAL, calculoId);
        List<ResultadoParcela> parcelasCalculo = consultaParcela.executaConsulta().getResultados();
        for (ResultadoParcela parcela : parcelasCalculo) {
            recuperarParcelasRecusivamente(parcelasFinais, null, parcela);
        }
        return Lists.newArrayList(parcelasFinais);
    }

    private void recuperarParcelasRecusivamente(HashSet<ResultadoParcela> parcelasDeclaracao,
                                                ResultadoParcela parcelaAnterior,
                                                ResultadoParcela parcelaAtual) {
        if (parcelaAtual.isEmAberto() || parcelaAtual.isPago() || (parcelaAtual.isCancelado() && parcelaAnterior == null)) {
            parcelasDeclaracao.add(parcelaAtual);
            return;
        }
        if (parcelaAtual.isInscrito()) {
            ConsultaParcela consultaParcela = new ConsultaParcela(consultaDebitosService);
            consultaParcela.addComplementoDoWhere(" where exists (select 1 " +
                    "                                                from inscricaodividaparcela idp" +
                    "                                             where idp.parcelavalordivida_id = " + parcelaAtual.getIdParcela() + "" +
                    "                                               and idp.iteminscricaodividaativa_id = " +
                    ConsultaParcela.Campo.CALCULO_ID.getCampo() + ") ");
            List<ResultadoParcela> parcelas = consultaParcela.executaConsulta().getResultados();
            for (ResultadoParcela parcela : parcelas) {
                recuperarParcelasRecusivamente(parcelasDeclaracao, parcelaAtual, parcela);
            }
        }
        if (parcelaAtual.isParcelado()) {
            ConsultaParcela consultaParcela = new ConsultaParcela(consultaDebitosService);
            consultaParcela.addComplementoDoWhere(" where exists (select 1 " +
                    "                                                from itemprocessoparcelamento ipp " +
                    "                                             where ipp.parcelavalordivida_id = " + parcelaAtual.getIdParcela() + "" +
                    "                                               and ipp.processoparcelamento_id = " +
                    ConsultaParcela.Campo.CALCULO_ID.getCampo() + ") ");
            List<ResultadoParcela> parcelas = consultaParcela.executaConsulta().getResultados();
            for (ResultadoParcela parcela : parcelas) {
                recuperarParcelasRecusivamente(parcelasDeclaracao, parcelaAtual, parcela);
            }
        }
    }
}
