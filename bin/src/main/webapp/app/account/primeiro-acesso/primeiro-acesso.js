(function () {
    'use strict';

    angular.module('nfseApp')
        .config(function ($stateProvider) {
            $stateProvider
                .state('primeiroAcesso', {
                    parent: 'entity',
                    url: '/primeiro-acesso',
                    data: {
                        roles: ['ROLE_USER'],
                        pageTitle: 'Primeiro Acesso'
                    },
                    views: {
                        'content@': {
                            templateUrl: 'app/account/primeiro-acesso/primeiro-acesso.html',
                            controller: 'PrimeiroAcessoController'
                        }
                    },
                    resolve: {
                    }
                });
        });
})();