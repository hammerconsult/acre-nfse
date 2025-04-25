(function () {
    'use strict';

angular.module('nfseApp')
    .factory('TipoLegislacaoService', function ($resource) {
        return $resource('api/tipo-legislacao/:id', {}, {
            'tiposLegislacaoWithlegislacoes': {
                method: 'GET',
                url: "api/tipo-legislacao-com-legislacoes",
                isArray: true

            }
        });
    });
})();
