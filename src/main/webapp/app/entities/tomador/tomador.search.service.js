(function () {
    'use strict';

angular.module('nfseApp')
    .factory('TomadorSearch', function ($resource) {
        return $resource('api/_search/tomadors/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
})();
