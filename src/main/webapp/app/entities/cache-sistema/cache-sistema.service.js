(function () {
    'use strict';

    angular.module('nfseApp')
        .factory('CacheSistema', function ($resource) {
            return $resource('api/cache-sistema/:id', {}, {
                'buscarCaches': {
                    url: 'api/cache-sistema/list',
                    method: 'POST',
                    isArray: true
                }
            });
        });
})();