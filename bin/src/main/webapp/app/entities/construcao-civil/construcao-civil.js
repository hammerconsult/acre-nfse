(function () {
    'use strict';

angular.module('nfseApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('construcaoCivil', {
                parent: 'entity',
                url: '/construcao-civil/consultar',
                ncyBreadcrumb: {
                    label: 'Consultar Construção Civil'
                },
                data: {
                    roles: ['ROLE_CONSTRUCAO_CIVIL_CONSULTAR'],
                    pageTitle: 'Construção Civil'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/construcao-civil/construcao-civil.html',
                        controller: 'ConstrucaoCivilController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('construcaoStatus');
                        return $translate.refresh();
                    }]
                }
            })
            .state('construcaoCivil.incluir', {
                parent: 'entity',
                url: '/construcao-civil/incluir',
                ncyBreadcrumb: {
                    label: 'Incluir Construção Civil'
                },
                data: {
                    roles: ['ROLE_CONSTRUCAO_CIVIL_INCLUIR'],
                    pageTitle: 'Construção Civil'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/construcao-civil/construcao-civil-edit.html',
                        controller: 'ConstrucaoCivilEditController'
                    }
                },
                resolve: {
                    entity: [function () {
                        return {
                            status: "NAO_INICIADA",
                            localizacao: {},
                            profissionais: []
                        };
                    }]
                }
            })
            .state('construcaoCivil.editar', {
                parent: 'entity',
                url: '/construcao-civil/editar/{id}',
                ncyBreadcrumb: {
                    label: 'Incluir Construção Civil'
                },
                data: {
                    roles: ['ROLE_CONSTRUCAO_CIVIL_INCLUIR'],
                    pageTitle: 'Construção Civil'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/construcao-civil/construcao-civil-edit.html',
                        controller: 'ConstrucaoCivilEditController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', '$state', 'ConstrucaoCivil', function ($stateParams, $state, ConstrucaoCivil) {
                        return ConstrucaoCivil.get({id: $stateParams.id},
                            function (sucess) {
                                return sucess;
                            }, function (error) {
                                $state.go('construcaoCivil');
                            });
                    }]
                }
            })
            .state('construcaoCivil.visualizar', {
                parent: 'entity',
                url: '/construcao-civil/detalhe/{id}',
                ncyBreadcrumb: {
                    label: 'Incluir Construção Civil'
                },
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'Construção Civil'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/construcao-civil/construcao-civil-detail.html',
                        controller: 'ConstrucaoCivilDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', '$state', 'ConstrucaoCivil', function ($stateParams, $state, ConstrucaoCivil) {
                        return ConstrucaoCivil.get({id: $stateParams.id},
                            function (sucess) {
                                return sucess;
                            }, function (error) {
                                $state.go('construcaoCivil');
                            });
                    }]
                }
            })
    });
})();