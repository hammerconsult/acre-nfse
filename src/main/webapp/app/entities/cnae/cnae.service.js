(function () {
    'use strict';

angular.module('nfseApp')
    .factory('Cnae', function ($resource) {
        return $resource('api/cnaes/:id', {}, {
            'query': {method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'getPorCodigo': {
                method: 'GET',
                url: "api/cnae_por_codigo/:codigo",
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'getPorServicoCodigoDescricao': {
                method: 'GET',
                url: "api/cnae-por-servico/",
                isArray: true
            },
            'teByPrestador': {
                method: 'GET',
                url: "api/cnaes-por-empresa/:id",
                isArray: true
            },
            'update': {method: 'PUT'}
        });
    });
})();