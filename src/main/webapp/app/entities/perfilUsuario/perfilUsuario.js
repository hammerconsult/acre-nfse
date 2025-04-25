(function () {
    'use strict';

angular.module('nfseApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('perfilUsuario', {
                parent: 'entity',
                url: '/perfil-usuario',
                data: {
                    roles: ['ROLE_CONTRIBUINTE'],
                    pageTitle: 'Perfil do Usuário'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/perfilUsuario/perfilUsuario.html',
                        controller: 'SettingsController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('settings');
                        return $translate.refresh();
                    }],
                    entity: ['Pessoa', function(Pessoa) {
                        return Pessoa.getPessoaDoUsuario().$promise;
                    }]
                }
            });
    });
})();