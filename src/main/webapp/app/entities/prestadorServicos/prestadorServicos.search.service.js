(function () {
    'use strict';

angular.module('nfseApp')
    .factory('PrestadorServicosSearch', function ($resource) {
        return $resource('api/_search/prestadorServicos/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
})();