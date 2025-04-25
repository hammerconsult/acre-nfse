(function () {
    'use strict';

    angular.module('nfseApp')
        .config(function ($stateProvider) {
            $stateProvider
                .state('registrarEmpresa', {
                    parent: 'entity',
                    url: '/registrar-empresa',
                    data: {
                        pageTitle: 'Informe seus dados para o cadastro'
                    },
                    ncyBreadcrumb: {
                        label: 'Registro de empresa'
                    },
                    views: {
                        'content@': {
                            templateUrl: 'app/account/registrar-empresa/registrar-empresa.html',
                            controller: 'RegistrarEmpresaController'
                        }
                    },
                    params: {
                        cpfCnpj: null,
                        criarUsuario: false
                    },
                    resolve: {
                        pagingParams: ['$stateParams', function ($stateParams) {
                            console.log('param..', $stateParams);
                            return {
                                cpfCnpj: $stateParams.cpfCnpj,
                                criarUsuario: $stateParams.criarUsuario
                            };
                        }],
                        translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                            $translatePartialLoader.addPart('register');
                            $translatePartialLoader.addPart('global');
                            $translatePartialLoader.addPart('servico');
                            $translatePartialLoader.addPart('cnae');
                            $translatePartialLoader.addPart('mes');
                            return $translate.refresh();
                        }],
                        convite: function () {
                            return {};
                        }
                    }
                });
        });
})();