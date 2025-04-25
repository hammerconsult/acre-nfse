(function () {
    'use strict';

angular.module('nfseApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('relatorioNotasEmitidas', {
                parent: 'entity',
                url: '/relatorio-notas-emitidas',
                ncyBreadcrumb: {
                    label: 'Relatório de Notas Fiscais Emitidas'
                },
                data: {
                    roles: ['ROLE_EMPRESA_ADM'],
                    pageTitle: 'Relatório de Notas Fiscais Emitidas'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/relatorios/relatorioNotasEmitidas.html',
                        controller: 'RelatorioNotasEmitidasController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('situacaoNota');
                        $translatePartialLoader.addPart('naturezaOperacao');
                        $translatePartialLoader.addPart('tipoRelatorio');
                        return $translate.refresh();
                    }]
                }
            })
            .state('relatorioNotasRecebidas', {
                parent: 'entity',
                url: '/relatorio-notas-recebidas',
                ncyBreadcrumb: {
                    label: 'Relatório de Notas Fiscais Recebidas'
                },
                data: {
                    roles: ['ROLE_EMPRESA_ADM'],
                    pageTitle: 'Relatório de Notas Fiscais Recebidas'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/relatorios/relatorioNotasRecebidas.html',
                        controller: 'RelatorioNotasRecebidasController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('situacaoNota');
                        $translatePartialLoader.addPart('naturezaOperacao');
                        $translatePartialLoader.addPart('tipoRelatorio');
                        return $translate.refresh();
                    }]
                }
            })
    });
})();