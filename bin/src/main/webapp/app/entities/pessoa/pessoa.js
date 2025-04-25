(function () {
    'use strict';

angular.module('nfseApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('pessoa', {
                parent: 'entity',
                url: '/pessoa',
                data: {
                    roles: ['ROLE_ADMIN'],
                    pageTitle: 'nfseApp.pessoa.home.title'
                },
                ncyBreadcrumb: {
                    label: 'Consulta de Pessoas'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/pessoa/pessoas.html',
                        controller: 'PessoaController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('pessoa');
                        $translatePartialLoader.addPart('global');
                        $translatePartialLoader.addPart('dadosPessoais');
                        return $translate.refresh();
                    }]
                }
            })
            .state('pessoa.detail', {
                parent: 'pessoa',
                url: '/detalhes/{id}',
                data: {
                    roles: ['ROLE_ADMIN'],
                    pageTitle: 'nfseApp.pessoa.home.title'
                },
                ncyBreadcrumb: {
                    label: 'Detalhes da Pessoa'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/pessoa/pessoa-detail.html',
                        controller: 'PessoaDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('pessoa');
                        $translatePartialLoader.addPart('dadosPessoais');
                        $translatePartialLoader.addPart('endereco');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Pessoa', function ($stateParams, Pessoa) {
                        return Pessoa.get({id: $stateParams.id});
                    }]
                }
            })
            .state('pessoa.new', {
                parent: 'pessoa',
                url: '/novo',
                data: {
                    roles: ['ROLE_ADMIN'],
                    pageTitle: 'nfseApp.pessoa.home.title'
                },
                ncyBreadcrumb: {
                    label: 'Nova Pessoa'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/pessoa/pessoa-edit.html',
                        controller: 'PessoaEditController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('pessoa');
                        $translatePartialLoader.addPart('dadosPessoais');
                        $translatePartialLoader.addPart('endereco');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }],
                    entity: ['localStorageService', function (localStorageService) {
                        return {id: null, dadosPessoais: {}};
                    }]
                }
            })
            .state('pessoa.edit', {
                parent: 'pessoa',
                url: '/edicao/{id}',
                data: {
                    roles: ['ROLE_ADMIN'],
                    pageTitle: 'nfseApp.pessoa.home.title'
                },
                ncyBreadcrumb: {
                    label: 'Edição da Pessoa'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/pessoa/pessoa-edit.html',
                        controller: 'PessoaEditController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('pessoa');
                        $translatePartialLoader.addPart('dadosPessoais');
                        $translatePartialLoader.addPart('endereco');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }],
                    entity: ['Pessoa', '$stateParams', function (Pessoa, $stateParams) {
                        return Pessoa.get({id: $stateParams.id});
                    }]
                }
            });
    });
})();