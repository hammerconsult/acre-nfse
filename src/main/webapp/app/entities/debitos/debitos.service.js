(function () {
    'use strict';

    angular.module('nfseApp')
        .factory('Debitos', function ($resource) {
            return $resource('api/debitos/:id', {}, {
                'buscarUltimoDamParcela': {
                    method: 'GET',
                    url: 'api/debito/buscar-ultimo-dam-parcela',
                    transformResponse: function (data) {
                        data = angular.fromJson(data);
                        return data;
                    }
                },
            });
        });
})();