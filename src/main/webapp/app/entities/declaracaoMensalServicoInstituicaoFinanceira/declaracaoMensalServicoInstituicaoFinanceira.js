(function () {
    'use strict';

angular.module('nfseApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('declaracaoMensalServicoInstituicaoFinanceira', {
                parent: 'entity',
                url: '/dms-if',
                data: {
                    roles: ['ROLE_ENCERRAMENTO_MENSAL_INSTITUICAO_FINANCEIRA'],
                    pageTitle: 'Declaração Mensal de Serviços de Instituições Financeiras'
                },
                ncyBreadcrumb: {
                    label: 'Consulta DMS-IF'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/declaracaoMensalServicoInstituicaoFinanceira/declaracaoMensalServicosInstituicaoFinanceira.html',
                        controller: 'DeclaracaoMensalServicoInstituicaoFinanceiraController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('declaracaoMensalServico');
                        $translatePartialLoader.addPart('situacaoDeclaracaoMensal');
                        $translatePartialLoader.addPart('situacaoParcela');
                        $translatePartialLoader.addPart('tipoDeclaracaoMensal');
                        $translatePartialLoader.addPart('tipoMovimentoMensal');
                        $translatePartialLoader.addPart('mes');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]

                }
            })
            .state('declaracaoMensalServicoInstituicaoFinanceira.new', {
                parent: 'declaracaoMensalServicoInstituicaoFinanceira',
                url: '/declarar-if/{mes}/{ano}',
                data: {
                    roles: ['ROLE_ENCERRAMENTO_MENSAL_INSTITUICAO_FINANCEIRA'],
                    pageTitle: 'Declaração Mensal de Serviços de Instituições Financeiras'
                },
                ncyBreadcrumb: {
                    label: 'Nova DMS-IF'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/declaracaoMensalServicoInstituicaoFinanceira/declaracaoMensalServicoInstituicaoFinanceira-edit.html',
                        controller: 'DeclaracaoMensalServicoInstituicaoFinanceiraEditController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('declaracaoMensalServico');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'localStorageService', function ($stateParams, localStorageService) {
                        return {
                            mes: $stateParams.mes,
                            exercicio: $stateParams.ano,
                            tipoMovimento: 'INSTITUICAO_FINANCEIRA',
                            prestador: localStorageService.get("prestadorPrincipal").prestador,
                            abertura: new Date(),
                            encerramento: new Date(),
                            lancadoPor: 'CLIENTE',
                            contasBancarias: []
                        };
                    }]
                }
            })
            .state('declaracaoMensalServicoInstituicaoFinanceira.import', {
                parent: 'declaracaoMensalServicoInstituicaoFinanceira',
                url: '/importacao-desif',
                data: {
                    roles: ['ROLE_ENCERRAMENTO_MENSAL_INSTITUICAO_FINANCEIRA'],
                    pageTitle: 'Importação Desif'
                },
                ncyBreadcrumb: {
                    label: 'Importação de Cosif via Arquivo'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/declaracaoMensalServicoInstituicaoFinanceira/declaracaoMensalServicoInstituicaoFinanceira-import.html',
                        controller: 'DeclaracaoMensalServicoInstituicaoFinanceiraImportController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        return $translate.refresh();
                    }]
                }

            })
            .state('declaracaoMensalServicoInstituicaoFinanceira.detail', {
                parent: 'declaracaoMensalServicoInstituicaoFinanceira',
                url: '/declararacao-if/detail/:id',
                data: {
                    roles: ['ROLE_ENCERRAMENTO_MENSAL_INSTITUICAO_FINANCEIRA'],
                    pageTitle: 'Detalhes da Declaração Mensal de Serviço de Instituição Financeira'
                },
                ncyBreadcrumb: {
                    label: 'Declaração Mensal de Serviço de Instituição Financeira'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/declaracaoMensalServicoInstituicaoFinanceira/declaracaoMensalServicoInstituicaoFinanceira-detail.html',
                        controller: 'DeclaracaoMensalServicoInstituicaoFinanceiraDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'DeclaracaoMensalServico', function ($stateParams, DeclaracaoMensalServico) {
                        return DeclaracaoMensalServico.get({id: $stateParams.id},
                            function (sucess) {
                                return sucess;
                            }, function (error) {
                                $state.go('declaracaoMensalServicoInstituicaoFinanceira');
                            }).$promise;
                    }]
                }
            });
    });
})();
