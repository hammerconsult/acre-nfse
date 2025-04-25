(function () {
    'use strict';

angular.module('nfseApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('substituicaoNotaFiscal', {
                parent: 'entity',
                url: '/substituicaoNotaFiscals',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'nfseApp.substituicaoNotaFiscal.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/substituicaoNotaFiscal/substituicaoNotaFiscals.html',
                        controller: 'SubstituicaoNotaFiscalController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('substituicaoNotaFiscal');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('substituicaoNotaFiscal.detail', {
                parent: 'entity',
                url: '/substituicaoNotaFiscal/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'nfseApp.substituicaoNotaFiscal.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/substituicaoNotaFiscal/substituicaoNotaFiscal-detail.html',
                        controller: 'SubstituicaoNotaFiscalDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('substituicaoNotaFiscal');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'SubstituicaoNotaFiscal', function($stateParams, SubstituicaoNotaFiscal) {
                        return SubstituicaoNotaFiscal.get({id : $stateParams.id});
                    }]
                }
            })
            .state('substituicaoNotaFiscal.new', {
                parent: 'substituicaoNotaFiscal',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'app/entities/substituicaoNotaFiscal/substituicaoNotaFiscal-dialog.html',
                        controller: 'SubstituicaoNotaFiscalDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('substituicaoNotaFiscal', null, { reload: true });
                    }, function() {
                        $state.go('substituicaoNotaFiscal');
                    })
                }]
            })
            .state('substituicaoNotaFiscal.edit', {
                parent: 'substituicaoNotaFiscal',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'app/entities/substituicaoNotaFiscal/substituicaoNotaFiscal-dialog.html',
                        controller: 'SubstituicaoNotaFiscalDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['SubstituicaoNotaFiscal', function(SubstituicaoNotaFiscal) {
                                return SubstituicaoNotaFiscal.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('substituicaoNotaFiscal', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
})();
