(function () {
    'use strict';

angular.module('nfseApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('integracaoNotaNacional', {
                parent: 'entity',
                url: '/integracaoNotaNacional',
                data: {
                    roles: ['ROLE_ADMIN'],
                    pageTitle: 'Integração com a Nfs-e Nacional'
                },
                ncyBreadcrumb: {
                    label: 'Consulta de Integração com a Nfs-e Nacional'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/nota-nacional/integracao-nota-nacional.html',
                        controller: 'IntegracaoNotaNacionalController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('global');
                        $translatePartialLoader.addPart('statusProcessamento');
                        return $translate.refresh();
                    }]
                }
            });
    });
})();
