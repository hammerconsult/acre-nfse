(function () {
    'use strict';

angular.module('nfseApp')
    .factory('Pais', function ($resource, DateUtils) {
        return $resource('api/paises/:id', {}, {
            'query': {method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'getByCodigo': {
                url: 'api/paises_por_codigo/:codigo',
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': {method: 'PUT'},
        });
    });
})();