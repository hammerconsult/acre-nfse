(function () {
    'use strict';

angular.module('nfseApp')
    .factory('PaisSearch', function ($resource) {
        return $resource('api/_search/paises/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
})();