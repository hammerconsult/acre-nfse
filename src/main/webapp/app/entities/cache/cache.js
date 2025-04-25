(function () {
    'use strict';

    angular.module('nfseApp')
        .config(function ($stateProvider) {
            $stateProvider
                .state('cache', {
                    parent: 'entity',
                    url: '/cache',
                    data: {
                        roles: ['ROLE_ADMIN'],
                        pageTitle: 'Cache do Sistema'
                    },
                    ncyBreadcrumb: {
                        label: 'Cache do Sistema'
                    },
                    views: {
                        'content@': {
                            templateUrl: 'app/entities/cache/cache.html',
                            controller: 'CacheController'
                        }
                    },
                    resolve: {
                        translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                            $translatePartialLoader.addPart('global');
                            return $translate.refresh();
                        }]
                    }
                })
        });
})();
