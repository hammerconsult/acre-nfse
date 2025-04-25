(function () {
    'use strict';

angular.module('nfseApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('home', {
                parent: 'site',
                url: '/',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'Últimas Atividades'
                },
                ncyBreadcrumb: {
                    label: 'Início'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/main/main.html',
                        controller: 'MainController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('mes');
                        $translatePartialLoader.addPart('global');
                        $translatePartialLoader.addPart('notaFiscal');
                        $translatePartialLoader.addPart('situacaoNota');
                        $translatePartialLoader.addPart('tipoServicoDeclarado');
                        $translatePartialLoader.addPart('modalidadeNota');
                        $translatePartialLoader.addPart('situacaoDeferimento');
                        return $translate.refresh();
                    }]
                }
                });
    });
})();
