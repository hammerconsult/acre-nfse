(function () {
    'use strict';

angular.module('nfseApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('servico', {
                parent: 'entity',
                url: '/servicos',
                data: {
                    roles: ['ROLE_ADMIN'],
                    pageTitle: 'Serviços'
                },
                ncyBreadcrumb: {
                    label: 'Consulta de Serviço'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/servico/servicos.html',
                        controller: 'ServicoController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('servico');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('servico.detail', {
                parent: 'servico',
                url: '/detalhes/{id}',
                data: {
                    roles: ['ROLE_ADMIN'],
                    pageTitle: 'nfseApp.servico.detail.title'
                },
                ncyBreadcrumb: {
                    label: 'Detalhes do Serviço'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/servico/servico-detail.html',
                        controller: 'ServicoDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('servico');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Servico', function ($stateParams, Servico) {
                        return Servico.get({id: $stateParams.id});
                    }]
                }
            })
            .state('servico.new', {
                parent: 'servico',
                url: '/novo',
                data: {
                    roles: ['ROLE_ADMIN'],
                },
                ncyBreadcrumb: {
                    label: 'Novo Serviço'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/servico/servico-dialog.html',
                        controller: 'ServicoDialogController'
                    }
                },

                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('servico');
                        return $translate.refresh();
                    }],
                    entity: function () {
                        return {codigo: null, descricao: null, ativo: true, id: null};
                    }
                }
            })
            .state('servico.edit', {
                parent: 'servico',
                url: '/edicao/{id}',
                data: {
                    roles: ['ROLE_ADMIN'],
                },
                ncyBreadcrumb: {
                    label: 'Edição de Serviço'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/servico/servico-dialog.html',
                        controller: 'ServicoDialogController'
                    }
                },

                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('servico');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Servico', function ($stateParams, Servico) {
                        return Servico.get({id: $stateParams.id});
                    }]
                }
            });
    });
})();