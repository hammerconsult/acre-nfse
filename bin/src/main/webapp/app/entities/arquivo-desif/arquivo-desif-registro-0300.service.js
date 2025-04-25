(function () {
    'use strict';

    angular.module('nfseApp')
        .factory('ArquivoDesifRegistro0300', function ($resource) {
            return $resource('/api/arquivo-desif-registro-0300/:id', {}, {
                'consultar': {
                    method: 'POST',
                    url: 'api/arquivo-desif-registro-0300/consultar',
                    isArray: true
                }
            });
        });
})();