(function () {
    'use strict';

angular.module('nfseApp')
    .factory('AidfeSearch', function ($resource) {
        return $resource('api/_search/aidfes/:query', {}, {
            'query': {method: 'GET', isArray: true},
            'buscarPorSituacao': {
                method: 'POST',
                url: 'api/_search/aidfes/situacao',
                isArray: true
            },
            'buscarAIDFEsPeloPrestador': {
                method: 'GET',
                url: 'api/_search/aidfes/prestador/:query',
                isArray: true
            }
        });
    });
})();