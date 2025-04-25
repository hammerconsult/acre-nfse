(function () {
    'use strict';

    angular.module('nfseApp')
        .factory('CodigoTributacao', function ($resource) {
            return $resource('api/codigo-tributacao/:id', {}, {
                'buscarPorCodigo': {
                    method: 'GET',
                    url: 'api/codigo-tributacao/buscar-por-codigo'
                },
                'consultar': {
                    url: 'api/codigo-tributacao/consultar',
                    method: 'POST',
                    isArray: true
                }
            });
        });
})();
