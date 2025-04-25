(function () {
    'use strict';

angular.module('nfseApp')
    .factory('GovBr', function ($resource) {
        return $resource('api/externo/gov-br/autenticar/:id', {}, {
            'autenticar': {
                method: 'GET',
                url: 'api/externo/gov-br/autenticar/:code'
            }
        });
    });
})();