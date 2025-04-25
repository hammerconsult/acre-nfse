(function () {
    'use strict';

angular.module('nfseApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('rPS', {
                parent: 'entity',
                url: '/rps',
                data: {
                    roles: ['ROLE_RPS_CONSULTAR'],
                    pageTitle: 'Recibo provisório de serviço'
                },
                ncyBreadcrumb: {
                    label: 'Consulta de RPS'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/rPS/rPSs.html',
                        controller: 'RPSController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('rPS');
                        $translatePartialLoader.addPart('global');
                        $translatePartialLoader.addPart('naturezaOperacao');
                        $translatePartialLoader.addPart('situacaoNota');
                        return $translate.refresh();
                    }]
                }
            })
            .state('rPS.detail', {
                parent: 'entity',
                url: '/rps/{id}',
                data: {
                    roles: ['ROLE_RPS_CONSULTAR'],
                    pageTitle: 'Recibo provisório de serviço'
                },
                ncyBreadcrumb: {
                    label: 'Detalhes do RPS'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/rPS/rPS-detail.html',
                        controller: 'RPSDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('rPS');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'RPS', function ($stateParams, RPS) {
                        return RPS.get({id: $stateParams.id});
                    }]
                }
            })
            .state('rPS.autenticar', {
                parent: 'entity',
                url: '/rps-autenticar',
                data: {
                    roles: [],
                    pageTitle: 'Consulte a Substituição de RPS em NFS-e'
                },
                ncyBreadcrumb: {
                    label: 'Autenticar RPS'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/rPS/autenticar-rps.html',
                        controller: 'AutenticarRpsController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('rPS');
                        return $translate.refresh();
                    }]
                }
            })
            .state('rPS.new', {
                parent: 'rPS',
                url: '/new',
                data: {
                    roles: ['ROLE_RPS_CONSULTAR'],
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/rPS/rPS-edit.html',
                        controller: 'RPSEditController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('rPS');
                        return $translate.refresh();
                    }],
                    entity: function () {
                        return {numero: null, serie: null, dataEmissao: null, id: null};
                    }
                }

            })
            .state('rPS.import', {
                parent: 'entity',
                url: '/importacao',
                data: {
                    roles: ['ROLE_RPS_IMPORTAR'],
                    pageTitle: 'Recibo provisório de serviço'
                },
                ncyBreadcrumb: {
                    label: 'Importação de RPS via xml'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/rPS/rPS-import.html',
                        controller: 'RPSImportController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('rPS');
                        return $translate.refresh();
                    }],
                    entity: function () {
                        return {numero: null, serie: null, dataEmissao: null, id: null};
                    }
                }

            })
            .state('rPS.edit', {
                parent: 'rPS',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_RPS_CONSULTAR'],
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/rPS/rPS-edit.html',
                        controller: 'RPSEditController',
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('rPS');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'RPS', function ($stateParams, RPS) {
                        return RPS.get({id: $stateParams.id});
                    }]
                }
            });
    });
})();