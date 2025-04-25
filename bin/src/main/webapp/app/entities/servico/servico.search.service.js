(function () {
    'use strict';

angular.module('nfseApp')
    .factory('ServicoSearch', function ($resource) {
        return $resource('api/_search/servicos/:query', {}, {
            'query': {method: 'GET', isArray: true},
            'queryByPrestador': {method: 'GET', isArray: true, url: '/api/servicos-prestador'},
            'queryByPrestadorAndCnae': {method: 'GET', isArray: true, url: '/api/servicos-prestador-cnae'}
        });
    });
})();