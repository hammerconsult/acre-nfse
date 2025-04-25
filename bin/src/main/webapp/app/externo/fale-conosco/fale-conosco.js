(function () {
    'use strict';

angular.module('nfseApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('faleconosco', {
                parent: 'out',
                url: '/fale-conosco',
                ncyBreadcrumb: {
                    label: 'Fale Conosco'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/externo/fale-conosco/fale-conosco.html',
                        controller: 'FaleConoscoController'
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
