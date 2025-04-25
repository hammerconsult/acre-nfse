(function () {
    'use strict';

angular.module('nfseApp')
    .factory('ServicoDeclarado', function ($resource, DateUtils) {
        return $resource('api/servico-declarado/:id', {}, {
            'query': {method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
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
            'new': {
                method: 'GET',
                url: 'api/servico-declarado/new',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'cancelar': {
                method: 'POST',
                url: 'api/servico-declarado/cancelar',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;

                }

            },
            'importar': {
                url: 'api/servico-declarado/importar-xml',
                method: 'POST',
                transformRequest: function (data) {
                    return angular.toJson(data);
                }
            },
            'getAllTiposDocumentosServicoDeclarado': {
                url: '/api/servico-declarado/tipos-documentos-servico-declarado',
                method: 'GET',
                isArray: true
            },
            'buscarServicosDeclarados': {
                method: 'POST',
                url: 'api/servico-declarado/consultar',
                isArray: true
            },
            'buscarParcelas': {
                method: 'GET',
                url: 'api/servico-declarado/parcelas/:id',
                isArray: true
            }
        });
    });
})();
