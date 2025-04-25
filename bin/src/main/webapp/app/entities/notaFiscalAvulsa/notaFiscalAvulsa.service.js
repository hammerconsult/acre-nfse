(function () {
    'use strict';

    angular.module('nfseApp')
        .factory('NotaFiscalAvulsa', function ($resource) {
            return $resource('api/notaFiscalAvulsa/:id', {}, {
                'search': {
                    url: 'api/notaFiscalAvulsa/search',
                    method: 'POST',
                    isArray: true
                },
                'get': {
                    method: 'GET',
                    transformResponse: function (data) {
                        data = angular.fromJson(data);
                        return data;
                    }
                },
                'save': {
                    method: 'POST',
                    transformRequest: function (data) {
                        return angular.toJson(data);
                    }
                }
            });
        });
})();