(function () {
    'use strict';

angular.module('nfseApp')
    .factory('ConfiguracaoSearch', function ($resource) {
        return $resource('api/_search/configuracaos/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
})();