(function () {
    'use strict';

    angular.module('nfseApp')
        .config(function ($stateProvider) {
            $stateProvider
                .state('password', {
                    parent: 'entity',
                    url: '/password',
                    data: {
                        roles: ['ROLE_USER'],
                        pageTitle: 'global.menu.account.password'
                    },
                    views: {
                        'content@': {
                            templateUrl: 'app/account/password/password.html',
                            controller: 'PasswordController'
                        }
                    },
                    resolve: {
                        translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                            $translatePartialLoader.addPart('password');
                            return $translate.refresh();
                        }]
                    }
                });
        });
})();