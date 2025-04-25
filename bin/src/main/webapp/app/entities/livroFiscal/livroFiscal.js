(function () {
    'use strict';

angular.module('nfseApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('livroFiscal', {
                parent: 'entity',
                url: '/livro-fiscal',
                data: {
                    roles: ['ROLE_CONTA_CORRENTE_LIVRO_FISCAL'],
                    pageTitle: 'Livro Fiscal'
                },
                ncyBreadcrumb: {
                    label: 'Consulta dos Livros Fiscais'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/livroFiscal/livroFiscal.html',
                        controller: 'LivroFiscalController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('tipoMovimentoMensal');
                        $translatePartialLoader.addPart('mes');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('livroFiscal.new', {
                parent: 'livroFiscal',
                url: '/livro-fiscal/gerar',
                data: {
                    roles: ['ROLE_CONTA_CORRENTE_LIVRO_FISCAL'],
                    pageTitle: 'Livro Fiscal'
                },
                ncyBreadcrumb: {
                    label: 'Geração dos Livros Fiscais'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/livroFiscal/livroFiscal-edit.html',
                        controller: 'LivroFiscalEditController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }],
                    entity: ['localStorageService', function (localStorageService) {
                        return {
                            id: null,
                            numero: null,
                            abertura: new Date(),
                            encerramento: new Date(),
                            tipoMovimento: 'NORMAL',
                            prestador: localStorageService.get("prestadorPrincipal").prestador,
                            itens: []
                        };
                    }]
                }
            });
    });
})();