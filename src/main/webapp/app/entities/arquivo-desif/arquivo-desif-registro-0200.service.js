(function () {
    'use strict';

    angular.module('nfseApp')
        .factory('ArquivoDesifRegistro0200', function ($resource) {
            return $resource('/api/arquivo-desif-registro-0200/:id', {}, {
                'consultar': {
                    method: 'POST',
                    url: 'api/arquivo-desif-registro-0200/consultar',
                    isArray: true
                }
            });
        });
})();