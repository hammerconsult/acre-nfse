(function () {
    'use strict';

angular.module('nfseApp')
    .factory('EventoSimplesNacional', function ($resource) {
        return $resource('api/evento-simples-nacional/:id', {}, {
            'buscarEventosPorEmpresa': {
                url: 'api/evento-simples-nacional/por-empresa',
                method: 'GET',
                isArray: true
            }
        });
    });
})();