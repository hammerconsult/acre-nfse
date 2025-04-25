(function () {
    'use strict';

angular.module('nfseApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('perfilEmpresa', {
                parent: 'entity',
                url: '/perfil-empresa',
                data: {
                    roles: ['ROLE_EMPRESA_ADM'],
                    pageTitle: 'Perfil da Empresa'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/perfilEmpresa/perfilEmpresa-detail.html',
                        controller: 'PerfilEmpresaController'
                    }
                },
                ncyBreadcrumb: {
                    label: 'Perfil da Empresa'
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('prestadorServicos');
                        $translatePartialLoader.addPart('global');
                        $translatePartialLoader.addPart('dadosPessoais');
                        $translatePartialLoader.addPart('endereco');
                        $translatePartialLoader.addPart('cnae');
                        $translatePartialLoader.addPart('servico');
                        $translatePartialLoader.addPart('settings');
                        $translatePartialLoader.addPart('versaoDesif');
                        $translatePartialLoader.addPart('regimeEspecialTributacao');
                        return $translate.refresh();
                    }]
                }
            });
    });
})();