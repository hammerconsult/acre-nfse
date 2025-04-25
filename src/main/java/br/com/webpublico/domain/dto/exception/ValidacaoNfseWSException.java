package br.com.webpublico.domain.dto.exception;

import br.com.webpublico.domain.dto.exception.TipoValidacao;
import br.com.webpublico.domain.dto.nfse203.ListaMensagemRetorno;
import br.com.webpublico.domain.dto.nfse203.TcMensagemRetorno;
import com.google.common.collect.Lists;

import java.util.List;

public class ValidacaoNfseWSException extends Throwable {

    private List<TipoValidacao> validacoes;
    private String[] params;

    public ValidacaoNfseWSException() {
        validacoes = Lists.newArrayList();
    }

    public ValidacaoNfseWSException(TipoValidacao tipoValidacao) {
        this();
        validacoes.add(tipoValidacao);
    }

    public ValidacaoNfseWSException(TipoValidacao tipoValidacao, String... params) {
        this();
        validacoes.add(tipoValidacao);
        this.params = params;
    }

    public ValidacaoNfseWSException(List<TipoValidacao> tipos) {
        this();
        validacoes.addAll(tipos);
    }

    public List<TipoValidacao> getValidacoes() {
        return validacoes;
    }

    public String[] getParams() {
        return params;
    }

    public ListaMensagemRetorno toListaMensagemRetorno203() {
        ListaMensagemRetorno lista = new ListaMensagemRetorno();
        for (TipoValidacao validacao : getValidacoes()) {
            TcMensagemRetorno tcMensagemRetorno = new TcMensagemRetorno();
            tcMensagemRetorno.setCodigo(validacao.name());
            if (params != null && params.length > 0) {
                tcMensagemRetorno.setMensagem(String.format(validacao.getDescricao(), params));
            } else {
                tcMensagemRetorno.setMensagem(validacao.getDescricao());
            }
            tcMensagemRetorno.setCorrecao(validacao.getSugestao());
            lista.getMensagemRetorno().add(tcMensagemRetorno);
        }
        return lista;
    }

    public br.com.webpublico.domain.dto.nfse12.ListaMensagemRetorno toListaMensagemRetorno12() {
        br.com.webpublico.domain.dto.nfse12.ListaMensagemRetorno lista = new br.com.webpublico.domain.dto.nfse12.ListaMensagemRetorno();
        for (TipoValidacao validacao : getValidacoes()) {
            br.com.webpublico.domain.dto.nfse12.TcMensagemRetorno tcMensagemRetorno = new br.com.webpublico.domain.dto.nfse12.TcMensagemRetorno();
            tcMensagemRetorno.setCodigo(validacao.name());
            if (params != null && params.length > 0) {
                tcMensagemRetorno.setMensagem(String.format(validacao.getDescricao(), params));
            } else {
                tcMensagemRetorno.setMensagem(validacao.getDescricao());
            }
            tcMensagemRetorno.setCorrecao(validacao.getSugestao());
            lista.getMensagemRetorno().add(tcMensagemRetorno);
        }
        return lista;
    }

    public br.com.webpublico.domain.dto.nfse10.ListaMensagemRetorno toListaMensagemRetorno10() {
        br.com.webpublico.domain.dto.nfse10.ListaMensagemRetorno lista = new br.com.webpublico.domain.dto.nfse10.ListaMensagemRetorno();
        for (TipoValidacao validacao : getValidacoes()) {
            br.com.webpublico.domain.dto.nfse10.TcMensagemRetorno tcMensagemRetorno = new br.com.webpublico.domain.dto.nfse10.TcMensagemRetorno();
            tcMensagemRetorno.setCodigo(validacao.name());
            if (params != null && params.length > 0) {
                tcMensagemRetorno.setMensagem(String.format(validacao.getDescricao(), params));
            } else {
                tcMensagemRetorno.setMensagem(validacao.getDescricao());
            }
            tcMensagemRetorno.setCorrecao(validacao.getSugestao());
            lista.getMensagemRetorno().add(tcMensagemRetorno);
        }
        return lista;
    }
}
