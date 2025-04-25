(function () {
    'use strict';

angular.module('nfseApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('servicoDeclarado', {
                parent: 'entity',
                url: '/servicos-declarados',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'Serviços Declarados'
                },
                ncyBreadcrumb: {
                    label: 'Consulta de Serviços Declarados'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/servico-declarado/servico-declarado.html',
                        controller: 'ServicoDeclaradoController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('notaFiscal');
                        $translatePartialLoader.addPart('tipoDocumento');
                        $translatePartialLoader.addPart('declaracaoPrestacaoServico');
                        $translatePartialLoader.addPart('situacaoNota');
                        $translatePartialLoader.addPart('global');
                        $translatePartialLoader.addPart('regimeTributario');
                        $translatePartialLoader.addPart('naturezaOperacao');
                        $translatePartialLoader.addPart('tipoServicoDeclarado');

                        return $translate.refresh();
                    }]
                }
            })
            .state('servicoDeclarado.new', {
                parent: 'servicoDeclarado',
                url: '/novo-servico-declarado',
                data: {
                    roles: ['ROLE_DOCUMENTOS_FISCAIS_DECLARAR_SERVICO'],
                    pageTitle: 'Serviço Declarado'
                },
                ncyBreadcrumb: {
                    label: 'Serviço Declarado'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/servico-declarado/servico-declarado-edit.html',
                        controller: 'ServicoDeclaradoEditController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('declaracaoPrestacaoServico');
                        $translatePartialLoader.addPart('tomador');
                        $translatePartialLoader.addPart('dadosPessoais');
                        $translatePartialLoader.addPart('endereco');
                        $translatePartialLoader.addPart('servico');
                        $translatePartialLoader.addPart('modalidadeNota');
                        $translatePartialLoader.addPart('regimeTributario');
                        $translatePartialLoader.addPart('naturezaOperacao');

                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'ServicoDeclarado', 'localStorageService', '$state', function ($stateParams, ServicoDeclarado, localStorageService, $state) {
                        return {declaracaoPrestacaoServico: {}};
                    }]
                }
            })
            .state('servicoDeclarado.detail', {
                parent: 'servicoDeclarado',
                url: '/detalhes-servico-declarado/{id}',
                data: {
                    roles: ['ROLE_DOCUMENTOS_FISCAIS_DECLARAR_SERVICO'],
                    pageTitle: 'Serviço Declarado'
                },
                ncyBreadcrumb: {
                    label: 'Detalhes do Serviço Declarado'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/servico-declarado/servico-declarado-detail.html',
                        controller: 'ServicoDeclaradoDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('declaracaoPrestacaoServico');
                        $translatePartialLoader.addPart('tomador');
                        $translatePartialLoader.addPart('dadosPessoais');
                        $translatePartialLoader.addPart('endereco');
                        $translatePartialLoader.addPart('servico');
                        $translatePartialLoader.addPart('modalidadeNota');
                        $translatePartialLoader.addPart('regimeTributario');
                        $translatePartialLoader.addPart('naturezaOperacao');
                        $translatePartialLoader.addPart('situacaoParcela');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'ServicoDeclarado', '$state', function ($stateParams, ServicoDeclarado, $state) {
                        return ServicoDeclarado.get({id: $stateParams.id},
                            function (sucess) {
                                return sucess;
                            }, function (error) {
                                $state.go('servicoDeclarado');
                            });
                    }]
                }
            })
            .state('servicoDeclarado.import', {
                parent: 'servicoDeclarado',
                url: '/importacao-servico-declarado',
                data: {
                    roles: ['ROLE_DOCUMENTOS_FISCAIS_DECLARAR_SERVICO'],
                    pageTitle: 'Serviço Declarado'
                },
                ncyBreadcrumb: {
                    label: 'Importação de Serviço Declarado via XML'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/servicoDeclarado/servico-declarado-import.html',
                        controller: 'ServicoDeclaradoImportController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('rPS');
                        return $translate.refresh();
                    }],
                    entity: function () {
                        return {declaracaoPrestacaoServico: {}};
                    }
                }

            })
    });
})();
