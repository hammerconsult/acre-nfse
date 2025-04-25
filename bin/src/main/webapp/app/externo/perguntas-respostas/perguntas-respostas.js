(function () {
    'use strict';

angular.module('nfseApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('perguntas-respostas', {
                parent: 'out',
                url: '/perguntas-respostas',
                ncyBreadcrumb: {
                    label: 'Perguntas e Respostas'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/externo/perguntas-respostas/perguntas-respostas.html',
                        controller: 'PerguntasRespostasController'
                    }
                },
                resolve: {}
            });
    });
})();
