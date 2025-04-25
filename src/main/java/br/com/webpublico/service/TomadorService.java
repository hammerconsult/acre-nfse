package br.com.webpublico.service;

import br.com.webpublico.domain.dto.DadosPessoaisNfseDTO;
import br.com.webpublico.domain.dto.PessoaNfseDTO;
import br.com.webpublico.domain.dto.TomadorServicoDTO;
import br.com.webpublico.domain.dto.consultanfse.ParametroQuery;
import br.com.webpublico.repository.TomadorJDBCRepository;
import br.com.webpublico.web.rest.util.PaginationUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
@Transactional
public class TomadorService extends AbstractWPService<TomadorServicoDTO> {

    private final Logger log = LoggerFactory.getLogger(TomadorService.class);

    @Autowired
    private TomadorJDBCRepository tomadorJDBCRepository;
    @Autowired
    private PessoaService pessoaService;
    @Autowired
    private CadastroEconomicoService cadastroEconomicoService;
    @Autowired
    private DadosPessoaisService dadosPessoaisService;

    public TomadorServicoDTO findByCnpjAndPrestador(String cpfCnpj, Long idPrestador) {
        TomadorServicoDTO tomador = tomadorJDBCRepository.findByCpfCnpjAndPrestador(cpfCnpj, idPrestador);
        preencher(tomador);
        return tomador;
    }

    public void preencher(TomadorServicoDTO tomadorServicoDTO) {
        if (tomadorServicoDTO != null) {
            if (tomadorServicoDTO.getDadosPessoais() != null) {
                tomadorServicoDTO.setDadosPessoais(dadosPessoaisService.findById(tomadorServicoDTO.getDadosPessoais().getId()));
            }
            if (tomadorServicoDTO.getPrestador() != null) {
                tomadorServicoDTO.setPrestador(cadastroEconomicoService.findById(tomadorServicoDTO.getPrestador().getId()));
            }
        }
    }


    public TomadorServicoDTO findInPessoaRepoByCpfCnpj(String cpf) throws CloneNotSupportedException {
        if (cpf != null && !cpf.isEmpty()) {
            PessoaNfseDTO pessoa = pessoaService.findByCpfCnpj(cpf.trim());
            DadosPessoaisNfseDTO dadosPessoais = null;
            if (pessoa != null) {
                dadosPessoais = pessoa.getDadosPessoais();
            } else {
                dadosPessoais = dadosPessoaisService.findByCpfCnpj(cpf.trim());
            }
            if (dadosPessoais != null) {
                TomadorServicoDTO result = new TomadorServicoDTO();
                result.setDadosPessoais(dadosPessoais.clone());
                result.getDadosPessoais().setId(null);
                return result;
            }
        }
        throw new RuntimeException("Não foi encontrada nenhuma pessoa com o cpf " + cpf);
    }

    @Override
    public String getTableName() {
        return "TomadorServicoNfse";
    }

    @Override
    public String getDefaltSearchFields() {
        return "dadosPessoaisNfse.cpfCnpj, dadosPessoaisNfse.nomeRazaoSocial, dadosPessoaisNfse.nomeFantasia ";
    }

    @Override
    public ParameterizedTypeReference<List<TomadorServicoDTO>> getResponseTypeList() {
        return new ParameterizedTypeReference<List<TomadorServicoDTO>>() {
        };
    }

    @Override
    public ParameterizedTypeReference<TomadorServicoDTO> getResponseType() {
        return new ParameterizedTypeReference<TomadorServicoDTO>() {
        };
    }

    public TomadorServicoDTO salvar(TomadorServicoDTO tomador) {
        if (tomador.getDadosPessoais() != null) {
            dadosPessoaisService.salvar(tomador.getDadosPessoais());
        }
        if (tomador.getId() == null) {
            tomadorJDBCRepository.inserirTomador(tomador);
        }
        return tomador;
    }

    public ResponseEntity<List<TomadorServicoDTO>> findAll(Pageable pageable, Long prestadorId, String query) {
        return findByQuery(query, pageable, prestadorId);
    }

    public ResponseEntity<List<TomadorServicoDTO>> findByQuery(String query, Long prestadorId) {
        return findByQuery(query, null, prestadorId);
    }

    public ResponseEntity<List<TomadorServicoDTO>> findByQuery(String query, Pageable pageable, Long prestadorId) {
        if (pageable == null) {
            pageable = PaginationUtil.generatePageRequest(0, 100);
        }
        String url = PaginationUtil.addParamsToUrl(urlsProperties.getWebpublicoPathNfse() + "/tomador/listar",
                pageable, query, getTableName(), getDefaltSearchFields());
        url = UriComponentsBuilder.fromUriString(url).queryParam("prestadorId", prestadorId).toUriString();
        return restTemplate.exchange(url, HttpMethod.GET, null, getResponseTypeList());
    }

    public TomadorServicoDTO findById(Long id) {
        TomadorServicoDTO dto = tomadorJDBCRepository.findById(id);
        if (dto.getPrestador() != null) {
            dto.setPrestador(cadastroEconomicoService.findOne(dto.getPrestador().getId()).getBody());
        }
        dto.setDadosPessoais(dadosPessoaisService.findById(dto.getDadosPessoais().getId()));
        return dto;
    }

    public Page<TomadorServicoDTO> consultarTomadores(Pageable pageable,
                                                      List<ParametroQuery> parametros,
                                                      String orderBy) throws Exception {
        List<TomadorServicoDTO> dtos = tomadorJDBCRepository.buscarTomadores(pageable, parametros, orderBy);

        Integer count = tomadorJDBCRepository.contarTomadores(parametros);

        return new PageImpl<>(dtos, pageable, count);
    }

    public void delete(Long id) {
        tomadorJDBCRepository.inativarTomador(id);
    }
}
