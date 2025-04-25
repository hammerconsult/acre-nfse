(function () {
    'use strict';

angular.module('nfseApp')
    .factory('ConstrucaoCivil', function ($resource, DateUtils) {
        return $resource('api/construcao-civil/:id', {}, {
            'buscarConstrucaoCivilFromPrestador': {
                method: 'GET',
                url: 'api/construcao-civil/from-prestador',
                isArray: true
            },
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    if (data.dataAprovacaoProjeto) {
                        data.dataAprovacaoProjeto = DateUtils.convertLocaleDateFromServer(data.dataAprovacaoProjeto);
                    }
                    if (data.dataInicio) {
                        data.dataInicio = DateUtils.convertLocaleDateFromServer(data.dataInicio);
                    }
                    if (data.dataConclusao) {
                        data.dataConclusao = DateUtils.convertLocaleDateFromServer(data.dataConclusao);
                    }
                    return data;
                }
            },
            'buscarConstrucaoCivilByArt': {
                method: 'GET',
                url: 'api/construcao-civil/by-art'
            }
        });
    });
})();