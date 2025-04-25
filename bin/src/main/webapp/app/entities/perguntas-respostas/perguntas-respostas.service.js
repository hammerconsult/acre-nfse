(function () {
    'use strict';

angular.module('nfseApp')
    .factory('PerguntasRespostasService', function ($resource) {
        return $resource('api/externo/perguntas-respostas/:id', {}, {
            'perguntasRespostasParaExibicao': {
                url: 'api/externo/perguntas-repostas-para-exibicao',
                method: 'GET',
                isArray: true
            }
        });
    });
})();