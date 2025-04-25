(function () {
    'use strict';

angular.module('nfseApp')
    .factory('DeclaracaoMensalServico', function ($resource, DateUtils) {
        return $resource('api/declaracaoMensalServicos/:id', {}, {
            'query': {method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.competencia = DateUtils.convertDateTimeFromServer(data.competencia);
                    return data;
                }
            },
            'getMeses': {
                url: 'api/declaracaoMensalServicos/meses',
                method: 'GET',
                isArray: true
            },
            'getByCalculoId': {
                url: 'api/dms-by-calculo',
                method: 'GET'
            },
            'getMesesNaoDeclarados': {
                url: 'api/declaracaoMensalServicos/meses_nao_declarados',
                method: 'GET',
                isArray: true
            },
            'getResumoPorDms': {
                url: 'api/resumo-por-dms',
                method: 'GET'
            },
            'getNotasDeclaradasPorDMS': {
                url: '/api/nota_declarada_por_dms/:id',
                method: 'GET',
                isArray: true
            },
            'update': {method: 'PUT'},
            'imprimirDam': {
                method: 'GET',
                url: "/api/imprimir-dam/:id",
                headers: {
                    accept: 'application/pdf'
                },
                responseType: 'arraybuffer',
                cache: true,
            },
            'salvarInstituicaoFinanceira': {
                method: 'POST',
                url: 'api/declaracaoMensalServicosInstituicaoFinanceira',
                transformRequest: function (data) {
                    data.competencia = DateUtils.convertLocaleDateToServer(data.competencia);
                    return angular.toJson(data);
                }
            },
            'cancelar': {
                url: '/api/cancelar-declaracao-mensal-servico/:id',
                method: 'GET'
            },
            'importarDesif': {
                url: '/api/importar-desif',
                method: 'POST'
            },
            'consultar': {
                method: 'POST',
                url: '/api/declaracaoMensalServicos/consultar',
                isArray: true,
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    if (data)
                        for (var i = 0; i < data.length; i++) {
                            var dms = data[i];
                            dms.competencia = DateUtils.convertLocaleDateToServer(dms.competencia);
                            dms.abertura = DateUtils.convertLocaleDateToServer(dms.abertura);
                            dms.encerramento = DateUtils.convertLocaleDateToServer(dms.encerramento);
                            dms.vencimento = DateUtils.convertLocaleDateToServer(dms.vencimento);
                        }
                    return data;
                }
            }
        });
    });
})();
