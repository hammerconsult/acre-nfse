(function () {
    'use strict';

angular.module('nfseApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('aidfe', {
                parent: 'entity',
                url: '/aidfes',
                data: {
                    roles: ['ROLE_AIDFE_CONSULTAR'],
                    pageTitle: 'AIDF-e'
                },
                ncyBreadcrumb: {
                    label: 'Consulta de AIDF-e '
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/aidfe/aidfes.html',
                        controller: 'AidfeController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('aidfe');
                        $translatePartialLoader.addPart('global');
                        $translatePartialLoader.addPart('dadosPessoais');
                        $translatePartialLoader.addPart('prestadorServicos');
                        return $translate.refresh();
                    }]
                }
            })
            .state('aidfe.detail', {
                parent: 'aidfe',
                url: '/aidfe/{id}',
                data: {
                    roles: ['ROLE_AIDFE_CONSULTAR'],
                    pageTitle: 'AIDF-e'
                },
                ncyBreadcrumb: {
                    label: 'Detalhes da AIDF-e'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/aidfe/aidfe-detail.html',
                        controller: 'AidfeDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('aidfe');
                        $translatePartialLoader.addPart('global');
                        $translatePartialLoader.addPart('dadosPessoais');
                        $translatePartialLoader.addPart('prestadorServicos');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Aidfe', '$state', function ($stateParams, Aidfe, $state) {
                        return Aidfe.get({id: $stateParams.id},
                            function (sucess) {
                                return sucess;
                            },
                            function (error) {
                                $state.go('aidfe');
                            });
                    }]
                }
            })

            .state('aidfe.new', {
                parent: 'aidfe',
                url: '/novo',
                data: {
                    roles: ['ROLE_AIDFE_SOLICITAR'],
                    pageTitle: 'AIDF-e'
                },
                ncyBreadcrumb: {
                    label: 'Solicitar AIDF-e'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/aidfe/aidfe-edit.html',
                        controller: 'AidfeEditController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('aidfe');
                        $translatePartialLoader.addPart('global');
                        $translatePartialLoader.addPart('dadosPessoais');
                        $translatePartialLoader.addPart('prestadorServicos');
                        return $translate.refresh();
                    }],
                    entity: ['localStorageService', function (localStorageService) {
                        return {id: null, prestador: localStorageService.get("prestadorPrincipal")};
                    }]
                }
            })
            .state('aidfe.edit', {
                parent: 'aidfe',
                url: '/edicao/{id}',
                data: {
                    roles: ['ROLE_AIDFE_SOLICITAR'],
                    pageTitle: 'AIDF-e'
                },
                ncyBreadcrumb: {
                    label: 'Edição da AIDF-e'
                },
                views: {
                    'content@': {
                        templateUrl: '/app/entities/aidfe/aidfe-edit.html',
                        controller: 'AidfeEditController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('prestadorServicos');
                        $translatePartialLoader.addPart('dadosPessoais');
                        $translatePartialLoader.addPart('endereco');
                        $translatePartialLoader.addPart('aidfe');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }],
                    entity: ['Aidfe', '$stateParams', '$state', function (Aidfe, $stateParams, $state) {
                        return Aidfe.get({id: $stateParams.id},
                            function (sucess) {
                                return sucess;
                            },
                            function (error) {
                                $state.go('aidfe');
                            });
                    }]
                }
            });
    });
})();