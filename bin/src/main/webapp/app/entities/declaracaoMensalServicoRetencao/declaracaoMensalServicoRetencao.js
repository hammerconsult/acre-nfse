(function () {
    'use strict';

angular.module('nfseApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('declaracaoMensalServicoRetencao', {
                parent: 'entity',
                url: '/dms-retencao',
                data: {
                    roles: ['ROLE_ENCERRAMENTO_MENSAL_SERVICOS_TOMADOS'],
                    pageTitle: 'Declaração Mensal de Serviços Tomados com Retenção'
                },
                ncyBreadcrumb: {
                    label: 'Consulta DMS'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/declaracaoMensalServicoRetencao/declaracaoMensalServicosRetencao.html',
                        controller: 'DeclaracaoMensalServicoRetencaoController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('declaracaoMensalServico');
                        $translatePartialLoader.addPart('situacaoDeclaracaoMensal');
                        $translatePartialLoader.addPart('situacaoParcela');
                        $translatePartialLoader.addPart('tipoDeclaracaoMensal');
                        $translatePartialLoader.addPart('tipoMovimentoMensal');
                        $translatePartialLoader.addPart('naturezaOperacao');
                        $translatePartialLoader.addPart('mes');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('declaracaoMensalServicoRetencao.new', {
                parent: 'declaracaoMensalServicoRetencao',
                url: '/declarar-retencao/{mes}/{ano}',
                data: {
                    roles: ['ROLE_ENCERRAMENTO_MENSAL_SERVICOS_TOMADOS'],
                    pageTitle: 'Declaração Mensal de Serviços Tomados com Retenção'
                },
                ncyBreadcrumb: {
                    label: 'Nova DMS'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/declaracaoMensalServicoRetencao/declaracaoMensalServicoRetencao-edit.html',
                        controller: 'DeclaracaoMensalServicoRetencaoEditController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('declaracaoMensalServico');
                        $translatePartialLoader.addPart('global');
                        $translatePartialLoader.addPart('tipoDocumento');
                        $translatePartialLoader.addPart('naturezaOperacao');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'localStorageService', function ($stateParams, localStorageService) {
                        return {
                            exercicio: $stateParams.ano,
                            mes: $stateParams.mes,
                            prestador: localStorageService.get("prestadorPrincipal").prestador,
                            tipoMovimento: 'RETENCAO',
                            abertura: new Date(),
                            encerramento: new Date(),
                            lancadoPor: 'CLIENTE',
                            notas: [],
                        };
                    }]
                }
            })
            .state('declaracaoMensalServicoRetencao.detail', {
                parent: 'declaracaoMensalServicoRetencao',
                url: '/detalhes/{id}',
                data: {
                    roles: ['ROLE_ENCERRAMENTO_MENSAL_SERVICOS_TOMADOS'],
                    pageTitle: 'Detalhes da Declaração Mensal de Serviços'
                },
                ncyBreadcrumb: {
                    label: 'Detalhes da Declaração Mensal de Serviços Tomados com Retenção'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/declaracaoMensalServicoRetencao/declaracaoMensalServicoRetencao-detail.html',
                        controller: 'DeclaracaoMensalServicoRetencaoDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('global');
                        $translatePartialLoader.addPart('tipoDocumento');
                        $translatePartialLoader.addPart('naturezaOperacao');
                        $translatePartialLoader.addPart('situacaoNota');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'DeclaracaoMensalServico', function ($stateParams, DeclaracaoMensalServico) {
                        return DeclaracaoMensalServico.get({id: $stateParams.id},
                            function (sucess) {
                                return sucess;
                            }, function (error) {
                                console.log(error);
                                $state.go('declaracaoMensalServicoRetencao');
                            }).$promise;
                    }]
                }
            });;
    });
})();
