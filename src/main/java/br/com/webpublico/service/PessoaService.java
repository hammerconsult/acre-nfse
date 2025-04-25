package br.com.webpublico.service;

import br.com.webpublico.StringUtils;
import br.com.webpublico.domain.dto.*;
import br.com.webpublico.repository.PessoaJDBCRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.List;

@Service
@Transactional
public class PessoaService extends AbstractWPService<PessoaNfseDTO> {

    private final Logger log = LoggerFactory.getLogger(PessoaService.class);

    private final PessoaJDBCRepository pessoaJDBCRepository;
    private final ImagemService imagemService;
    private final ArquivoService arquivoService;

    public PessoaService(PessoaJDBCRepository pessoaJDBCRepository, ImagemService imagemService,
                         ArquivoService arquivoService) {
        this.pessoaJDBCRepository = pessoaJDBCRepository;
        this.imagemService = imagemService;
        this.arquivoService = arquivoService;
    }

    public PessoaNfseDTO findByLogin(String login) {
        UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(urlsProperties.getWebpublicoPathNfse() + "/pessoa_do_usuario")
                .queryParam("login", login);
        String url = uri.toUriString();
        return restTemplate.exchange(url, HttpMethod.GET, null, PessoaNfseDTO.class).getBody();
    }

    public ResponseEntity<PessoaNfseDTO> findOne(Long id) {
        UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(urlsProperties.getWebpublicoPathNfse() + "/pessoa")
                .queryParam("id", id);
        String url = uri.toUriString();
        return restTemplate.exchange(url, HttpMethod.GET, null, PessoaNfseDTO.class);
    }

    @Override
    public String getTableName() {
        return "Pessoa";
    }

    @Override
    public String getDefaltSearchFields() {
        return "nome, email";
    }

    @Override
    public ParameterizedTypeReference<List<PessoaNfseDTO>> getResponseTypeList() {
        return new ParameterizedTypeReference<List<PessoaNfseDTO>>() {
        };
    }

    @Override
    public ParameterizedTypeReference<PessoaNfseDTO> getResponseType() {
        return new ParameterizedTypeReference<PessoaNfseDTO>() {
        };
    }

    public PessoaNfseDTO findByCpfCnpj(String cpfCnpj) {
        PessoaNfseDTO dto = pessoaJDBCRepository.findByCpfCnpj(StringUtils.removerMascaraCpfCnpj(cpfCnpj));
        preencher(dto);
        return dto;
    }

    public PessoaNfseDTO findById(Long id) {
        PessoaNfseDTO dto = pessoaJDBCRepository.findById(id);
        preencher(dto);
        return dto;
    }

    private void preencher(PessoaNfseDTO dto) {
        if (dto != null) {
            List<TelefoneNfseDTO> telefones = pessoaJDBCRepository.findTelefones(dto.getId());
            TelefoneNfseDTO fone = TelefoneNfseDTO.getTelefonePorTipo("FIXO", telefones);
            if (fone != null) {
                dto.getDadosPessoais().setTelefone(fone.getTelefone());
            }
            TelefoneNfseDTO celular = TelefoneNfseDTO.getTelefonePorTipo("CELULAR", telefones);
            if (celular != null) {
                dto.getDadosPessoais().setTelefone(celular.getTelefone());
            }
        }
    }

    public Long getIdArquivo(Long idPessoa) {
        return pessoaJDBCRepository.getIdArquivo(idPessoa);
    }

    public void updateArquivo(PessoaNfseDTO pessoa, ArquivoNfseDTO arquivo) {
        pessoaJDBCRepository.updateArquivo(pessoa, arquivo);
    }

    public PessoaNfseDTO save(PessoaNfseDTO pessoa) {
        pessoaJDBCRepository.update(pessoa);
        pessoaJDBCRepository.saveTelefones(pessoa);
        return pessoa;
    }

    public ImagemUsuarioNfseDTO getImagemPessoa(Long id) {
        ImagemUsuarioNfseDTO one = imagemService.findOnMongo(id);
        if (one != null) return one;
        PessoaNfseDTO pessoa = findById(id);
        Long idArquivo = getIdArquivo(id);
        if (idArquivo != null && idArquivo != 0) {
            ArquivoNfseDTO arquivo = arquivoService.findById(idArquivo);
            ImagemUsuarioNfseDTO imagemUsuarioNfseDTO = new ImagemUsuarioNfseDTO();
            imagemUsuarioNfseDTO.setId(pessoa.getId());
            imagemUsuarioNfseDTO.setPessoa(pessoa);
            imagemUsuarioNfseDTO.setConteudo(arquivo.montarConteudo());
            imagemService.saveOnMongo(imagemUsuarioNfseDTO);
            return imagemUsuarioNfseDTO;
        }
        return null;
    }

    public void updateImagemPessoa(ImagemUsuarioNfseDTO imagemDTO) throws IOException {
        PessoaNfseDTO pessoa = findById(imagemDTO.getPessoa().getId());
        ArquivoNfseDTO arquivo = new ArquivoNfseDTO().toArquivo(imagemDTO.getConteudo(), "Imagem da Pessoa");
        arquivoService.save(arquivo);
        updateArquivo(pessoa, arquivo);
        imagemDTO.setId(pessoa.getId());
        imagemDTO.setPessoa(pessoa);
        imagemDTO.setConteudo(arquivo.montarConteudo());
        imagemService.saveOnMongo(imagemDTO);
    }

}
