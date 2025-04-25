(function () {
    'use strict';

angular.module('nfseApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('mensagemContribuinteUsuario', {
                parent: 'home',
                url: 'mensagens',
                ncyBreadcrumb: {
                    label: 'Mensagens'
                },
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'Mensagens'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/mensagem-contribuinte-usuario/mensagem-contribuinte-usuario.html',
                        controller: 'MensagemContribuinteUsuarioController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('mensagemContribuinteUsuarioNew', {
                parent: 'entity',
                url: '/mensagem/nova/{id}',
                ncyBreadcrumb: {
                    label: 'Nova Mensagem'
                },
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'Nova Mensagem',
                    specialClass: "white-bg not-navigation",
                    notBreadCrumb: true
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/mensagem-contribuinte-usuario/mensagem-contribuinte-usuario-new.html',
                        controller: 'MensagemContribuinteUsuarioNewController'
                    },
                    'navbar@': {
                        template: '<div></div>',
                    },
                    'menu@': {
                        template: '<div></div>',
                    },
                    'footer@': {
                        template: '<div></div>',
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'MensagemContribuinteUsuarioService', function ($stateParams, MensagemContribuinteUsuarioService) {
                        return MensagemContribuinteUsuarioService.get({id: $stateParams.id}).$promise;
                    }]
                }
            })
            .state('mensagemContribuinteUsuario.detail', {
                parent: 'home',
                url: 'mensagem/{id}',
                ncyBreadcrumb: {
                    label: 'Mensagem'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/mensagem-contribuinte-usuario/mensagem-contribuinte-usuario-detail.html',
                        controller: 'MensagemContribuinteUsuarioDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'MensagemContribuinteUsuarioService', function ($stateParams, MensagemContribuinteUsuarioService) {
                        return MensagemContribuinteUsuarioService.get({id: $stateParams.id}).$promise;
                    }]
                }
            });
    });
})();
