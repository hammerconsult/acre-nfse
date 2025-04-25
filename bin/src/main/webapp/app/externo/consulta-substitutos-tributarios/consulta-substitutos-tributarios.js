(function () {
    'use strict';

angular.module('nfseApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('substitutostributarios', {
                parent: 'out',
                url: '/consulta-substitutos-tributarios',
                data: {
                  pageTitle: 'Consute os Substitutos Tributários'
                },
                ncyBreadcrumb: {
                    label: 'Consulta de Substitutos Tributários'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/externo/consulta-substitutos-tributarios/consulta-substitutos-tributarios.html',
                        controller: 'SubstitutosTributariosController'
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
