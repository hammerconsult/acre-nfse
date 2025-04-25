(function () {
    'use strict';

    angular.module('nfseApp')
        .factory('NotaNacional', function ($resource) {
            return $resource('api/nota-nacional/:id', {}, {
                'integrar': {
                    url: 'api/nota-nacional/integrar',
                    method: 'GET'
                },
                'buscarRetornosDfeAdn': {
                    url: 'api/nota-nacional/retornos-dfe-adn',
                    method: 'GET',
                    isArray: true
                },
                'buscarMensagensDfeAd': {
                    url: 'api/nota-nacional/mensagens-dfe-adn',
                    method: 'GET',
                    isArray: true
                },
                'buscarIntegracoes': {
                    method: 'POST',
                    url: 'api/nota-nacional/buscar-integracoes',
                    isArray: true
                },
                'buscarQuantidadePorErro': {
                    method: 'GET',
                    url: 'api/nota-nacional/buscar-quantidade-por-erro',
                    isArray: true
                },
                'buscarQuantidadePorStatus': {
                    method: 'GET',
                    url: 'api/nota-nacional/buscar-quantidade-por-status',
                    isArray: true
                },
                'reintegrarNotasPorCodigoErro': {
                    url: 'api/nota-nacional/reintegrar-notas-por-codigo-erro',
                    method: 'GET'
                },
            });
        });
})();
