package br.com.webpublico.service;

import br.com.webpublico.domain.dto.ConfiguracaoNfseDTO;
import br.com.webpublico.domain.dto.NotaFiscalNfseDTO;
import br.com.webpublico.repository.NotaFiscalJDBCRepository;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.Future;

@Service
@Transactional
public class EmailNotaService {

    public static final Logger logger = LoggerFactory.getLogger(EmailNotaService.class);
    private Boolean enviando = false;
    @Autowired
    private NotaFiscalService notaFiscalService;
    @Autowired
    private NotaFiscalJDBCRepository notaFiscalJDBCRepository;
    @Autowired
    private ConfiguracaoService configuracaoService;

    @Scheduled(cron = "0 0/5 * * * ?")
    public void cronEnviarNota() {
        comunicarNotasPorEmail();
    }

    public void comunicarNotasPorEmail() {
        if (enviando)
            return;
        ConfiguracaoNfseDTO config = configuracaoService.getConfiguracaoFromServer();
        if (!config.getProducao() && !config.getEnviaEmailEmHomologacao()) {
            logger.info("Não vai inicar a comunicação por email das notas fiscais");
            return;
        }
        try {
            enviando = true;
            logger.info("Iniciando comunicação por email das notas fiscais");
            List<NotaFiscalNfseDTO> notas = notaFiscalJDBCRepository.consultarNotasFiscaisParaEnviarPorEmail();
            if (notas != null && !notas.isEmpty()) {
                List<List<NotaFiscalNfseDTO>> notasParticionadas = Lists.partition(notas, 20);
                List<Future> futures = Lists.newArrayList();
                for (List<NotaFiscalNfseDTO> parte : notasParticionadas) {
                    futures.add(enviarNotasPorEmail(parte));
                }
                while (futuresEmAndamento(futures)) {
                }
            }
            logger.info("Finalizou comunicação por email das notas fiscais");
        } catch (Exception e) {
            logger.error("Erro ao enviar nota por email. {}", e.getMessage());
            logger.debug("Detalhes do erro ao enviar nota por email. ", e);
        } finally {
            enviando = false;
        }
    }

    private boolean futuresEmAndamento(List<Future> futures) {
        if (futures == null || futures.isEmpty())
            return false;
        for (Future future : futures) {
            if (!future.isDone() && !future.isCancelled())
                return true;
        }
        return false;
    }

    @Async
    public Future enviarNotasPorEmail(List<NotaFiscalNfseDTO> notas) {
        if (notas != null && !notas.isEmpty())
            for (NotaFiscalNfseDTO nota : notas) {
                try {
                    notaFiscalService.enviarNotaPorEmail(nota.getEmails(), notaFiscalService.findById(nota.getId()));
                } catch (Exception e) {
                    logger.error("Erro ao comunicar nota por email. {}", e.getMessage());
                    logger.debug("Detalhes do erro ao comunicar nota por email. ", e);
                }
            }
        return new AsyncResult(null);
    }
}
