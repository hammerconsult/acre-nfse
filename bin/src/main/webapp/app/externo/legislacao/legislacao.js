(function () {
    'use strict';

angular.module('nfseApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('legislacao', {
                parent: 'external',
                url: '/legislacoes',
                ncyBreadcrumb: {
                    label: 'Legislações'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/externo/legislacao/legislacao.html',
                        controller: 'LegislacaoController'
                    }
                },
                resolve: {

                }
            });
    });
})();
