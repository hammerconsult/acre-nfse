(function () {
    'use strict';

    angular.module('nfseApp')
        .config(function ($stateProvider) {
            $stateProvider
                .state('desif', {
                    parent: 'out',
                    url: '/DES-IF',
                    ncyBreadcrumb: {
                        label: 'DES-IF'
                    },
                    views: {
                        'content@': {
                            templateUrl: 'app/externo/desif/desif.html',
                            controller: 'DesifController'
                        }
                    },
                    resolve: {}
                });
        });
})();
