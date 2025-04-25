(function () {
    'use strict';

    angular.module('nfseApp')
        .config(function ($stateProvider) {
            $stateProvider
                .state('login', {
                    parent: 'account',
                    url: '/login',
                    data: {
                        roles: [],
                        pageTitle: 'Bem vindo ao ISS on-line',
                        specialClass: "login-page"
                    },
                    ncyBreadcrumb: {
                        label: 'Autênticação '
                    },
                    views: {
                        'content@': {
                            templateUrl: 'app/account/login/login.html',
                            controller: 'LoginController'
                        },
                        'breadcrumb@': {},
                        'menu@': {},
                        'navbar@': {
                            templateUrl: 'app/components/navbar/no-navbar.html',
                            controller: 'NavbarController'
                        },
                    }
                });
        });
})();