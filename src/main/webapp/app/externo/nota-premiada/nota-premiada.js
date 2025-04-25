(function () {
    'use strict';

angular.module('nfseApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('nota-premiada', {
                parent: 'out',
                url: '/notas-premiadas',
                ncyBreadcrumb: {
                    label: 'Nota Premiada'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/externo/nota-premiada/nota-premiada.html',
                        controller: 'NotaPremiadaController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            });
    });
})();
