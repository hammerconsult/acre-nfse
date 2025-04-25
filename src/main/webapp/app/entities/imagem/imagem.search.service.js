(function () {
    'use strict';

angular.module('nfseApp')
    .factory('ImagemSearch', function ($resource) {
        return $resource('api/_search/imagem/:query', {}, {
            'query': {method: 'GET', isArray: true}
        });
    });
})();