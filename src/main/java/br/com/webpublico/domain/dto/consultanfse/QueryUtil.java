package br.com.webpublico.domain.dto.consultanfse;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class QueryUtil {
    public static Map<String, Object> montarParametroString(StringBuilder sqlBuilder, List<ParametroQuery> parametros) throws Exception {
        Map<String, Object> parameters = Maps.newHashMap();
        if (parametros != null && !parametros.isEmpty()) {
            String where = "";
            String juncao = " where ";
            AtomicInteger index = new AtomicInteger();
            for (ParametroQuery parametro : parametros) {
                if (parametro.getParametroQueryCampos() != null && !parametro.getParametroQueryCampos().isEmpty()) {
                    where += juncao + " ( ";
                    String juncaoCampo = " ";
                    for (ParametroQueryCampo parametroQueryCampo : parametro.getParametroQueryCampos()) {
                        where += juncaoCampo + parametroQueryCampo.getCampo() + " ";

                        if (parametroQueryCampo.getOperador() != null) {
                            where += parametroQueryCampo.getOperador().getOperador() + " ";
                        }

                        if (parametroQueryCampo.getValor() != null) {
                            parameters.put("index_" + index.get(), parametroQueryCampo.getValor());
                            if (Operador.IN.equals(parametroQueryCampo.getOperador())) {
                                where += "(:index_" + index.getAndAdd(1) + ")";
                            } else {
                                where += ":index_" + index.getAndAdd(1);
                            }
                        }
                        juncaoCampo = parametro.getJuncao();
                    }
                    where += " ) ";
                    juncao = " and ";
                }
            }
            if (!Strings.isNullOrEmpty(where)) {
                sqlBuilder.append(where);
            }
        }
        return parameters;
    }
}
