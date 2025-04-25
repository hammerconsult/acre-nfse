(function () {
    'use strict';

angular.module('nfseApp')
    .factory('PessoaSearch', function ($resource) {
        return $resource('api/_search/pessoas/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
})();