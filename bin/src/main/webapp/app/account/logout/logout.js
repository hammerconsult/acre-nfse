(function () {
    'use strict';

    angular.module('nfseApp')
        .config(function ($stateProvider) {
            $stateProvider
                .state('logout', {
                    parent: 'account',
                    url: '/logout',
                    data: {
                        roles: []
                    },
                    views: {
                        'content@': {
                            templateUrl: 'app/main/main.html',
                            controller: 'LogoutController'
                        }
                    }
                });
        });
})();