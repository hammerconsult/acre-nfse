(function () {
    'use strict';

angular.module('nfseApp')
    .factory('SubstituicaoRPS', function ($resource, DateUtils) {
        return $resource('api/substituicaoRPSs/:id', {}, {
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
