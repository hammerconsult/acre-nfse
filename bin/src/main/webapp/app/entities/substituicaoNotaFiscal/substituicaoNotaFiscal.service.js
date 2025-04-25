(function () {
    'use strict';

angular.module('nfseApp')
    .factory('SubstituicaoNotaFiscal', function ($resource, DateUtils) {
        return $resource('api/substituicaoNotaFiscals/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
})();
