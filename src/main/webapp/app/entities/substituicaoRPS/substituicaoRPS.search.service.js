(function () {
    'use strict';

angular.module('nfseApp')
    .factory('SubstituicaoRPSSearch', function ($resource) {
        return $resource('api/_search/substituicaoRPSs/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
})();
