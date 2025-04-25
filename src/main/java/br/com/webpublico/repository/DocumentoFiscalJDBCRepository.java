package br.com.webpublico.repository;

import br.com.webpublico.domain.dto.DocumentoFiscalDTO;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class DocumentoFiscalJDBCRepository extends AbstractJDBCRepository<DocumentoFiscalDTO> {

    @Override
    public String getSelect() {
        return "  SELECT  " +
                "   COALESCE(NF.ID, SD.ID) AS ID, " +
                "   OBJ.ID AS ID_DECLARACAO, " +
                "   OBJ.TIPODOCUMENTO AS TIPO_DOCUMENTO,  " +
                "   COALESCE(NF.NUMERO, SD.NUMERO) AS NUMERO,  " +
                "   COALESCE(NF.EMISSAO, SD.EMISSAO) AS EMISSAO,  " +
                "   DPP.CPFCNPJ AS PRESTADOR_CPFCNPJ,  " +
                "   DPP.NOMERAZAOSOCIAL AS PRESTADOR_NOMERAZAOSOCIAL,  " +
                "   DPT.CPFCNPJ AS TOMADOR_CPFCNPJ,  " +
                "   DPT.NOMERAZAOSOCIAL AS TOMADOR_NOMERAZAOSOCIAL,  " +
                "   OBJ.NATUREZAOPERACAO AS NATUREZA_OPERACAO,  " +
                "   OBJ.SITUACAO AS SITUACAO, " +
                "   COALESCE(OBJ.ISSRETIDO, 0) AS ISS_RETIDO,  " +
                "   COALESCE(OBJ.TOTALSERVICOS, 0) AS TOTAL_SERVICOS,  " +
                "   COALESCE(COALESCE(OBJ.DESCONTOSINCONDICIONAIS, OBJ.DESCONTOSCONDICIONAIS), 0) AS DESCONTOS, " +
                "   COALESCE(OBJ.DEDUCOESLEGAIS, 0) AS DEDUCOES, " +
                "   COALESCE(OBJ.VALORLIQUIDO, 0) AS VALOR_LIQUIDO,  " +
                "   COALESCE(OBJ.BASECALCULO, 0) AS BASE_CALCULO,  " +
                "   COALESCE(IDS.ALIQUOTASERVICO, 0) AS ALIQUOTA,  " +
                "   COALESCE(OBJ.ISSCALCULADO, 0) AS ISS_CALCULADO,  " +
                "   COALESCE(OBJ.ISSPAGAR, 0) AS ISS_PAGAR  ";
    }

    @Override
    public String getFrom() {
        return "  FROM DECLARACAOPRESTACAOSERVICO OBJ  " +
                " INNER JOIN ITEMDECLARACAOSERVICO IDS ON IDS.DECLARACAOPRESTACAOSERVICO_ID = OBJ.ID " +
                " LEFT JOIN NOTAFISCAL NF ON NF.DECLARACAOPRESTACAOSERVICO_ID = OBJ.ID  " +
                " LEFT JOIN SERVICODECLARADO SD ON SD.DECLARACAOPRESTACAOSERVICO_ID = OBJ.ID " +
                " LEFT JOIN DADOSPESSOAISNFSE DPP ON DPP.ID = OBJ.DADOSPESSOAISPRESTADOR_ID  " +
                " LEFT JOIN DADOSPESSOAISNFSE DPT ON DPT.ID = OBJ.DADOSPESSOAISTOMADOR_ID  ";
    }

    @Override
    public RowMapper<DocumentoFiscalDTO> newRowMapper() {
        return new DocumentoFiscalDTO();
    }

    @Override
    public DocumentoFiscalDTO insert(DocumentoFiscalDTO dto) {
        return null;
    }

    @Override
    public DocumentoFiscalDTO update(DocumentoFiscalDTO dto) {
        return null;
    }

    @Override
    public void remove(DocumentoFiscalDTO dto) {
    }
}
