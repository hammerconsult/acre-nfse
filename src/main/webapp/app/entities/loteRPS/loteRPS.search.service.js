(function () {
    'use strict';

angular.module('nfseApp')
    .factory('LoteRPSSearch', function ($resource) {
        return $resource('api/_search/loteRPSs/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
})();