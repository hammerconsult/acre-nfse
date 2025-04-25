(function () {
    'use strict';

angular.module('nfseApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('prestadorServicos', {
                parent: 'entity',
                url: '/prestadorServicos',
                data: {
                    roles: ['ROLE_ADMIN'],
                    pageTitle: 'nfseApp.prestadorServicos.home.title'
                },
                ncyBreadcrumb: {
                    label: 'Consulta de Prestadores de Serviços'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/prestadorServicos/prestadorServicoss.html',
                        controller: 'PrestadorServicosController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('prestadorServicos');
                        $translatePartialLoader.addPart('global');
                        $translatePartialLoader.addPart('dadosPessoais');
                        return $translate.refresh();
                    }]
                }
            })
            .state('prestadorServicos.detail', {
                parent: 'prestadorServicos',
                url: '/detalhes/{id}',
                data: {
                    roles: ['ROLE_ADMIN'],
                    pageTitle: 'nfseApp.prestadorServicos.home.title'
                },
                ncyBreadcrumb: {
                    label: 'Detalhes do Prestadores de Serviços'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/prestadorServicos/prestadorServicos-detail.html',
                        controller: 'PrestadorServicosDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('prestadorServicos');
                        $translatePartialLoader.addPart('dadosPessoais');
                        $translatePartialLoader.addPart('endereco');
                        $translatePartialLoader.addPart('cnae');
                        $translatePartialLoader.addPart('servico');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'PrestadorServicos', function ($stateParams, PrestadorServicos) {
                        return PrestadorServicos.get({id: $stateParams.id});
                    }]
                }
            })
            .state('prestadorServicos.new', {
                parent: 'prestadorServicos',
                url: '/novo',
                data: {
                    roles: ['ROLE_ADMIN'],
                    pageTitle: 'nfseApp.prestadorServicos.home.title'
                },
                ncyBreadcrumb: {
                    label: 'Novo Prestadores de Serviços'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/prestadorServicos/prestadorServicos-edit.html',
                        controller: 'PrestadorServicosEditController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('prestadorServicos');
                        $translatePartialLoader.addPart('dadosPessoais');
                        $translatePartialLoader.addPart('endereco');
                        $translatePartialLoader.addPart('cnae');
                        $translatePartialLoader.addPart('servico');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }],
                    entity: ['localStorageService', function (localStorageService) {
                        return {id: null, pessoa: {dadosPessoais: {}}, cnaes: [], usuarios: []};
                    }]
                }
            })
            .state('prestadorServicos.edit', {
                parent: 'prestadorServicos',
                url: '/edicao/{id}',
                data: {
                    roles: ['ROLE_ADMIN'],
                    pageTitle: 'nfseApp.prestadorServicos.home.title'
                },
                ncyBreadcrumb: {
                    label: 'Edição do Prestadores de Serviços'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/prestadorServicos/prestadorServicos-edit.html',
                        controller: 'PrestadorServicosEditController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('prestadorServicos');
                        $translatePartialLoader.addPart('dadosPessoais');
                        $translatePartialLoader.addPart('endereco');
                        $translatePartialLoader.addPart('cnae');
                        $translatePartialLoader.addPart('servico');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }],
                    entity: ['PrestadorServicos', '$stateParams', function (PrestadorServicos, $stateParams) {
                        return PrestadorServicos.get({id: $stateParams.id}).$promise;
                    }]
                }
            });
    });
})();