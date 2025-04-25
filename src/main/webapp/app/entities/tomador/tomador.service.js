(function () {
    'use strict';

angular.module('nfseApp')
    .factory('Tomador', function ($resource, DateUtils) {
        return $resource('api/tomadors/:id', {}, {
            'query': {
                url: 'api/tomadors/consultar',
                method: 'POST',
                isArray: true
            },
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'getInPessoRepoByCpfCnpj': {
                method: 'GET',
                url: "api/tomador_pessoa_repo/:cpfCnpj",
                transformResponse: function (data) {
                    if (data)
                        data = angular.fromJson(data);
                    return data;
                }
            },
            'getTomadorPorCpfCnpj': {
                method: 'GET',
                url: "api/tomador_por_cpfCnpj/:cpfCnpj",
                transformResponse: function (data) {
                    if (data)
                        data = angular.fromJson(data);
                    return data;
                }
            }
        });
    });
})();
