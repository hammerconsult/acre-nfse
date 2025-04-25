package br.com.webpublico.service;

import br.com.webpublico.domain.dto.PrestadorUsuarioNfseDTO;
import br.com.webpublico.repository.CadastroEconomicoJDBCRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

@Transactional
@Service
public class UserCadastroEconomicoService implements Serializable {

    private final CadastroEconomicoJDBCRepository cadastroEconomicoJDBCRepository;

    public UserCadastroEconomicoService(CadastroEconomicoJDBCRepository cadastroEconomicoJDBCRepository) {
        this.cadastroEconomicoJDBCRepository = cadastroEconomicoJDBCRepository;
    }

    public PrestadorUsuarioNfseDTO findByUserAndPrestador(Long idUsuario, Long idCadstro) {
        PrestadorUsuarioNfseDTO userCadastroEconomico = cadastroEconomicoJDBCRepository.findByUserAndPrestador(idUsuario, idCadstro);
        if (userCadastroEconomico != null) {
            userCadastroEconomico.setPrestador(cadastroEconomicoJDBCRepository.findById(userCadastroEconomico.getPrestador().getId()));
            userCadastroEconomico.setRoles(cadastroEconomicoJDBCRepository.findRolesEmpresa(userCadastroEconomico.getId()));
        }
        return userCadastroEconomico;
    }

    public PrestadorUsuarioNfseDTO findFirstUserCadastroEconomico(Long id) {
        PrestadorUsuarioNfseDTO userCadastroEconomico = cadastroEconomicoJDBCRepository.findFirstUserCadastroEconomico(id);
        if (userCadastroEconomico == null) {
            return null;
        }
        userCadastroEconomico.setPrestador(cadastroEconomicoJDBCRepository.findById(userCadastroEconomico.getPrestador().getId()));
        userCadastroEconomico.setRoles(cadastroEconomicoJDBCRepository.findRolesEmpresa(userCadastroEconomico.getId()));
        return userCadastroEconomico;
    }

    public void ativarOrDesativarUserCadastroEconomico(Long idUserCadastroEconomico, Long idUserResponsavel) {
        cadastroEconomicoJDBCRepository.ativarOrDesativarUserCadastroEconomico(idUserCadastroEconomico, idUserResponsavel);
    }
}
