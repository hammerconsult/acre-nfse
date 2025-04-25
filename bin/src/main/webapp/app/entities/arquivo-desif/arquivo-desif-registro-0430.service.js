(function () {
    'use strict';

    angular.module('nfseApp')
        .factory('ArquivoDesifRegistro0430', function ($resource) {
            return $resource('/api/arquivo-desif-registro-0430/:id', {}, {
                'consultar': {
                    method: 'POST',
                    url: 'api/arquivo-desif-registro-0430/consultar',
                    isArray: true
                }
            });
        });
})();