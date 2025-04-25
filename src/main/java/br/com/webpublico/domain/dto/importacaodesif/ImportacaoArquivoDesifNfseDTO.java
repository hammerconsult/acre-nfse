package br.com.webpublico.domain.dto.importacaodesif;

import br.com.webpublico.domain.dto.PrestadorServicoNfseDTO;
import com.beust.jcommander.internal.Lists;
import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public class ImportacaoArquivoDesifNfseDTO implements Serializable {

    private PrestadorServicoNfseDTO prestador;
    private String file;
    private List<ValidacaoDesifNfseDTO> validacoes;
    private Long idArquivo;

    public ImportacaoArquivoDesifNfseDTO() {
        validacoes = Lists.newArrayList();
    }

    public PrestadorServicoNfseDTO getPrestador() {
        return prestador;
    }

    public void setPrestador(PrestadorServicoNfseDTO prestador) {
        this.prestador = prestador;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public List<ValidacaoDesifNfseDTO> getValidacoes() {
        return validacoes;
    }

    public void setValidacoes(List<ValidacaoDesifNfseDTO> validacoes) {
        this.validacoes = validacoes;
    }

    public Long getIdArquivo() {
        return idArquivo;
    }

    public void setIdArquivo(Long idArquivo) {
        this.idArquivo = idArquivo;
    }

    public void adicionarValidacao(TipoValidacaoDesifNfseDTO tipoValidacao,
                                   String complemento) {
        if (validacoes == null) {
            validacoes = Lists.newArrayList();
        }
        ValidacaoDesifNfseDTO validacao = null;
        Optional<ValidacaoDesifNfseDTO> first = validacoes.stream()
                .filter(v -> v.getTipoValidacao().equals(tipoValidacao))
                .findFirst();
        if (!first.isPresent()) {
            validacao = new ValidacaoDesifNfseDTO();
            validacao.setTipoValidacao(tipoValidacao);
            validacoes.add(validacao);
        } else {
            validacao = first.get();
        }
        if (complemento == null) {
            complemento = "";
        }
        if (Strings.isNullOrEmpty(validacao.getMensagem())) {
            validacao.setMensagem(tipoValidacao.getMensagem() + " " + complemento);
        } else {
            validacao.setMensagem(validacao.getMensagem() + "; " + complemento);
        }
    }
}
