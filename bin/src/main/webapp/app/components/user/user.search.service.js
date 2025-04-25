(function () {
    'use strict';

angular.module('nfseApp')
    .factory('UserSearch', function ($resource) {
        return $resource('api/_search/users/:query', {}, {
            'query': {method: 'GET', isArray: true, url: 'api/_search/users'}
        });
    });
})();