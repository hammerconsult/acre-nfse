package br.com.webpublico.service;

import br.com.webpublico.domain.dto.ConstrucaoCivilNfseDTO;
import br.com.webpublico.repository.ConstrucaoCivilJDBCRepository;
import br.com.webpublico.web.rest.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
@Transactional
public class ConstrucaoCivilService extends AbstractWPService<ConstrucaoCivilNfseDTO> {

    @Autowired
    private ConstrucaoCivilJDBCRepository construcaoCivilJDBCRepository;
    @Autowired
    private CadastroEconomicoService cadastroEconomicoService;
    @Autowired
    private PessoaService pessoaService;
    @Autowired
    private ConstrucaoCivilLocalizacaoService construcaoCivilLocalizacaoService;

    @Override
    public String getTableName() {
        return "";
    }

    @Override
    public String getDefaltSearchFields() {
        return null;
    }

    @Override
    public ParameterizedTypeReference<List<ConstrucaoCivilNfseDTO>> getResponseTypeList() {
        return new ParameterizedTypeReference<List<ConstrucaoCivilNfseDTO>>() {
        };
    }

    @Override
    public ParameterizedTypeReference<ConstrucaoCivilNfseDTO> getResponseType() {
        return null;
    }


    public ResponseEntity<List<ConstrucaoCivilNfseDTO>> buscarTodasConstrucaoFromPrestador(Pageable pageable,
                                                                                           Long prestador,
                                                                                           String filtro) {
        String url = urlsProperties.getWebpublicoPathNfse() + "/construcao-civil/from-prestador";
        url = UriComponentsBuilder.fromUriString(url)
                .queryParam("prestadorId", prestador)
                .queryParam("filtro", filtro)
                .toUriString();

        UriComponentsBuilder uriComponentsBuilder = PaginationUtil.addPageableParam(url, pageable);

        return restTemplate.exchange(uriComponentsBuilder.toUriString(), HttpMethod.GET, null, getResponseTypeList());
    }

    public ResponseEntity<ConstrucaoCivilNfseDTO> salvar(ConstrucaoCivilNfseDTO construcaoCivil) {
        String url = UriComponentsBuilder.fromUriString(urlsProperties.getWebpublicoPathNfse() + "/construcao-civil").toUriString();
        return restTemplate.postForEntity(url, construcaoCivil, ConstrucaoCivilNfseDTO.class);
    }

    public ResponseEntity<ConstrucaoCivilNfseDTO> getById(Long id) {
        return restTemplate.getForEntity(urlsProperties.getWebpublicoPathNfse() + "/construcao-civil/" + id, ConstrucaoCivilNfseDTO.class);
    }

    public void delete(Long id) {
        restTemplate.delete(urlsProperties.getWebpublicoPathNfse() + "/construcao-civil/" + id);
    }

    public ResponseEntity<ConstrucaoCivilNfseDTO> buscarConstrucaoCivilFromArt(String art) {
        String url = urlsProperties.getWebpublicoPathNfse() + "/construcao-civil/by-art";
        url = UriComponentsBuilder.fromUriString(url)
                .queryParam("art", art)
                .toUriString();

        return restTemplate.getForEntity(url, ConstrucaoCivilNfseDTO.class);
    }

    public ConstrucaoCivilNfseDTO findById(Long id) {
        ConstrucaoCivilNfseDTO dto = construcaoCivilJDBCRepository.findById(id);
        if (dto != null) {
            if (dto.getResponsavel() != null) {
                dto.setResponsavel(pessoaService.findById(dto.getResponsavel().getId()));
            }
            if (dto.getLocalizacao() != null) {
                dto.setLocalizacao(construcaoCivilLocalizacaoService.findById(dto.getLocalizacao().getId()));
            }
            if (dto.getPrestador() != null) {
                dto.setPrestador(cadastroEconomicoService.findOne(dto.getPrestador().getId()).getBody());
            }
        }
        return dto;
    }

    public void inserir(ConstrucaoCivilNfseDTO dto) {
        if (dto.getLocalizacao() != null) {
            construcaoCivilJDBCRepository.inserirLocalizacao(dto.getLocalizacao());
        }
        construcaoCivilJDBCRepository.inserir(dto);
    }
}
