(function () {
    'use strict';

angular.module('nfseApp')
    .factory('LoteRPS', function ($resource) {
        return $resource('api/lote-rps/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' },
            'buscarLotesRps': {
                method: 'POST',
                url: 'api/buscar-lotes-rps',
                isArray: true
            },
            'reprocessar': {
                method: 'POST',
                url: 'api/reprocessar-lote-rps'
            },
            'inverterReprocessar': {
                method: 'POST',
                url: 'api/inverter-reprocessar-lote-rps'
            },
            'dispararReprocessamento': {
                method: 'GET',
                url: 'api/disparar-reprocessamento'
            },
            'getXml': {
                method: 'GET',
                url: 'api/lote-rps/get-xml'
            },
            'getLog': {
                method: 'GET',
                url: 'api/lote-rps/get-log'
            }
        });
    });
})();