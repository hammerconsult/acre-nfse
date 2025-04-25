(function () {
    'use strict';

angular.module('nfseApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('pesquisa-cnae', {
                parent: 'out',
                url: '/pequisa-cnae',
                ncyBreadcrumb: {
                    label: 'Pesquisa CNAE'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/externo/pesquisa-cnae/pesquisa-cnae.html',
                        controller: 'PesquisaCnaeController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            });
    });
})();
