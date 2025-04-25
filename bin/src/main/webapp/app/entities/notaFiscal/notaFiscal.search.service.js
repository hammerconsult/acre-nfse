(function () {
    'use strict';

angular.module('nfseApp')
    .factory('NotaFiscalSearch', function ($resource) {
        return $resource('api/_search/notaFiscals/:query', {}, {
            'query': {method: 'GET', isArray: true},
            'searchNotasTomador': {
                url: "/api/_search/notaFiscals/tomador/:query",
                method: 'GET',
                isArray: true
            }
        });
    });
})();