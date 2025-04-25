(function () {
    'use strict';

angular.module('nfseApp')
    .factory('LoteServicoTomado', function ($resource, DateUtils) {
        return $resource('api/lote-declaracao-servico-tomado/:id', {}, {
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