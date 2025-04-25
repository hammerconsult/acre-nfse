(function () {
    'use strict';

angular.module('nfseApp')
    .factory('SubstituicaoNotaFiscalSearch', function ($resource) {
        return $resource('api/_search/substituicaoNotaFiscals/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
})();
