(function () {
    'use strict';

angular.module('nfseApp')
    .factory('Configuracao', function ($resource, DateUtils) {
        return $resource('api/configuracao/', {}, {
            'query': {method: 'GET', isArray: true},
            'get': {
                url: 'publico/configuracao/',
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'getPerfil': {
                url: 'publico/perfil-app/',
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': {method: 'PUT'}
        });
    });
})();