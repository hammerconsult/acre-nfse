(function () {
    'use strict';

angular.module('nfseApp')
    .factory('CEP', function ($resource, DateUtils) {
        return $resource('api/cep/:cep', {}, {
            'getByCep': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            }
        });
    });
})();