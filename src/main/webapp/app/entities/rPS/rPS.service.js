(function () {
    'use strict';

angular.module('nfseApp')
    .factory('RPS', function ($resource, DateUtils) {
        return $resource('api/rps/:id', {}, {
            'query': {method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'buscarRps': {
                method: 'POST',
                url: 'api/buscar-rps',
                isArray: true
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    return angular.toJson(data);
                }
            },
            'autenticar': {
                url: 'api/rps-por-cnpj-numero-serie',
                method: 'GET',
                transformRequest: function (data) {
                    return angular.toJson(data);
                },
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'importar': {
                url: 'api/rps/importar-xml',
                method: 'POST'
            }
        });
    });
})();
