(function () {
    'use strict';

angular.module('nfseApp')
    .factory('SyncElasticSearch', function ($resource) {
        return $resource('api/sync/elasticsearch', {}, {
            'update': {method: 'PUT'}
        });
    });
})();
