(function () {
    'use strict';

angular.module('nfseApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('sync-data-base', {
                parent: 'site',
                url: '/sync-data-base',
                data: {
                    roles: ['ROLE_ADMIN'],
                    pageTitle: 'Sincronização Entre os Banco de Dados'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/syncElasticSearch/sync.html',
                        controller: 'SyncController'
                    }
                }
            })
    });
})();
