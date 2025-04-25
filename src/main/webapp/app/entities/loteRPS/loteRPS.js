(function () {
    'use strict';

angular.module('nfseApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('loteRPS', {
                parent: 'entity',
                url: '/loteRPSs',
                data: {
                    roles: ['ROLE_RPS_LOG_LOTES'],
                    pageTitle: 'Log do RPS'
                },
                ncyBreadcrumb: {
                    label: 'Consulta de Logs de Importação de RPS'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/loteRPS/loteRPSs.html',
                        controller: 'LoteRPSController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('loteRPS');
                        $translatePartialLoader.addPart('global');
                        $translatePartialLoader.addPart('situacaoLoteRps');
                        return $translate.refresh();
                    }]
                }
            })
            .state('loteRPS.detail', {
                parent: 'entity',
                url: '/loteRPS/{id}',
                data: {
                    roles: ['ROLE_RPS_LOG_LOTES'],
                    pageTitle: 'Log do RPS'
                },
                ncyBreadcrumb: {
                    label: 'Detalhes da Importação do RPS'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/loteRPS/loteRPS-detail.html',
                        controller: 'LoteRPSDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('loteRPS');
                        $translatePartialLoader.addPart('global');
                        $translatePartialLoader.addPart('situacaoLoteRps');

                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'LoteRPS', function($stateParams, LoteRPS) {
                        return LoteRPS.get({id : $stateParams.id}).$promise;
                    }]
                }
            })
            .state('loteRPS.new', {
                parent: 'loteRPS',
                url: '/new',
                data: {
                    roles: ['ROLE_RPS_LOG_LOTES'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'app/entities/loteRPS/loteRPS-dialog.html',
                        controller: 'LoteRPSDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {codigo: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('loteRPS', null, { reload: true });
                    }, function() {
                        $state.go('loteRPS');
                    })
                }]
            })
            .state('loteRPS.edit', {
                parent: 'loteRPS',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_RPS_LOG_LOTES'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'app/entities/loteRPS/loteRPS-dialog.html',
                        controller: 'LoteRPSDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['LoteRPS', function(LoteRPS) {
                                return LoteRPS.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('loteRPS', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
})();