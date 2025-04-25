package br.com.webpublico.service;

import br.com.webpublico.domain.dto.AnexoLei1232006NfseDTO;
import br.com.webpublico.domain.dto.MunicipioNfseDTO;
import br.com.webpublico.repository.AbstractJDBCRepository;
import br.com.webpublico.repository.AnexoLei1232006JDBCRepository;
import br.com.webpublico.repository.CidadeJDBCRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AnexoLei1232006Service extends AbstractService<AnexoLei1232006NfseDTO> {

    @Autowired
    private AnexoLei1232006JDBCRepository repository;

    @Override
    public AbstractJDBCRepository getRepository() {
        return repository;
    }

    public List<AnexoLei1232006NfseDTO> buscarAnexos() {
        List<AnexoLei1232006NfseDTO> anexos = repository.buscarAnexos();
        if (anexos != null) {
            for (AnexoLei1232006NfseDTO anexo : anexos) {
                preencher(anexo);
            }
        }
        return anexos;
    }

    @Override
    public void preencher(AnexoLei1232006NfseDTO registro) {
        registro.setFaixas(repository.buscarFaixasDoAnexo(registro));
    }
}
