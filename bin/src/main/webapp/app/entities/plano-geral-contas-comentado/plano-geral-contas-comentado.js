(function () {
    'use strict';

    angular.module('nfseApp')
        .config(function ($stateProvider) {
            $stateProvider
                .state('planoGeralContasComentado', {
                    parent: 'entity',
                    url: '/plano-geral-contas-comentado/consultar',
                    ncyBreadcrumb: {
                        label: 'Consultar Plano Geral de Contas Comentado'
                    },
                    data: {
                        roles: ['ROLE_PLANO_GERAL_CONTAS_COMENTADO_CONSULTAR'],
                        pageTitle: 'Plano Geral de Contas Comentado'
                    },
                    views: {
                        'content@': {
                            templateUrl: 'app/entities/plano-geral-contas-comentado/plano-geral-contas-comentado.html',
                            controller: 'PlanoGeralContasComentadoController'
                        }
                    },
                    resolve: {}
                })
                .state('planoGeralContasComentado.detail', {
                    parent: 'entity',
                    url: '/plano-geral-contas-comentado/detalhe-conta/{id}',
                    ncyBreadcrumb: {
                        label: 'Detalhe da Conta do PGCC'
                    },
                    data: {
                        roles: ['ROLE_PLANO_GERAL_CONTAS_COMENTADO_CONSULTAR'],
                        pageTitle: 'Plano Geral de Contas Comentado'
                    },
                    views: {
                        'content@': {
                            templateUrl: 'app/entities/plano-geral-contas-comentado/plano-geral-contas-comentado-detail.html',
                            controller: 'PlanoGeralContasComentadoDetailController'
                        }
                    },
                    resolve: {
                        entity: ['PlanoGeralContasComentado', '$stateParams', function (PlanoGeralContasComentado, $stateParams) {
                            return PlanoGeralContasComentado.get({id: $stateParams.id}, function (data) {
                                return data;
                            }, function (error) {
                                $state.go('planoGeralContasComentado');
                            }).$promise;
                        }]
                    }
                })
                .state('planoGeralContasComentado.insert', {
                    parent: 'entity',
                    url: '/plano-geral-contas-comentado/inserir-conta',
                    ncyBreadcrumb: {
                        label: 'Inserir Conta do PGCC'
                    },
                    data: {
                        roles: ['ROLE_PLANO_GERAL_CONTAS_COMENTADO_EDITAR'],
                        pageTitle: 'Plano Geral de Contas Comentado'
                    },
                    views: {
                        'content@': {
                            templateUrl: 'app/entities/plano-geral-contas-comentado/plano-geral-contas-comentado-edit.html',
                            controller: 'PlanoGeralContasComentadoEditController'
                        }
                    },
                    resolve: {
                        entity: function () {
                            return {};
                        }
                    }
                })
                .state('planoGeralContasComentado.edit', {
                    parent: 'entity',
                    url: '/plano-geral-contas-comentado/editar-conta/{id}',
                    ncyBreadcrumb: {
                        label: 'Editar Conta do PGCC'
                    },
                    data: {
                        roles: ['ROLE_PLANO_GERAL_CONTAS_COMENTADO_EDITAR'],
                        pageTitle: 'Plano Geral de Contas Comentado'
                    },
                    views: {
                        'content@': {
                            templateUrl: 'app/entities/plano-geral-contas-comentado/plano-geral-contas-comentado-edit.html',
                            controller: 'PlanoGeralContasComentadoEditController'
                        }
                    },
                    resolve: {
                        entity: ['PlanoGeralContasComentado', '$stateParams', function (PlanoGeralContasComentado, $stateParams) {
                            return PlanoGeralContasComentado.get({id: $stateParams.id}, function (data) {
                                return data;
                            }, function () {
                                $state.go('planoGeralContasComentado');
                            }).$promise;
                        }]
                    }
                })
        });
})();