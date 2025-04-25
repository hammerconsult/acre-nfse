(function () {
    'use strict';

    angular.module('nfseApp')
        .factory('DocumentoFiscal', function ($resource) {
            return $resource('api/documento-fiscal/:id', {}, {
                'query': {method: 'GET', isArray: true},
                'consultar': {
                    method: 'POST',
                    url: 'api/documento-fiscal/consultar',
                    isArray: true
                }
            });
        })
    ;
})();
