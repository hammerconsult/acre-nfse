(function () {
    'use strict';

angular.module('nfseApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('debitos', {
                parent: 'entity',
                url: '/consutar-debitos',
                data: {
                    roles: ['ROLE_CONTA_CORRENTE_DEBITOS'],
                    pageTitle: 'Conta Corrente'
                },
                ncyBreadcrumb: {
                    label: 'Consulta de Débitos'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/debitos/debitos.html',
                        controller: 'DebitosController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('global');
                        $translatePartialLoader.addPart('situacaoParcela');
                        return $translate.refresh();
                    }]
                }
            })
            .state('debitos.details', {
                parent: 'entity',
                url: '/consutar-debitos/{id}',
                data: {
                    roles: ['ROLE_CONTA_CORRENTE_DEBITOS'],
                    pageTitle: 'Conta Corrente Detalhes'
                },
                ncyBreadcrumb: {
                    label: 'Consulta de Débitos Detalhes'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/debitos/debitos-detail.html',
                        controller: 'DebitosDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('global');
                        $translatePartialLoader.addPart('situacaoParcela');
                        $translatePartialLoader.addPart('situacaoNota');
                        return $translate.refresh();
                    }],
                    debito: ['$stateParams', 'PrestadorServicos', function ($stateParams, PrestadorServicos) {
                        return PrestadorServicos.getDebito({idParcela: $stateParams.id},
                            function (data) {
                                return data;
                            }).$promise;
                    }]
                }
            })
            .state('guia', {
                parent: 'entity',
                url: '/nova-guia/',
                data: {
                    roles: ['ROLE_CONTA_CORRENTE_GERAR_GUIA'],
                    pageTitle: 'Gerar Guia'
                },
                ncyBreadcrumb: {
                    label: 'Nova Guia'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/debitos/guia.html',
                        controller: 'GuiaController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('declaracaoMensalServico');
                        $translatePartialLoader.addPart('global');
                        $translatePartialLoader.addPart('tipoDocumento');
                        $translatePartialLoader.addPart('tipoMovimentoMensal');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'localStorageService', function ($stateParams, localStorageService) {
                        return {
                            id: null,
                            codigo: null,
                            competencia: null,
                            tipo: null,
                            tipoMovimento: $stateParams.tipoMovimento ? $stateParams.tipoMovimento : 'NORMAL',
                            prestador: localStorageService.get("prestadorPrincipal"),
                            notas: [],
                            mes: new Date().getMonth() + 1,
                            exercicio: new Date().getFullYear(),
                        };
                    }],
                    idDeclaracao: ['$stateParams', 'localStorageService', function ($stateParams) {
                        return $stateParams.idDeclaracao;
                    }]
                }
            })
            .state('guiaUnica', {
                parent: 'entity',
                url: '/nova-guia/{mes}/{ano}/{idDeclaracao}/{tipoMovimento}',
                data: {
                    roles: ['ROLE_CONTA_CORRENTE_GERAR_GUIA'],
                    pageTitle: 'Gerar Guia'
                },
                ncyBreadcrumb: {
                    label: 'Nova Guia'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/debitos/guia.html',
                        controller: 'GuiaController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('declaracaoMensalServico');
                        $translatePartialLoader.addPart('global');
                        $translatePartialLoader.addPart('tipoDocumento');
                        $translatePartialLoader.addPart('tipoMovimentoMensal');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'localStorageService', function ($stateParams, localStorageService) {
                        return {
                            id: null,
                            codigo: null,
                            competencia: null,
                            tipo: null,
                            tipoMovimento: $stateParams.tipoMovimento ? $stateParams.tipoMovimento : 'NORMAL',
                            prestador: localStorageService.get("prestadorPrincipal"),
                            notas: [],
                            mes: $stateParams.mes,
                            exercicio: $stateParams.ano,
                        };
                    }],
                    idDeclaracao: ['$stateParams', 'localStorageService', function ($stateParams) {
                        return $stateParams.idDeclaracao;
                    }]
                }
            });
    });
})();