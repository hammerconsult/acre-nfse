package br.com.webpublico.service;

import br.com.webpublico.FileUtils;
import br.com.webpublico.domain.dto.DetentorArquivoComposicaoNfseDTO;
import br.com.webpublico.domain.dto.NoticiaNfseDTO;
import br.com.webpublico.repository.AbstractJDBCRepository;
import br.com.webpublico.repository.NoticiaJDBCRepository;
import br.com.webpublico.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticiaNfseService extends AbstractService<NoticiaNfseDTO> {

    @Autowired
    private NoticiaJDBCRepository repository;
    @Autowired
    private DetentorArquivoComposicaoService detentorArquivoComposicaoService;

    @Override
    public AbstractJDBCRepository getRepository() {
        return repository;
    }

    public List<NoticiaNfseDTO> buscarUltimasNoticias() {
        List<NoticiaNfseDTO> ultimasNoticias = repository.buscarUltimasNoticias();
        if (ultimasNoticias != null) {
            for (NoticiaNfseDTO noticia : ultimasNoticias) {
                transformarDetentorBase64(noticia);
            }
        }
        return ultimasNoticias;
    }

    private void transformarDetentorBase64(NoticiaNfseDTO noticia) {
        DetentorArquivoComposicaoNfseDTO detentor = detentorArquivoComposicaoService.findById(noticia.getIdDetentor());
        byte[] dados = Util.getByteArrayDosDados(detentor.getArquivo().getArquivoNfseDTO().getPartes());
        noticia.setImagem("data:image/png;base64," +
                FileUtils.getBase64Encode(dados));
    }

    @Override
    public void preencher(NoticiaNfseDTO registro) {
        if (registro == null) {
            return;
        }
        transformarDetentorBase64(registro);
    }
}
