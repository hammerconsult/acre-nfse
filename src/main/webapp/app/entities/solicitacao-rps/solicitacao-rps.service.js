(function () {
    'use strict';

angular.module('nfseApp')
    .factory('Solicitacaorps', function ($resource) {
        return $resource('api/solicitacao-rps/:id', {}, {
            'query': {method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'queryPorSituacao': {
                url: 'api/solicitacao-rps/por-situacao',
                method: 'GET', isArray: true
            },
            'buscarTodasDoPrestador': {
                method: 'GET',
                url: 'api/solicitacao-rps/por-prestador',
                isArray: true
            },
            'update': {method: 'PUT'},
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    return angular.toJson(data);
                }
            },
        });
    });
})();
