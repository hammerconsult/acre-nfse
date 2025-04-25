(function () {
    'use strict';

    angular.module('nfseApp')
        .factory('ArquivoDesifRegistro0100', function ($resource) {
            return $resource('/api/arquivo-desif-registro-0100/:id', {}, {
                'consultar': {
                    method: 'POST',
                    url: 'api/arquivo-desif-registro-0100/consultar',
                    isArray: true
                }
            });
        });
})();