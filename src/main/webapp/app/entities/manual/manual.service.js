(function () {
    'use strict';

angular.module('nfseApp')
    .factory('ManualService', function ($resource) {
        return $resource('api/externo/manual/:id', {}, {
            'manuaisParaExibicao': {
                method: 'GET',
                url: "api/externo/manuais-para-exibicao",
                isArray: true

            },
            'manuaisPorTag': {
                method: 'GET',
                url: "api/externo/manuais-por-tag",
                isArray: true

            },
            'manuaisPorTipo': {
                method: 'GET',
                url: "api/externo/manuais-por-tipo",
                isArray: true
            }
        });
    });
})();