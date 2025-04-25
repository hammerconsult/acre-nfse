(function () {
    'use strict';

    angular.module('nfseApp')
        .factory('ProdutoServico', function ($resource) {
            return $resource('api/produto-servico/:id', {}, {
                'consultar': {
                    url: 'api/produto-servico/consultar',
                    method: 'POST',
                    isArray: true
                },
                'buscarPorCodigo': {
                    method: 'GET',
                    url: 'api/produto-servico/buscar-por-codigo'
                }
            });
        });
})();