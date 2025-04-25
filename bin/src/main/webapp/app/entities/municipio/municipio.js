(function () {
    'use strict';

angular.module('nfseApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('municipio', {
                parent: 'entity',
                url: '/municipios',
                data: {
                    roles: ['ROLE_ADMIN'],
                    pageTitle: 'nfseApp.municipio.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/municipio/municipios.html',
                        controller: 'MunicipioController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('municipio');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('municipio.detail', {
                parent: 'entity',
                url: '/municipio/{id}',
                data: {
                    roles: ['ROLE_ADMIN'],
                    pageTitle: 'nfseApp.municipio.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/municipio/municipio-detail.html',
                        controller: 'MunicipioDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('municipio');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Municipio', function($stateParams, Municipio) {
                        return Municipio.get({id : $stateParams.id});
                    }]
                }
            })
            .state('municipio.new', {
                parent: 'municipio',
                url: '/new',
                data: {
                    roles: ['ROLE_ADMIN'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'app/entities/municipio/municipio-dialog.html',
                        controller: 'MunicipioDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {codigo: null, nome: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('municipio', null, { reload: true });
                    }, function() {
                        $state.go('municipio');
                    })
                }]
            })
            .state('municipio.edit', {
                parent: 'municipio',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_ADMIN'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'app/entities/municipio/municipio-dialog.html',
                        controller: 'MunicipioDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Municipio', function(Municipio) {
                                return Municipio.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('municipio', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
})();