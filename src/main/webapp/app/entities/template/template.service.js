(function () {
    'use strict';

angular.module('nfseApp')
    .factory('Template', function ($resource, DateUtils) {
        return $resource('api/templates/:id', {}, {
            'query': {method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': {method: 'PUT'}
        });
    });
})();
