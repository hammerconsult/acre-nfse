(function () {
    'use strict';

    angular.module('nfseApp')
        .config(function ($stateProvider) {
            $stateProvider
                .state('arquivoDesif', {
                    parent: 'entity',
                    url: '/arquivo-desif/consultar',
                    data: {
                        roles: ['ROLE_ENCERRAMENTO_MENSAL_INSTITUICAO_FINANCEIRA'],
                        pageTitle: 'Consulta de Importação de Arquivo DES-IF'
                    },
                    ncyBreadcrumb: {
                        label: 'Consulta de Importação de Arquivo DES-IF'
                    },
                    views: {
                        'content@': {
                            templateUrl: 'app/entities/arquivo-desif/arquivo-desif.html',
                            controller: 'ArquivoDesifController'
                        }
                    },
                    resolve: {
                        translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                            $translatePartialLoader.addPart('global');
                            $translatePartialLoader.addPart('moduloArquivoDesif');
                            $translatePartialLoader.addPart('situacaoArquivoDesif');
                            $translatePartialLoader.addPart('tipoArquivoDesif');
                            return $translate.refresh();
                        }]
                    }
                })
                .state('arquivoDesif.validar', {
                    parent: 'arquivoDesif',
                    url: '/arquivo-desif/validar',
                    data: {
                        roles: ['ROLE_ENCERRAMENTO_MENSAL_INSTITUICAO_FINANCEIRA'],
                        pageTitle: 'Validar Importação de Arquivo DES-IF'
                    },
                    ncyBreadcrumb: {
                        label: 'Validar Importação de Arquivo DES-IF'
                    },
                    views: {
                        'content@': {
                            templateUrl: 'app/entities/arquivo-desif/arquivo-desif-validate.html',
                            controller: 'ArquivoDesifValidateController'
                        }
                    },
                    resolve: {
                        translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                            $translatePartialLoader.addPart('global');
                            return $translate.refresh();
                        }]
                    }
                })
                .state('arquivoDesif.detalhe', {
                    parent: 'arquivoDesif',
                    url: '/arquivo-desif/detalhe/:id',
                    data: {
                        roles: ['ROLE_ENCERRAMENTO_MENSAL_INSTITUICAO_FINANCEIRA'],
                        pageTitle: 'Detalhe da Importação de Arquivo DES-IF'
                    },
                    ncyBreadcrumb: {
                        label: 'Detalhe da Importação de Arquivo DES-IF'
                    },
                    views: {
                        'content@': {
                            templateUrl: 'app/entities/arquivo-desif/arquivo-desif-detail.html',
                            controller: 'ArquivoDesifDetailController'
                        }
                    },
                    resolve: {
                        translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                            $translatePartialLoader.addPart('global');
                            $translatePartialLoader.addPart('moduloArquivoDesif');
                            $translatePartialLoader.addPart('situacaoArquivoDesif');
                            $translatePartialLoader.addPart('tipoArquivoDesif');
                            $translatePartialLoader.addPart('tipoConsolidacaoArquivoDesif');
                            $translatePartialLoader.addPart('tipoArredondamentoArquivoDesif');
                            $translatePartialLoader.addPart('tipoPartidaArquivoDesif');
                            $translatePartialLoader.addPart('identificacaoDependenciaDesif');
                            return $translate.refresh();
                        }],
                        arquivo: ['$stateParams', 'ArquivoDesif', function ($stateParams, ArquivoDesif) {
                            return ArquivoDesif.get({id: $stateParams.id},
                                function (sucess) {
                                    return sucess;
                                }, function (error) {
                                    $state.go('arquivoDesif');
                                }).$promise;
                        }]
                    }
                });
        });
})();
