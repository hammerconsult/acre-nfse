(function () {
    'use strict';

angular.module('nfseApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('substituicaoRPS', {
                parent: 'entity',
                url: '/substituicaoRPSs',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'nfseApp.substituicaoRPS.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/substituicaoRPS/substituicaoRPSs.html',
                        controller: 'SubstituicaoRPSController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('substituicaoRPS');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('substituicaoRPS.detail', {
                parent: 'entity',
                url: '/substituicaoRPS/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'nfseApp.substituicaoRPS.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/substituicaoRPS/substituicaoRPS-detail.html',
                        controller: 'SubstituicaoRPSDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('substituicaoRPS');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'SubstituicaoRPS', function($stateParams, SubstituicaoRPS) {
                        return SubstituicaoRPS.get({id : $stateParams.id});
                    }]
                }
            })
            .state('substituicaoRPS.new', {
                parent: 'substituicaoRPS',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'app/entities/substituicaoRPS/substituicaoRPS-dialog.html',
                        controller: 'SubstituicaoRPSDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('substituicaoRPS', null, { reload: true });
                    }, function() {
                        $state.go('substituicaoRPS');
                    })
                }]
            })
            .state('substituicaoRPS.edit', {
                parent: 'substituicaoRPS',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'app/entities/substituicaoRPS/substituicaoRPS-dialog.html',
                        controller: 'SubstituicaoRPSDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['SubstituicaoRPS', function(SubstituicaoRPS) {
                                return SubstituicaoRPS.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('substituicaoRPS', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
})();
