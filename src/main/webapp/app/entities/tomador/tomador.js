(function () {
    'use strict';

angular.module('nfseApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('tomador', {
                parent: 'entity',
                url: '/tomador',
                data: {
                    roles: ['ROLE_TOMADORES_CONSULTAR'],
                    pageTitle: 'Tomador de Serviços'
                },
                ncyBreadcrumb: {
                    label: 'Consulta de Tomadores de Serviço '
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/tomador/tomadors.html',
                        controller: 'TomadorController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('tomador');
                        $translatePartialLoader.addPart('global');
                        $translatePartialLoader.addPart('dadosPessoais');
                        return $translate.refresh();
                    }]
                }
            })
            .state('tomador.detail', {
                parent: 'tomador',
                url: '/detalhes/{id}',
                data: {
                    roles: ['ROLE_TOMADORES_CONSULTAR'],
                    pageTitle: 'Tomador de Serviços'
                },
                ncyBreadcrumb: {
                    label: 'Detalhes do Tomador de Serviço'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/tomador/tomador-detail.html',
                        controller: 'TomadorDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('tomador');
                        $translatePartialLoader.addPart('dadosPessoais');
                        $translatePartialLoader.addPart('endereco');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Tomador', function ($stateParams, Tomador) {
                        return Tomador.get({id: $stateParams.id});
                    }]
                }
            })
            .state('tomador.new', {
                parent: 'tomador',
                url: '/novo',
                data: {
                    roles: ['ROLE_TOMADORES_INCLUIR'],
                    pageTitle: 'Tomador de Serviços'
                },
                ncyBreadcrumb: {
                    label: 'Novo Tomador de Serviço'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/tomador/tomador-edit.html',
                        controller: 'TomadorEditController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('tomador');
                        $translatePartialLoader.addPart('dadosPessoais');
                        $translatePartialLoader.addPart('endereco');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }],
                    entity: ['localStorageService', function (localStorageService) {
                        return {id: null, prestador: localStorageService.get("prestadorPrincipal")};
                    }]
                }
            })
            .state('tomador.edit', {
                parent: 'tomador',
                url: '/edicao/{id}',
                data: {
                    roles: ['ROLE_TOMADORES_INCLUIR'],
                    pageTitle: 'Tomador de Serviços'
                },
                ncyBreadcrumb: {
                    label: 'Edição do Tomador de Serviço'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/tomador/tomador-edit.html',
                        controller: 'TomadorEditController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('tomador');
                        $translatePartialLoader.addPart('dadosPessoais');
                        $translatePartialLoader.addPart('endereco');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }],
                    entity: ['Tomador', '$stateParams', function (Tomador, $stateParams) {
                        return Tomador.get({id: $stateParams.id}).$promise;
                    }]
                }
            });
    });
})();
