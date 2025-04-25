(function () {
'use strict';

angular.module('nfseApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('register', {
                parent: 'account',
                url: '/register',
                data: {
                    roles: [],
                    pageTitle: 'Informe seus dados para o cadastro'
                },
                ncyBreadcrumb: {
                    label: 'Registro de usuário'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/account/register/register.html',
                        controller: 'RegisterController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('register');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }],
                    convite: function () {
                        return {};
                    }
                }
            })
            .state('registerWithPrestador', {
                parent: 'account',
                url: '/register/{id}',
                data: {
                    roles: [],
                    pageTitle: 'register.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/account/register/register.html',
                        controller: 'RegisterController'
                    },
                    'navbar@': {
                        templateUrl: 'app/components/navbar/no-navbar.html'
                    },
                    'menu@': {
                        templateUrl: 'app/components/menu/no-navigation.html',
                        controller: 'NavbarController'
                    },
                    /*
                     'footer@': {
                     templateUrl: 'app/components/footer/footer.html',
                     controller: 'NavbarController'
                     }*/
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('register');
                        return $translate.refresh();
                    }],
                    convite: function ($stateParams, ConviteUsuarioService) {
                        return ConviteUsuarioService.get({
                            id: $stateParams.id
                        }).$promise;
                    }
                }
            });
    });
})();