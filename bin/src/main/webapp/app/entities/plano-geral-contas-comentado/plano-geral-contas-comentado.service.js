(function () {
    'use strict';

    angular.module('nfseApp')
        .factory('PlanoGeralContasComentado', function ($resource, DateUtils) {
            return $resource('api/plano-geral-contas-comentado/:id', {}, {
                'get': {
                    url: 'api/plano-geral-contas-comentado/:id',
                    method: 'GET',
                    transformResponse: function (data) {
                        data = angular.fromJson(data);
                        data.inicioVigencia = DateUtils.convertDateLong(data.inicioVigencia);
                        data.fimVigencia = DateUtils.convertDateLong(data.fimVigencia);
                        if (data.tarifaBancaria) {
                            data.tarifaBancaria.inicioVigencia = DateUtils.convertDateLong(data.tarifaBancaria.inicioVigencia);
                        }
                        return data;
                    }
                },
                'consultar': {
                    url: 'api/plano-geral-contas-comentado/consultar',
                    method: 'POST',
                    isArray: true
                },
                'buscarPorConta': {
                    url: 'api/plano-geral-contas-comentado/buscar-por-conta',
                    method: 'GET'
                },
                'buscarContasTributaveisNaoDeclaradas': {
                    url: 'api/plano-geral-contas-comentado/buscar-contas-tributaveis-nao-declaradas',
                    method: 'GET',
                    isArray: true
                }
            });
        });
})();