(function () {
    'use strict';

angular.module('nfseApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('solicitacaorps', {
                parent: 'entity',
                url: '/solicitacaorps',
                data: {
                    roles: ['ROLE_RPS_CONSULTAR'],
                    pageTitle: 'RPS'
                },
                ncyBreadcrumb: {
                    label: 'Consulta de RPS '
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/solicitacao-rps/solicitacao-rps.html',
                        controller: 'SolicitacaoRPSController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('solicitacaoRps');
                        $translatePartialLoader.addPart('global');
                        $translatePartialLoader.addPart('dadosPessoais');
                        $translatePartialLoader.addPart('prestadorServicos');
                        return $translate.refresh();
                    }]
                }
            })
            .state('solicitacaorps.new', {
                parent: 'solicitacaorps',
                url: '/novo',
                data: {
                    roles: ['ROLE_RPS_CONSULTAR'],
                    pageTitle: 'RPS'
                },
                ncyBreadcrumb: {
                    label: 'Solicitar RPS'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/solicitacao-rps/solicitacao-rps-edit.html',
                        controller: 'SolicitacaoRPSEditarController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('solicitacaoRps');
                        $translatePartialLoader.addPart('global');
                        $translatePartialLoader.addPart('dadosPessoais');
                        $translatePartialLoader.addPart('prestadorServicos');
                        return $translate.refresh();
                    }],
                    entity: ['localStorageService', function (localStorageService) {
                        return {id: null, prestador: localStorageService.get("prestadorPrincipal")};
                    }]
                }
            });
    });
})();
