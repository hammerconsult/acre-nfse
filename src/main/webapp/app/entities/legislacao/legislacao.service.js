(function () {
    'use strict';

angular.module('nfseApp')
    .factory('LegislacaoService', function ($resource) {
        return $resource('api/externo/legislacao/:id', {}, {
            'legislacoesParaExibicao': {
                method: 'GET',
                url: "api/externo/legislacoes-para-exibicao",
                isArray: true

            }
        });
    });
})();