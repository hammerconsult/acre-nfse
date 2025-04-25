(function () {
    'use strict';

angular.module('nfseApp')
    .factory('Pessoa', function ($resource, DateUtils) {
        return $resource('api/pessoas/:id', {}, {
            'query': {method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'getPorCpfCnpj': {
                method: 'GET',
                url: "api/pessoa_por_cpfCnpj/:cpfCnpj",
                transformResponse: function (data) {
                    if (data)
                        data = angular.fromJson(data);
                    return data;
                }
            },
            'getPessoaDoUsuario': {
                method: 'GET',
                url: "api/pessoa_do_usuario",
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'getPessoaDoLogin': {
                method: 'GET',
                url: "api/pessoa_do_usuario/login/:login",
            },
            'update': {method: 'PUT'}
        });
    });
})();