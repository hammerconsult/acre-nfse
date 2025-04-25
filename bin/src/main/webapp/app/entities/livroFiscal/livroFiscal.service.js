(function () {
    'use strict';

angular.module('nfseApp')
    .factory('LivroFiscal', function ($resource, DateUtils) {
        return $resource('api/livroFiscal/:id', {}, {
            'query': {method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.competencia = DateUtils.convertDateTimeFromServer(data.competencia);
                    return data;
                }
            },
            'update': {method: 'PUT'},
            'competenciasLivroFiscal': {
                method: 'POST',
                url: 'api/competencias-livro-fiscal',
                isArray: true
            },
            'imprimirLivroFiscal': {
                method: 'POST',
                url: 'api/imprimir-livro-fiscal'
            }
        });
    });
})();