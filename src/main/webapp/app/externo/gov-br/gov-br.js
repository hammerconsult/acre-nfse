(function () {
    'use strict';

angular.module('nfseApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('govBr', {
                parent: 'external',
                url: '/gov-br?code',
                ncyBreadcrumb: {
                    label: 'Autenticação gov.br'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/externo/gov-br/gov-br.html',
                        controller: 'GovBrController'
                    }
                }
            });
    });
})();
