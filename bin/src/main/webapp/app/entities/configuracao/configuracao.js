(function () {
    'use strict';

angular.module('nfseApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('configuracao', {
                parent: 'site',
                url: '/configuracao/',
                data: {
                    roles: ['ROLE_ADMIN'],
                    pageTitle: 'Configurações Municipais'
                },
                ncyBreadcrumb: {
                    label: 'Edição'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/configuracao/configuracao-detail.html',
                        controller: 'ConfiguracaoDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('configuracao');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Configuracao', function ($stateParams, Configuracao) {
                        return Configuracao.get();
                    }]
                }
            })
    });
})();