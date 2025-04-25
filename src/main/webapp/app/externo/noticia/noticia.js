(function () {
    'use strict';

    angular.module('nfseApp')
        .config(function ($stateProvider) {
            $stateProvider
                .state('noticias', {
                    parent: 'out',
                    url: '/noticias',
                    data: {
                        roles: [],
                        pageTitle: 'Notícias'
                    },
                    ncyBreadcrumb: {
                        label: 'Notícias'
                    },
                    views: {
                        'content@': {
                            templateUrl: 'app/externo/noticia/noticias.html',
                            controller: 'NoticiasController'
                        }
                    },
                    resolve: {}
                })
                .state('noticias.detail', {
                    parent: 'out',
                    url: '/noticia/detalhe/{id}',
                    data: {
                        roles: [],
                        pageTitle: 'Visualização da Notícia'
                    },
                    ncyBreadcrumb: {
                        label: 'Detalhes da Notícia'
                    },
                    views: {
                        'content@': {
                            templateUrl: 'app/externo/noticia/noticia-detail.html',
                            controller: 'NoticiaDetailController'
                        }
                    },
                    resolve: {
                        translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                            $translatePartialLoader.addPart('global');
                            return $translate.refresh();
                        }],
                        entity: ['$stateParams', 'Noticia', '$state', function ($stateParams, Noticia, $state) {
                            return Noticia.get({id: $stateParams.id},
                                function (sucess) {
                                    return sucess;
                                }, function () {
                                    $state.go('noticias');
                                }).$promise;
                        }]
                    }
                });
        });
})();
