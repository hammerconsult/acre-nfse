(function () {
    'use strict';

    angular.module('nfseApp')
        .factory('TarifaBancaria', function ($resource) {
            return $resource('api/tarifa-bancaria/:id', {}, {
                'consultar': {
                    url: 'api/tarifa-bancaria/consultar',
                    method: 'POST',
                    isArray: true
                },
                'buscarPorCodigo': {
                    method: 'GET',
                    url: 'api/tarifa-bancaria/buscar-por-codigo'
                }
            });
        });
})();