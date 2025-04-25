// (function () {
//     'use strict';
//
// angular.module('nfseApp')
//     .config(function ($stateProvider) {
//         $stateProvider
//             .state('credenciamento', {
//                 parent: 'entity',
//                 url: '/credenciamento',
//                 data: {
//                     pageTitle: 'Cadastro Inicial do Prestador de Serviços'
//                 },
//                 ncyBreadcrumb: {
//                     label: 'Credenciamento'
//                 },
//                 views: {
//                     'content@': {
//                         templateUrl: 'app/entities/credenciamento/credenciamento.html',
//                         controller: 'CredenciamentoController'
//                     }
//                 },
//                 params: {
//                     criarUsuario: false
//                 },
//                 resolve: {
//                     translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
//                         $translatePartialLoader.addPart('prestadorServicos');
//                         $translatePartialLoader.addPart('dadosPessoais');
//                         $translatePartialLoader.addPart('endereco');
//                         $translatePartialLoader.addPart('global');
//                         return $translate.refresh();
//                     }],
//                     pagingParams: ['$stateParams', function ($stateParams) {
//                         return {
//                             criarUsuario : $stateParams.criarUsuario
//                         }
//                     }]
//                 }
//             })
//             .state('documentacaoCredenciamento', {
//                 parent: 'entity',
//                 url: '/documentos-apresentacao/{id}',
//                 data: {
//                     pageTitle: 'Documentos para o Credencimento'
//                 },
//                 ncyBreadcrumb: {
//                     label: 'Documentação'
//                 },
//                 views: {
//                     'content@': {
//                         templateUrl: 'app/entities/credenciamento/documentos.html',
//                         controller: 'DocumentosController'
//                     },
//                     'menu@': {
//                         templateUrl: 'app/components/menu/navigation.html',
//                         controller: 'NavbarController'
//                     }
//                 },
//                 resolve: {
//                     translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
//                         $translatePartialLoader.addPart('prestadorServicos');
//                         $translatePartialLoader.addPart('dadosPessoais');
//                         $translatePartialLoader.addPart('endereco');
//                         $translatePartialLoader.addPart('global');
//                         return $translate.refresh();
//                     }],
//                     entity: ['$stateParams', 'PrestadorServicos', function ($stateParams, PrestadorServicos) {
//                         return PrestadorServicos.get({id: $stateParams.id}).$promise;
//                     }]
//                 }
//             })
//             .state('imprimeTermoCredenciamento', {
//                 url: '/impressao-termo-credenciamento/{id}',
//                 parent: 'documentacaoCredenciamento',
//                 data: {
//                     pageTitle: 'Documentos para o Credencimento'
//                 },
//                 ncyBreadcrumb: {
//                     label: 'Detalhes do Termo de Credenciamento'
//                 },
//                 onEnter: ['$stateParams', '$state', '$modal', function ($stateParams, $state, $modal) {
//                     $modal.open({
//                         templateUrl: 'app/entities/credenciamento/termo-credenciamento-impressao.html',
//                         controller: 'TermoCredenciamentoImpressaoController',
//                         size: 'lg',
//                         resolve: {
//                             id: function () {
//                                 return $stateParams.id;
//                             }
//                         }
//                     }).result.then(function () {
//                         $state.go('^', null, {reload: true});
//                     }, function () {
//                         $state.go('^');
//                     })
//                 }]
//             })
//     });
// })();