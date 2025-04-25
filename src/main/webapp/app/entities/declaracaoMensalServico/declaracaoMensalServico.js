(function () {
    'use strict';

angular.module('nfseApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('declaracaoMensalServico', {
                parent: 'entity',
                url: '/dms',
                data: {
                    roles: ['ROLE_ENCERRAMENTO_MENSAL_SERVICOS_PRESTADOS'],
                    pageTitle: 'Declaração Mensal de Serviços'
                },
                ncyBreadcrumb: {
                    label: 'Consulta DMS'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/declaracaoMensalServico/declaracaoMensalServicos.html',
                        controller: 'DeclaracaoMensalServicoController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('declaracaoMensalServico');
                        $translatePartialLoader.addPart('situacaoDeclaracaoMensal');
                        $translatePartialLoader.addPart('situacaoParcela');
                        $translatePartialLoader.addPart('tipoDeclaracaoMensal');
                        $translatePartialLoader.addPart('tipoMovimentoMensal');
                        $translatePartialLoader.addPart('tipoDocumento');
                        $translatePartialLoader.addPart('naturezaOperacao');
                        $translatePartialLoader.addPart('mes');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('declaracaoMensalServico.new', {
                parent: 'declaracaoMensalServico',
                url: '/declarar/{mes}/{ano}',
                data: {
                    roles: ['ROLE_ENCERRAMENTO_MENSAL_SERVICOS_PRESTADOS'],
                    pageTitle: 'Declaração Mensal de Serviços'
                },
                ncyBreadcrumb: {
                    label: 'Nova DMS'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/declaracaoMensalServico/declaracaoMensalServico-edit.html',
                        controller: 'DeclaracaoMensalServicoEditController'
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
                            tipoMovimento: 'NORMAL',
                            abertura: new Date(),
                            encerramento: new Date(),
                            lancadoPor: 'CLIENTE',
                            notas: [],
                        };
                    }]
                }
            })
            .state('declaracaoMensalServico.detail', {
                parent: 'declaracaoMensalServico',
                url: '/detalhes/{id}',
                data: {
                    roles: ['ROLE_ENCERRAMENTO_MENSAL_SERVICOS_PRESTADOS'],
                    pageTitle: 'Detalhes da Declaração Mensal de Serviços'
                },
                ncyBreadcrumb: {
                    label: 'Detalhes da Declaração Mensal de Serviços'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/declaracaoMensalServico/declaracaoMensalServico-detail.html',
                        controller: 'DeclaracaoMensalServicoDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('declaracaoMensalServico');
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
                                ssss
                                $state.go('declaracaoMensalServico');
                            }).$promise;
                    }]
                }
            });
    });
})();
