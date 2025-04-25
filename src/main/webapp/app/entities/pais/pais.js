(function () {
    'use strict';

angular.module('nfseApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('pais', {
                parent: 'entity',
                url: '/paises',
                data: {
                    roles: ['ROLE_ADMIN'],
                    pageTitle: 'nfseApp.pais.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/pais/paises.html',
                        controller: 'PaisController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('pais');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('pais.detail', {
                parent: 'entity',
                url: '/detalhes/{id}',
                data: {
                    roles: ['ROLE_ADMIN'],
                    pageTitle: 'nfseApp.pais.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/pais/pais-detail.html',
                        controller: 'PaisDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('pais');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Pais', function($stateParams, Pais) {
                        return Pais.get({id : $stateParams.id});
                    }]
                }
            })
            .state('pais.new', {
                parent: 'pais',
                url: '/novo',
                data: {
                    roles: ['ROLE_ADMIN'],
                },
                ncyBreadcrumb: {
                    label: 'Novo País'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/pais/pais-dialog.html',
                        controller: 'PaisDialogController'
                    }
                },

                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('pais');
                        return $translate.refresh();
                    }],
                    entity: function () {
                        return {id: null, codigo: "", nome: ""};
                    }
                }
            })
            .state('pais.edit', {
                parent: 'pais',
                url: '/edicao/{id}',
                data: {
                    roles: ['ROLE_ADMIN'],
                },
                ncyBreadcrumb: {
                    label: 'Edição de Pais'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/pais/pais-dialog.html',
                        controller: 'PaisDialogController'
                    }
                },

                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('pais');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Pais', function($stateParams, Pais) {
                        return Pais.get({id : $stateParams.id});
                    }]
                }
            });
    });
})();