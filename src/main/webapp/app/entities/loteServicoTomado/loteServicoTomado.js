(function () {
    'use strict';

angular.module('nfseApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('loteServicoTomado', {
                parent: 'entity',
                url: '/lote-servicos-tomados',
                data: {
                    roles: ['ROLE_CONTRIBUINTE'],
                    pageTitle: 'Log do ARQUIVO'
                },
                ncyBreadcrumb: {
                    label: 'Consulta de Logs de Importação de Documentos Recebidos'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/loteServicoTomado/loteServicoTomados.html',
                        controller: 'LoteServicoTomadoController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('loteServicoTomado');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('loteServicoTomado.detail', {
                parent: 'entity',
                url: '/lote-servicos-tomados/{id}',
                data: {
                    roles: ['ROLE_CONTRIBUINTE'],
                    pageTitle: 'Log do ARQUIVO'
                },
                ncyBreadcrumb: {
                    label: 'Detalhes da Importação do Lote'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/loteServicoTomado/loteServicoTomado-detail.html',
                        controller: 'LoteServicoTomadoDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('loteServicoTomado');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'LoteServicoTomado', function ($stateParams, LoteServicoTomado) {
                        return LoteServicoTomado.get({id: $stateParams.id}).$promise;
                    }]
                }
            })
            .state('loteServicoTomado.new', {
                parent: 'loteServicoTomado',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function ($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'app/entities/loteServicoTomado/loteServicoTomado-dialog.html',
                        controller: 'LoteServicoTomadoDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {codigo: null, id: null};
                            }
                        }
                    }).result.then(function (result) {
                        $state.go('loteServicoTomado', null, {reload: true});
                    }, function () {
                        $state.go('loteServicoTomado');
                    })
                }]
            })
            .state('loteServicoTomado.edit', {
                parent: 'loteServicoTomado',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function ($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'app/entities/loteServicoTomado/loteServicoTomado-dialog.html',
                        controller: 'LoteServicoTomadoDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['LoteServicoTomado', function (LoteServicoTomado) {
                                return LoteServicoTomado.get({id: $stateParams.id});
                            }]
                        }
                    }).result.then(function (result) {
                        $state.go('loteServicoTomado', null, {reload: true});
                    }, function () {
                        $state.go('^');
                    })
                }]
            });
    });
})();