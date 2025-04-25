(function () {
    'use strict';

angular.module('nfseApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('notaFiscalAvulsa', {
                parent: 'entity',
                url: '/nota-fiscal-avulsa',
                data: {
                    roles: ['ROLE_NFS_AVULSA'],
                    pageTitle: 'Nota Fiscal Avulsa'
                },
                ncyBreadcrumb: {
                    label: 'Consulta de Nota Fiscal Avulsa'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/notaFiscalAvulsa/notaFiscalAvulsa.html',
                        controller: 'NotaFiscalAvulsaController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('notaFiscalAvulsa.new', {
                parent: 'entity',
                url: '/nova-nota-fiscal-avulsa',
                data: {
                    roles: ['ROLE_NFS_AVULSA'],
                    pageTitle: 'Nota Fiscal Avulsa'
                },
                ncyBreadcrumb: {
                    label: 'Emissão de Nota Fiscal Avulsa'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/notaFiscalAvulsa/notaFiscalAvulsa-edit.html',
                        controller: 'NotaFiscalAvulsaEditController'
                    }
                },
                resolve: {
                    entity: function () {
                        return {
                            dataNota: new Date(),
                            tomador: {},
                            itens: []
                        };
                    }
                }
            })
            .state('notaFiscalAvulsa.detail', {
                parent: 'entity',
                url: '/visualizacao-nota-fiscal-avulsa/{id}',
                data: {
                    roles: ['ROLE_NFS_AVULSA'],
                    pageTitle: 'Nota Fiscal Avulsa'
                },
                ncyBreadcrumb: {
                    label: 'Visualização de Nota Fiscal Avulsa'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/notaFiscalAvulsa/notaFiscalAvulsa-detail.html',
                        controller: 'NotaFiscalAvulsaDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'NotaFiscalAvulsa', '$state', function ($stateParams, NotaFiscalAvulsa, $state) {
                        return NotaFiscalAvulsa.get({id: $stateParams.id},
                            function (sucess) {
                                return sucess;
                            }, function (error) {
                                $state.go('notaFiscalAvulsa');
                            });
                    }]
                }
            })
    });
})();