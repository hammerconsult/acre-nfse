(function () {
    'use strict';

angular.module('nfseApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('user-management', {
                parent: 'site',
                url: '/user-management',
                data: {
                    roles: ['ROLE_ADMIN'],
                    pageTitle: 'Lista de Usuários'
                },
                ncyBreadcrumb: {
                    label: 'Usuários'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/admin/user-management/user-management.html',
                        controller: 'UserManagementController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('user-management');
                        return $translate.refresh();
                    }]
                }
            })
            .state('user-management-detail', {
                parent: 'user-management',
                url: '/user-management/:login',
                data: {
                    roles: ['ROLE_ADMIN'],
                    pageTitle: 'Detalhes do Usuário'
                },
                ncyBreadcrumb: {
                    label: 'Detalhes do Usuário'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/admin/user-management/user-management-detail.html',
                        controller: 'UserManagementDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('user-management');
                        return $translate.refresh();
                    }]
                }
            }).state('user-management-edit', {
                parent: 'user-management',
                url: '/edit/user-management/:login',
                data: {
                    roles: ['ROLE_ADMIN'],
                    pageTitle: 'Edição do Usuário'
                },
                ncyBreadcrumb: {
                    label: 'Edição do Usuário'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/admin/user-management/user-management-edit.html',
                        controller: 'UserManagementDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('user-management');
                        return $translate.refresh();
                    }]
                }
            });
    });
})();