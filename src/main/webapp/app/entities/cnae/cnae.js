(function () {
    'use strict';

angular.module('nfseApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('cnae', {
                parent: 'entity',
                url: '/cnaes',
                data: {
                    roles: ['ROLE_ADMIN'],
                    pageTitle: 'nfseApp.cnae.home.title'
                },
                ncyBreadcrumb: {
                    label: 'Consulta de CNAE'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/cnae/cnaes.html',
                        controller: 'CnaeController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('cnae');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('cnae.detail', {
                parent: 'cnae',
                url: '/detalhes/{id}',
                data: {
                    roles: ['ROLE_ADMIN'],
                    pageTitle: 'nfseApp.cnae.detail.title'
                },
                ncyBreadcrumb: {
                    label: 'Detalhes do CNAE'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/cnae/cnae-detail.html',
                        controller: 'CnaeDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('cnae');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Cnae', function($stateParams, Cnae) {
                        return Cnae.get({id : $stateParams.id});
                    }]
                }
            })
            .state('cnae.new', {
                parent: 'cnae',
                url: '/novo',
                data: {
                    roles: ['ROLE_ADMIN'],
                },
                ncyBreadcrumb: {
                    label: 'Novo CNAE'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/cnae/cnae-dialog.html',
                        controller: 'CnaeDialogController'
                    }
                },

                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('cnae');
                        return $translate.refresh();
                    }],
                    entity: function () {
                        return {codigo: null, descricao: null, ativo: true, id: null};
                    }
                }
            })
            .state('cnae.edit', {
                parent: 'cnae',
                url: '/edicao/{id}',
                data: {
                    roles: ['ROLE_ADMIN'],
                },
                ncyBreadcrumb: {
                    label: 'Edição de CNAE'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/cnae/cnae-dialog.html',
                        controller: 'CnaeDialogController'
                    }
                },

                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('cnae');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Cnae', function($stateParams, Cnae) {
                        return Cnae.get({id : $stateParams.id});
                    }]
                }
            });
    });
})();