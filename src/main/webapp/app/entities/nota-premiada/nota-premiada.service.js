(function () {
    'use strict';

angular.module('nfseApp')
    .factory('NotaPremiadaService', function ($resource) {
        return $resource('api/externo/nota-premiada/:id', {}, {
            'notaPremiadaParaExibicao': {
                method: 'GET',
                url: "api/externo/nota-premiada-para-exibicao",
                isArray: true
            },
            'buscarPremios': {
                url: 'api/externo/nota-premiada-premios/:id',
                method: 'GET',
                isArray: true
            },
            'buscarBilhetes': {
                url: 'api/externo/nota-premiada-bilhetes',
                method: 'GET',
                isArray: true
            },
            'buscarBilhetesPremiados': {
                url: 'api/externo/nota-premiada-bilhetes-premiados/:id',
                method: 'GET',
                isArray: true
            },
            'buscarTodosPremios': {
                url: 'api/externo/nota-premiada-premios',
                method: 'GET',
                isArray: true
            },
            'anularParticipacaoCampanha': {
                url: 'api/externo/anular-participacao-campanha',
                method: 'GET',
                isArray: true
            }
        });
    });
})();