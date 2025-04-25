(function () {
    'use strict';

    angular.module('nfseApp')
        .factory('Cosif', function ($resource) {
            return $resource('api/cosif/:id', {}, {
                'consultar': {
                    url: 'api/cosif/consultar',
                    method: 'POST',
                    isArray: true
                },
                'buscarPorConta': {
                    method: 'GET',
                    url: 'api/cosif/buscar-por-conta'
                }
            });
        });
})();