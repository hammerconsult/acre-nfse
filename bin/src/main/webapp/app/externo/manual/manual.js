(function () {
    'use strict';

angular.module('nfseApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('manual', {
                parent: 'external',
                url: '/manuais',
                ncyBreadcrumb: {
                    label: 'Manuais'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/externo/manual/manual.html',
                        controller: 'ManualController'
                    }
                },
                resolve: {}
            });
    });
})();
