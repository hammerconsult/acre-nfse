(function () {
    'use strict';
    angular.module('nfseApp')
        .config(function ($stateProvider) {
            $stateProvider
                .state('activate', {
                    parent: 'account',
                    url: '/activate?key',
                    data: {
                        roles: [],
                        pageTitle: 'Ativação do cadastro'
                    },
                    ncyBreadcrumb: {
                        label: 'Registro de usuário'
                    },
                    views: {
                        'content@': {
                            templateUrl: 'app/account/activate/activate.html',
                            controller: 'ActivationController'
                        }
                    },
                    resolve: {
                        translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                            $translatePartialLoader.addPart('activate');
                            return $translate.refresh();
                        }]
                    }
                });
        });
})();