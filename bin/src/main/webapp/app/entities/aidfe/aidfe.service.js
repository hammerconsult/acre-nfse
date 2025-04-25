(function () {
    'use strict';

angular.module('nfseApp')
    .factory('Aidfe', function ($resource) {
        return $resource('api/aidfes/:id', {}, {
            'query': {method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'queryPorSituacao': {
                url: 'api/aidfes/por-situacao',
                method: 'GET', isArray: true
            },

            'buscarAidfeNumeroInicial': {
                method: 'GET',
                url: 'api/aidfes/numero-inicial'
            },
            'buscarTodasAidfesDoPrestador': {
                method: 'GET',
                url: 'api/aidfes/por-prestador',
                isArray: true
            },
            'buscarAidfeWithStatusAguardando': {
                method: 'GET',
                url: 'api/aidfes/aidfe-aguardando',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': {method: 'PUT'},
            'deferir': {
                url: 'api/aidfes/deferir',
                method: 'POST'
            },
            'deferirParcialmente': {
                url: 'api/aidfes/deferir-parcialmente',
                method: 'POST'
            },
            'indeferir': {
                url: 'api/aidfes/indeferir',
                method: 'POST'
            },
            'getSituacoes': {
                url: 'api/aidfes/situacoes',
                method: 'GET',
                isArray: true
            },
            'getAidfeAsADM': {
                method: 'GET',
                url: 'api/aidfes/admin/:id'
            }
        });
    });
})();