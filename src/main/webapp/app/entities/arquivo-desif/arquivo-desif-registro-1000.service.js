(function () {
    'use strict';

    angular.module('nfseApp')
        .factory('ArquivoDesifRegistro1000', function ($resource) {
            return $resource('/api/arquivo-desif-registro-1000/:id', {}, {
                'consultar': {
                    method: 'POST',
                    url: 'api/arquivo-desif-registro-1000/consultar',
                    isArray: true
                }
            });
        });
})();