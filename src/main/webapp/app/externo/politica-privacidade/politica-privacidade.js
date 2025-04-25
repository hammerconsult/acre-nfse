(function () {
    'use strict';

angular.module('nfseApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('politicaprivacidade', {
                parent: 'out',
                url: '/politica-privacidade',
                ncyBreadcrumb: {
                    label: 'Política de Privacidade'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/externo/politica-privacidade/politica-privacidade.html',
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('register');
                        $translatePartialLoader.addPart('global');

                        return $translate.refresh();
                    }]
                }
            });
    });
})();
