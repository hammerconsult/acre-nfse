(function () {
    'use strict';

angular.module('nfseApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('template', {
                parent: 'entity',
                url: '/templates',
                data: {
                    roles: ['ROLE_ADMIN'],
                    pageTitle: 'nfseApp.template.home.title'
                },
                ncyBreadcrumb: {
                    label: 'Consulta de Template'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/template/templates.html',
                        controller: 'TemplateController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('template');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('template.detail', {
                parent: 'template',
                url: '/detalhes/{id}',
                data: {
                    roles: ['ROLE_ADMIN'],
                    pageTitle: 'nfseApp.template.detail.title'
                },
                ncyBreadcrumb: {
                    label: 'Detalhes do Template'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/template/template-detail.html',
                        controller: 'TemplateDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('template');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Template', function($stateParams, Template) {
                        return Template.get({id : $stateParams.id});
                    }]
                }
            })
            .state('template.new', {
                parent: 'template',
                url: '/novo',
                data: {
                    roles: ['ROLE_ADMIN'],
                },
                ncyBreadcrumb: {
                    label: 'Novo Template'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/template/template-dialog.html',
                        controller: 'TemplateDialogController'
                    }
                },

                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('template');
                        return $translate.refresh();
                    }],
                    entity: function () {
                        return {codigo: null, descricao: null, ativo: true, id: null};
                    }
                }
            })
            .state('template.edit', {
                parent: 'template',
                url: '/edicao/{id}',
                data: {
                    roles: ['ROLE_ADMIN'],
                },
                ncyBreadcrumb: {
                    label: 'Edição de Template'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/template/template-dialog.html',
                        controller: 'TemplateDialogController'
                    }
                },

                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('template');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Template', function($stateParams, Template) {
                        return Template.get({id : $stateParams.id});
                    }]
                }
            });
    });
})();
