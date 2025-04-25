(function () {
    'use strict';

angular.module('nfseApp')
    .factory('CnaeSearch', function ($resource) {
        return $resource('api/_search/cnaes/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
})();