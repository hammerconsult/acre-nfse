package br.com.webpublico.service;

import br.com.webpublico.domain.dto.ArquivoNfseDTO;
import br.com.webpublico.domain.dto.ArquivoParteNfseDTO;
import br.com.webpublico.repository.AbstractJDBCRepository;
import br.com.webpublico.repository.ArquivoJDBCRepository;
import org.apache.commons.codec.binary.Base64;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
@Transactional
public class ArquivoService extends AbstractService<ArquivoNfseDTO> {

    @Autowired
    private ArquivoJDBCRepository repository;
    @Autowired
    private ArquivoParteService arquivoParteService;

    @Override
    public ArquivoJDBCRepository getRepository() {
        return repository;
    }

    @Override
    public void preencher(ArquivoNfseDTO registro) {
        if (registro == null) {
            return;
        }
        registro.setPartes(arquivoParteService.findByArquivo(registro));
    }

    @Override
    @Transactional
    public ArquivoNfseDTO posSave(ArquivoNfseDTO dto) {
        for (ArquivoParteNfseDTO parte : dto.getPartes()) {
            if (parte.getIdArquivo() == null) {
                parte.setIdArquivo(dto.getId());
            }
        }
        arquivoParteService.save(dto.getPartes());
        return dto;
    }

    public ArquivoNfseDTO findByLogin(String login) {
        ArquivoNfseDTO byLogin = repository.findByLogin(login);
        preencher(byLogin);
        return byLogin;
    }

    public byte[] montarArquivoParaDownload(ArquivoNfseDTO arquivo) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        for (ArquivoParteNfseDTO a : arquivo.getPartes()) {
            try {
                buffer.write(a.getDados());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        byte[] byteFile = new byte[buffer.size()];
        InputStream is = new ByteArrayInputStream(buffer.toByteArray());
        try {
            is.read(byteFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteFile;

    }

    public String getExtension(String mimeType) {
        try {
            MimeTypes allTypes = MimeTypes.getDefaultMimeTypes();
            MimeType extension = allTypes.forName(mimeType);
            return extension.getExtension();
        } catch (MimeTypeException e) {
        }
        return "";
    }

}
