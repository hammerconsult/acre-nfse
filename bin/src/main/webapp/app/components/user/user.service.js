(function () {
    'use strict';

angular.module('nfseApp')
    .factory('User', function ($resource) {
        return $resource('api/users/:login', {}, {
            'query': {method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': {method: 'PUT'},
            'acrescentarTentativaAcesso': {
                url: 'api/users-acrescentar-tentativa-login/:login',
                method: 'GET',
            },
            'zerarTentativaAcesso': {
                url: 'api/users-zerar-tentativa-login/:login',
                method: 'GET',
            },
            'fromLogin': {
                url: 'api/user-from-login',
                method: 'GET'
            },
            'fromResetKey': {
                url: 'api/user-from-reset-key',
                method: 'GET'
            }
        });
    });
})();