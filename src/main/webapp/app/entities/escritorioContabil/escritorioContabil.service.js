(function () {
    'use strict';

    angular.module('nfseApp')
        .factory('EscritorioContabil', function ($resource) {
            return $resource('api/escritorio-contabil/:id', {}, {
                'consultar': {
                    url: 'api/escritorio-contabil/consultar',
                    method: 'POST',
                    isArray: true
                }
            });
        });
})();