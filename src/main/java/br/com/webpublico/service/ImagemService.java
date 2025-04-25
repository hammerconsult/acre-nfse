package br.com.webpublico.service;

import br.com.webpublico.domain.dto.ImagemUsuarioNfseDTO;
import br.com.webpublico.repository.mongo.ImagemUsuarioMongoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
@Transactional
public class ImagemService extends AbstractWPService {

    private final Logger log = LoggerFactory.getLogger(ImagemService.class);

    private final ImagemUsuarioMongoRepository imagemUsuarioMongoRepository;
    private final ConfiguracaoService configuracaoService;
    private final ArquivoService arquivoService;

    public ImagemService(ImagemUsuarioMongoRepository imagemUsuarioMongoRepository,
                         ConfiguracaoService configuracaoService,
                         ArquivoService arquivoService) {
        this.imagemUsuarioMongoRepository = imagemUsuarioMongoRepository;
        this.configuracaoService = configuracaoService;
        this.arquivoService = arquivoService;
    }

    @Override
    public String getTableName() {
        return "Arquivo";
    }

    @Override
    public String getDefaltSearchFields() {
        return "nome";
    }

    public ImagemUsuarioNfseDTO findOnMongo(Long id) {
        try {
            if (!configuracaoService.getConfiguracaoFromServer().getUtilizaBancoCache()) {
                return null;
            }
            ImagemUsuarioNfseDTO one = imagemUsuarioMongoRepository.findById(id).orElse(null);
            if (one != null) {
                return one;
            }
        } catch (Exception e) {
            log.debug("Erro ao buscar ImagemUsuario por id no mongo.", e);
            log.error("Erro ao buscar ImagemUsuario por id no mongo.");

        }
        return null;
    }

    public ImagemUsuarioNfseDTO deleteOnMongo(Long id) {
        try {
            if (!configuracaoService.getConfiguracaoFromServer().getUtilizaBancoCache()) {
                return null;
            }
            imagemUsuarioMongoRepository.deleteById(id);
        } catch (Exception e) {
            log.debug("Erro ao deletar ImagemUsuario por id no mongo.", e);
            log.error("Erro ao deletar ImagemUsuario por id no mongo.");
        }
        return null;
    }

    public void saveOnMongo(ImagemUsuarioNfseDTO dto) {
        try {
            if (!configuracaoService.getConfiguracaoFromServer().getUtilizaBancoCache()) {
                return;
            }
            if (dto != null) {
                imagemUsuarioMongoRepository.save(dto);
            }
        } catch (Exception e) {
            log.debug("Erro ao salvar ImagemUsuario no mongo.", e);
            log.error("Erro ao salvar ImagemUsuario no mongo.");
        }
    }

    public void updateImagemUsuario(ImagemUsuarioNfseDTO imagemDTO) {
        UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(urlsProperties.getWebpublicoPathNfse() + "/usuario/imagem");
        String url = uri.toUriString();
        restTemplate.postForEntity(url, imagemDTO, String.class);
    }

    public ImagemUsuarioNfseDTO getImagemUsuario(String login) {
        try {
            ImagemUsuarioNfseDTO imagemUsuarioNfseDTO = new ImagemUsuarioNfseDTO();
            imagemUsuarioNfseDTO.setConteudo(arquivoService.findByLogin(login).montarConteudo());
            return imagemUsuarioNfseDTO;
        } catch (Exception e) {
            return new ImagemUsuarioNfseDTO();
        }
    }

    public void deleteImage(Long id) {
        try {
            ImagemUsuarioNfseDTO byPessoaId = imagemUsuarioMongoRepository.findByPessoaId(id);
            if (byPessoaId != null) {
                imagemUsuarioMongoRepository.deleteById(byPessoaId.getId());
            }
            restTemplate.delete(urlsProperties.getWebpublicoPathNfse() + "/usuario/imagem/" + id);
        } catch (Exception e) {
            log.error("erro ao remover imagem", e);
        }
    }

    @Override
    public ParameterizedTypeReference<List<ImagemUsuarioNfseDTO>> getResponseTypeList() {
        return new ParameterizedTypeReference<List<ImagemUsuarioNfseDTO>>() {
        };
    }

    @Override
    public ParameterizedTypeReference<ImagemUsuarioNfseDTO> getResponseType() {
        return new ParameterizedTypeReference<ImagemUsuarioNfseDTO>() {
        };
    }
}
