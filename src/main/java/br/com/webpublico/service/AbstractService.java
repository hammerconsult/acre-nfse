package br.com.webpublico.service;

import br.com.webpublico.domain.dto.AbstractEntity;
import br.com.webpublico.domain.dto.consultanfse.ParametroQuery;
import br.com.webpublico.repository.AbstractJDBCRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public abstract class AbstractService<T extends AbstractEntity> {

    public abstract AbstractJDBCRepository getRepository();

    public List<T> preencher(List<T> registros) {
        if (registros != null) {
            for (T registro : registros) {
                preencher(registro);
            }
        }
        return registros;
    }

    public void preencher(T registro) {
    }

    public T findById(Long id) {
        T registro = (T) getRepository().findById(id);
        preencher(registro);
        return registro;
    }

    public List<T> consultar(Pageable pageable, List<ParametroQuery> parametros, String orderBy) throws Exception {
        List<T> registros = getRepository().consultar(pageable, parametros, orderBy);
        for (T registro : registros) {
            preencher(registro);
        }
        return registros;
    }

    public Page<T> consultarPaginado(Pageable pageable, List<ParametroQuery> parametros, String orderBy) throws Exception {
        Page page = getRepository().consultarPaginado(pageable, parametros, orderBy);
        for (T registro : (List<T>) page.getContent()) {
            preencher(registro);
        }
        return page;
    }

    public T findByAtributo(String atributo, Object valor) {
        T registro = (T) getRepository().findByAtributo(atributo, valor);
        preencher(registro);
        return registro;
    }

    @Transactional
    public void save(List<T> list) {
        if (list != null) {
            for (T dto : list) {
                save(dto);
            }
        }
    }

    @Transactional
    public T save(T dto) {
        dto = preSave(dto);
        if (dto.getId() == null) {
            dto = (T) getRepository().insert(dto);
        }
        dto = (T) getRepository().update(dto);
        dto = posSave(dto);
        return dto;
    }

    @Transactional
    public T posSave(T dto) {
        return dto;
    }

    @Transactional
    public T preSave(T dto) {
        dto.realizarValidacoes();
        return dto;
    }

    @Transactional
    public void remove(Long id) {
        AbstractEntity byId = getRepository().findById(id);
        getRepository().remove(byId);
    }
}
