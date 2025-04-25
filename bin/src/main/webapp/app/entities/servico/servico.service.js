(function () {
    'use strict';

angular.module('nfseApp')
    .factory('Servico', function ($resource) {
        return $resource('api/servico/:id', {}, {
            'consultar': {
                method: 'POST',
                url: 'api/servico/consultar',
                isArray: true
            },
            'getPorCodigo': {
                method: 'GET',
                url: "api/servico/por-codigo"
            },
            'queryPorPrestador': {
                method: 'GET',
                url: '/api/servico/por-prestador',
                isArray: true
            },
        });
    });
})();