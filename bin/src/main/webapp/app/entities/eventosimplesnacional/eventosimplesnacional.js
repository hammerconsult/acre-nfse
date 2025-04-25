(function () {
    'use strict';

angular.module('nfseApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('eventosSimplesNacional', {
                parent: 'entity',
                url: '/evento-simples-nacional',
                data: {
                    pageTitle: 'Consulta de Eventos do Simples Nacional'
                },
                ncyBreadcrumb: {
                    label: 'Consulta de Eventos do Simples Nacional'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/eventosimplesnacional/eventosimplesnacional.html',
                        controller: 'EventoSimplesNacionalController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
    });
})();