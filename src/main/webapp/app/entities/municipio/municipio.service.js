(function () {
    'use strict';

angular.module('nfseApp')
    .factory('Municipio', function ($resource, DateUtils) {
        return $resource('api/municipios/:id', {}, {
            'query': {method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'getByCodigo': {
                url: 'api/municipios_por_codigo/:codigo',
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'getByNome': {
                url: 'api/municipio_por_descricao/:nome',
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': {method: 'PUT'},
            'search': {
                url: 'api/_search/municipios',
                method: 'GET',
                isArray: true
            }
        });
    });
})();