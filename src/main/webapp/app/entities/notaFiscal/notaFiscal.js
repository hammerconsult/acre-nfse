(function () {
    'use strict';

    angular.module('nfseApp')
        .config(function ($stateProvider) {
            $stateProvider
                .state('notaFiscal', {
                    parent: 'entity',
                    url: '/nota-fiscal',
                    data: {
                        roles: ['ROLE_RELATORIOS_CONSULTAR_DOCUMENTOS'],
                        pageTitle: 'Nota Fiscal'
                    },
                    ncyBreadcrumb: {
                        label: 'Consulta de Notas Fiscais Emitidas'
                    },
                    views: {
                        'content@': {
                            templateUrl: 'app/entities/notaFiscal/notaFiscals.html',
                            controller: 'NotaFiscalController'
                        }
                    },
                    resolve: {
                        translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                            $translatePartialLoader.addPart('notaFiscal');
                            $translatePartialLoader.addPart('declaracaoPrestacaoServico');
                            $translatePartialLoader.addPart('modalidadeNota');
                            $translatePartialLoader.addPart('situacaoNota');
                            $translatePartialLoader.addPart('regimeTributario');
                            $translatePartialLoader.addPart('tipoIssqn');
                            $translatePartialLoader.addPart('global');
                            $translatePartialLoader.addPart('naturezaOperacao');
                            $translatePartialLoader.addPart('origemEmissao');
                            return $translate.refresh();
                        }]
                    }
                })
                .state('notas-recebidas', {
                    parent: 'entity',
                    url: '/notas-fiscais-recebidas',
                    data: {
                        roles: ['ROLE_USER'],
                        pageTitle: 'Notas Fiscais Recebidas'
                    },
                    ncyBreadcrumb: {
                        label: 'Consulta de Notas Fiscais Recebidas'
                    },
                    views: {
                        'content@': {
                            templateUrl: 'app/entities/notaFiscal/notas-recebidas.html',
                            controller: 'NotasFiscaisRecebidasController'
                        }
                    },
                    resolve: {
                        translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                            $translatePartialLoader.addPart('notaFiscal');
                            $translatePartialLoader.addPart('declaracaoPrestacaoServico');
                            $translatePartialLoader.addPart('modalidadeNota');
                            $translatePartialLoader.addPart('situacaoNota');
                            $translatePartialLoader.addPart('regimeTributario');
                            $translatePartialLoader.addPart('tipoIssqn');
                            $translatePartialLoader.addPart('global');
                            $translatePartialLoader.addPart('naturezaOperacao');
                            return $translate.refresh();
                        }]
                    }
                })
                .state('cancelamento-nota-fiscal', {
                    parent: 'notaFiscal',
                    url: '/cancelamento',
                    ncyBreadcrumb: {
                        label: 'Cancelamentos Nota Fiscal'
                    },
                    views: {
                        'content@': {
                            templateUrl: 'app/entities/notaFiscal/cancelamento/cancelamento.html',
                            controller: 'CancelamentoController'
                        }
                    },
                    resolve: {
                        translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                            $translatePartialLoader.addPart('global');
                            $translatePartialLoader.addPart('situacaoDeferimento');
                            return $translate.refresh();
                        }]
                    }
                })
                .state('notaFiscal.detail', {
                    parent: 'notaFiscal',
                    url: '/detalhes/{id}',
                    data: {
                        roles: ['ROLE_DOCUMENTOS_FISCAIS_EMITIR_NFSE'],
                        pageTitle: 'Nota Fiscal'
                    },
                    ncyBreadcrumb: {
                        label: 'Detalhes da Nota Fiscal'
                    },
                    views: {
                        'content@': {
                            templateUrl: 'app/entities/notaFiscal/notaFiscal-detail.html',
                            controller: 'NotaFiscalDetailController'
                        }
                    },
                    resolve: {
                        translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                            $translatePartialLoader.addPart('notaFiscal');
                            $translatePartialLoader.addPart('declaracaoPrestacaoServico');
                            $translatePartialLoader.addPart('global');
                            $translatePartialLoader.addPart('tipoIssqn');
                            $translatePartialLoader.addPart('situacaoParcela');
                            $translatePartialLoader.addPart('modalidadeNota');
                            $translatePartialLoader.addPart('regimeTributario');
                            $translatePartialLoader.addPart('naturezaOperacao');
                            $translatePartialLoader.addPart('situacaoDeferimento');
                            $translatePartialLoader.addPart('motivoCancelamento');
                            $translatePartialLoader.addPart('statusProcessamento');
                            return $translate.refresh();
                        }],
                        entity: ['$stateParams', 'NotaFiscal', '$state', function ($stateParams, NotaFiscal, $state) {
                            return NotaFiscal.get({id: $stateParams.id},
                                function (sucess) {
                                    return sucess;
                                }, function (error) {
                                    $state.go('notaFiscal');
                                }).$promise;
                        }]
                    }
                })
                .state('imprimeNota', {
                    url: '/impressao/{id}',
                    parent: 'entity',
                    data: {
                        roles: [],
                        pageTitle: 'Nota Fiscal'
                    },
                    ncyBreadcrumb: {
                        label: 'Detalhes da Nota Fiscal'
                    },
                    onEnter: ['$stateParams', '$state', '$modal', 'NotaFiscal', '$rootScope', function ($stateParams, $state, $modal, NotaFiscal, $rootScope) {
                        $modal.open({
                            templateUrl: 'app/entities/notaFiscal/notaFiscal-impressao.html',
                            controller: 'NotaFiscalImpressaoController',
                            size: 'lg',
                            resolve: {
                                id: function () {
                                    return $stateParams.id;
                                }
                            }
                        }).result.then(function (result) {
                            if ($rootScope.previousStateName && $rootScope.previousStateName != 'notaFiscal.edit') {
                                $state.go($rootScope.previousStateName, null, {reload: true});
                            } else {
                                $state.go('notaFiscal', null, {reload: true});
                            }
                        }, function () {
                            if ($rootScope.previousStateName && ($rootScope.previousStateName != 'notaFiscal.edit' && $rootScope.previousStateName != 'notaFiscal.copy')) {
                                $state.go($rootScope.previousStateName);
                            } else {
                                $state.go('notaFiscal');
                            }
                        })
                    }]
                })
                .state('notaFiscal.new', {
                    parent: 'notaFiscal',
                    url: '/novo',
                    data: {
                        roles: ['ROLE_DOCUMENTOS_FISCAIS_EMITIR_NFSE'],
                        pageTitle: 'Nota Fiscal'
                    },
                    ncyBreadcrumb: {
                        label: 'Emissão de nova Nota Fiscal'
                    },
                    views: {
                        'content@': {
                            templateUrl: 'app/entities/notaFiscal/notaFiscal-edit.html',
                            controller: 'NotaFiscalEditController'
                        }
                    },
                    resolve: {
                        translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                            $translatePartialLoader.addPart('notaFiscal');
                            $translatePartialLoader.addPart('declaracaoPrestacaoServico');
                            $translatePartialLoader.addPart('tomador');
                            $translatePartialLoader.addPart('dadosPessoais');
                            $translatePartialLoader.addPart('endereco');
                            $translatePartialLoader.addPart('servico');
                            $translatePartialLoader.addPart('modalidadeNota');
                            $translatePartialLoader.addPart('regimeTributario');
                            $translatePartialLoader.addPart('tipoIssqn');
                            $translatePartialLoader.addPart('naturezaOperacao');
                            return $translate.refresh();
                        }],
                        entity: ['$stateParams', 'NotaFiscal', 'localStorageService', '$state', function ($stateParams, NotaFiscal, localStorageService, $state) {
                            return NotaFiscal.new({}, function (data) {
                                return data;
                            }, function (error) {
                                $state.go('notaFiscal');
                            });
                        }]
                    }
                })
                .state('notaFiscal.newRpsManual', {
                    parent: 'notaFiscal',
                    url: '/novo-rps-manual',
                    data: {
                        roles: ['ROLE_DOCUMENTOS_FISCAIS_EMITIR_NFSE'],
                        pageTitle: 'Nota Fiscal'
                    },
                    ncyBreadcrumb: {
                        label: 'Emissão de nova Nota Fiscal'
                    },
                    views: {
                        'content@': {
                            templateUrl: 'app/entities/notaFiscal/notaFiscal-edit.html',
                            controller: 'NotaFiscalEditController'
                        }
                    },
                    resolve: {
                        translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                            $translatePartialLoader.addPart('notaFiscal');
                            $translatePartialLoader.addPart('declaracaoPrestacaoServico');
                            $translatePartialLoader.addPart('tomador');
                            $translatePartialLoader.addPart('dadosPessoais');
                            $translatePartialLoader.addPart('endereco');
                            $translatePartialLoader.addPart('servico');
                            $translatePartialLoader.addPart('modalidadeNota');
                            $translatePartialLoader.addPart('regimeTributario');
                            $translatePartialLoader.addPart('tipoIssqn');
                            $translatePartialLoader.addPart('naturezaOperacao');
                            return $translate.refresh();
                        }],
                        entity: ['$stateParams', 'NotaFiscal', 'localStorageService', '$state', function ($stateParams, NotaFiscal, localStorageService, $state) {
                            return NotaFiscal.newRpsManual({}, function (data) {
                                return data;
                            }, function (error) {
                                $state.go('notaFiscal');
                            });
                        }]
                    }
                })
                .state('notaFiscal.copy', {
                    parent: 'notaFiscal',
                    url: '/nova-nota-copiada/{id}',
                    data: {
                        roles: ['ROLE_DOCUMENTOS_FISCAIS_EMITIR_NFSE'],
                        pageTitle: 'Nota Fiscal'
                    },
                    ncyBreadcrumb: {
                        label: 'Emissão de nova Nota Fiscal'
                    },
                    views: {
                        'content@': {
                            templateUrl: 'app/entities/notaFiscal/notaFiscal-edit.html',
                            controller: 'NotaFiscalEditController'
                        }
                    },
                    resolve: {
                        translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                            $translatePartialLoader.addPart('notaFiscal');
                            $translatePartialLoader.addPart('declaracaoPrestacaoServico');
                            $translatePartialLoader.addPart('tomador');
                            $translatePartialLoader.addPart('dadosPessoais');
                            $translatePartialLoader.addPart('endereco');
                            $translatePartialLoader.addPart('servico');
                            $translatePartialLoader.addPart('tipoIssqn');
                            $translatePartialLoader.addPart('regimeTributario');
                            $translatePartialLoader.addPart('naturezaOperacao');
                            return $translate.refresh();
                        }],
                        entity: ['$stateParams', 'NotaFiscal', 'localStorageService', '$state', 'SweetAlert', 'DateUtils',
                            function ($stateParams, NotaFiscal, localStorageService, $state, DateUtils) {
                                return NotaFiscal.copy({id: $stateParams.id}, function (data) {
                                    data.notaPai = $stateParams.id;
                                    data.emissao = DateUtils.convertLocaleDateFromServer(data.emissao);
                                    return data;
                                }, function (error) {
                                    $state.go('notaFiscal');
                                });
                            }]
                    }
                })
                .state('notaFiscal.byRps', {
                    parent: 'notaFiscal',
                    url: '/nova-nota-rps/{numeroRps}',
                    data: {
                        roles: ['ROLE_DOCUMENTOS_FISCAIS_EMITIR_NFSE'],
                        pageTitle: 'Nota Fiscal'
                    },
                    ncyBreadcrumb: {
                        label: 'Emissão de nova Nota Fiscal'
                    },
                    views: {
                        'content@': {
                            templateUrl: 'app/entities/notaFiscal/notaFiscal-edit.html',
                            controller: 'NotaFiscalEditController'
                        }
                    },
                    resolve: {
                        translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                            $translatePartialLoader.addPart('notaFiscal');
                            $translatePartialLoader.addPart('declaracaoPrestacaoServico');
                            $translatePartialLoader.addPart('tomador');
                            $translatePartialLoader.addPart('dadosPessoais');
                            $translatePartialLoader.addPart('tipoIssqn');
                            $translatePartialLoader.addPart('endereco');
                            $translatePartialLoader.addPart('servico');
                            $translatePartialLoader.addPart('regimeTributario');
                            $translatePartialLoader.addPart('naturezaOperacao');

                            return $translate.refresh();
                        }],
                        entity: ['$stateParams', 'NotaFiscal', 'localStorageService', '$state', 'SweetAlert', function ($stateParams, NotaFiscal, localStorageService, $state, SweetAlert) {
                            return NotaFiscal.copyPorRps({numeroRps: $stateParams.numeroRps}, function (data) {
                                data.notaPai = $stateParams.id;
                                return data;
                            }, function (error) {
                                $state.go('notaFiscal');
                            });
                        }]
                    }
                })
                .state('notaFiscal.edit', {

                    parent: 'notaFiscal',
                    url: '/edicao/{id}',
                    data: {
                        roles: ['ROLE_DOCUMENTOS_FISCAIS_EMITIR_NFSE'],
                        pageTitle: 'Nota Fiscal'
                    },
                    ncyBreadcrumb: {
                        label: 'Edição da Nota Fiscal'
                    },
                    views: {
                        'content@': {
                            templateUrl: 'app/entities/notaFiscal/notaFiscal-edit.html',
                            controller: 'NotaFiscalEditController'
                        }
                    },
                    resolve: {
                        translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                            $translatePartialLoader.addPart('notaFiscal');
                            $translatePartialLoader.addPart('declaracaoPrestacaoServico');
                            $translatePartialLoader.addPart('dadosPessoais');
                            $translatePartialLoader.addPart('tipoIssqn');
                            $translatePartialLoader.addPart('regimeTributario');
                            $translatePartialLoader.addPart('naturezaOperacao');

                            return $translate.refresh();
                        }],
                        entity: ['$stateParams', 'NotaFiscal', '$state', function ($stateParams, NotaFiscal, $state) {
                            return NotaFiscal.get({id: $stateParams.id},
                                function (sucess) {
                                    return sucess;
                                }, function (error) {
                                    $state.go('notaFiscal');
                                });
                        }]
                    }
                })
                .state('notaFiscal.autenticar', {
                    parent: 'entity',
                    url: '/notas-autenticar',
                    data: {
                        roles: [],
                        pageTitle: 'Verifique a Autenticidade da NFS-e '
                    },
                    ncyBreadcrumb: {
                        label: 'Autenticar Nfse'
                    },
                    views: {
                        'content@': {
                            templateUrl: 'app/entities/notaFiscal/autenticar-nota.html',
                            controller: 'AutenticarNotaController'
                        }
                    },
                    resolve: {
                        translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                            $translatePartialLoader.addPart('notaFiscal');
                            $translatePartialLoader.addPart('global');
                            return $translate.refresh();
                        }],
                        entity: function () {
                            return {numero: null, serie: null, dataEmissao: null, id: null};
                        }
                    }
                })
                .state('notaFiscal.autenticar-qrcode', {
                    parent: 'entity',
                    url: '/notas-autenticar-qr-code/{id}',
                    data: {
                        roles: [],
                        pageTitle: 'Verifique a Autenticidade da NFS-e '
                    },
                    ncyBreadcrumb: {
                        label: 'Autenticar Nfse'
                    },
                    views: {
                        'content@': {
                            templateUrl: 'app/entities/notaFiscal/autenticar-nota.html',
                            controller: 'AutenticarNotaController'
                        }
                    },
                    resolve: {
                        translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                            $translatePartialLoader.addPart('notaFiscal');
                            $translatePartialLoader.addPart('declaracaoPrestacaoServico');
                            $translatePartialLoader.addPart('dadosPessoais');
                            $translatePartialLoader.addPart('regimeTributario');
                            $translatePartialLoader.addPart('tipoIssqn');
                            $translatePartialLoader.addPart('naturezaOperacao');

                            return $translate.refresh();
                        }],
                        entity: ['$stateParams', 'NotaFiscal', '$state', function ($stateParams, NotaFiscal, $state) {
                            return NotaFiscal.getPublica({id: $stateParams.id}).$promise;
                        }]
                    }
                })
                .state('exportarXml', {
                    parent: 'entity',
                    url: '/exportar-xml?tipo',
                    data: {
                        roles: ['ROLE_DOCUMENTOS_FISCAIS_EXPORTAR_XML'],
                        pageTitle: 'Exportar XML de Nota Fiscal'
                    },
                    ncyBreadcrumb: {
                        label: 'Exportar XML de Nota Fiscal'
                    },
                    views: {
                        'content@': {
                            templateUrl: 'app/entities/notaFiscal/exportacaoxml/exportacao-xml.html',
                            controller: 'ExportacaoXmlController'
                        }
                    },
                    params: {
                        tipo: null
                    },
                    resolve: {
                        pagingParams: ['$stateParams', function ($stateParams) {
                            return {
                                tipo: $stateParams.tipo
                            };
                        }],
                        translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                            $translatePartialLoader.addPart('global');
                            return $translate.refresh();
                        }]
                    }
                })
                .state('notaFiscal.testeData', {
                    parent: 'notaFiscal',
                    url: '/teste-data',
                    data: {
                        pageTitle: 'Teste de Data'
                    },
                    ncyBreadcrumb: {
                        label: 'Teste de Data'
                    },
                    views: {
                        'content@': {
                            templateUrl: 'app/entities/teste/data.html',
                            controller: 'DataController'
                        }
                    }
                })
        });
})();
