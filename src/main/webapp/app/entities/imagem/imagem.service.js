(function () {
    'use strict';

angular.module('nfseApp')
    .factory('Imagem', function ($resource) {
        return $resource('api/imagem/:id', {}, {
            'query': {method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'updateImagem': {
                method: 'PUT',
                url: "api/imagem"
            },

            'getImageFromPessoa': {
                method: 'GET',
                url: "api/imagem-pessoa/:id",
                transformResponse: function (data) {
                    if (data)
                        data = angular.fromJson(data);
                    return data;
                }
            },
            'getImageFromPrestador': {
                method: 'GET',
                url: "api/imagem-prestador/:id",
                transformResponse: function (data) {
                    if (data)
                        data = angular.fromJson(data);
                    return data;
                }
            },
            'deleteImagemPrestador': {
                method: 'DELETE',
                url: "api/imagem-prestador/:id",
            },
            'updateImagemUsuario': {
                method: 'POST',
                url: "api/imagem-usuario"
            },
            'getImageFromUsuario': {
                method: 'GET',
                url: "api/imagem-usuario",
                transformResponse: function (data) {
                    if (data)
                        data = angular.fromJson(data);
                    return data;
                }
            },
            'deleteImagemUsuario': {
                method: 'DELETE',
                url: "api/imagem-usuario/:id",
            },

            'update': {method: 'PUT'}
        });
    });
})();