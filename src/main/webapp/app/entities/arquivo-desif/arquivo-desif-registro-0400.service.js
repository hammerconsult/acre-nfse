(function () {
    'use strict';

    angular.module('nfseApp')
        .factory('ArquivoDesifRegistro0400', function ($resource) {
            return $resource('/api/arquivo-desif-registro-0400/:id', {}, {
                'consultar': {
                    method: 'POST',
                    url: 'api/arquivo-desif-registro-0400/consultar',
                    isArray: true
                }
            });
        });
})();