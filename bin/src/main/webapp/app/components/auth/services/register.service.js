(function () {
    'use strict';

angular.module('nfseApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
            'autorizarUsuario': {
                method: 'POST',
                url: 'api/prestador-servico/vincular-usuario'
            }
        });
    });


})();