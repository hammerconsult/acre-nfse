(function () {
    'use strict';

angular.module('nfseApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('loteRpsGeral', {
                parent: 'entity',
                url: '/loteRpsGeral',
                data: {
                    roles: ['ROLE_ADMIN'],
                    pageTitle: 'Lotes de RPS'
                },
                ncyBreadcrumb: {
                    label: 'Consulta de Lotes de RPS'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/loterpsgeral/loteRpsGeral.html',
                        controller: 'LoteRpsGeralController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('global');
                        $translatePartialLoader.addPart('situacaoLoteRps');
                        return $translate.refresh();
                    }]
                }
            })
            .state('loteRpsGeral.detail', {
                parent: 'entity',
                url: '/loteRpsGeral/{id}',
                data: {
                    roles: ['ROLE_ADMIN'],
                    pageTitle: 'Lote de RPS'
                },
                ncyBreadcrumb: {
                    label: 'Detalhes do Lote de RPS'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/loterpsgeral/loteRpsGeral-detail.html',
                        controller: 'LoteRpsGeralDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('global');
                        $translatePartialLoader.addPart('situacaoLoteRps');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'LoteRPS', function($stateParams, LoteRPS) {
                        return LoteRPS.get({id : $stateParams.id}).$promise;
                    }]
                }
            });
    });
})();
