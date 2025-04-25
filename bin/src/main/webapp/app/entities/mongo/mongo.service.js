'use strict';

angular.module('nfseApp')
    .factory('Mongo', function ($resource) {
        return $resource('api/mongo/:id', {}, {
            'getDb': {
                url: 'api/mongo/get-db',
                method: 'GET'
            },
            'dropDb': {
                url: 'api/mongo/drop-db',
                method: 'DELETE'
            },
            'getCollections': {
                url: 'api/mongo/get-collections',
                method: 'GET',
                isArray: true
            },
            'dropCollection': {
                url: 'api/mongo/drop-collection',
                method: 'DELETE'
            }
        });
    });
