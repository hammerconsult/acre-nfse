(function () {
    'use strict';

    angular.module('nfseApp')
        .factory('CodigoTributacaoMunicipio', function ($resource) {
            return $resource('api/codigo-tributacao-municipio/:id', {}, {
                'buscarPorIdCodigoTributacao': {
                    method: 'GET',
                    url: 'api/codigo-tributacao-municipio/buscar-por-id-codigo-tributacao'
                }
            });
        });
})();
