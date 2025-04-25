(function () {
    'use strict';

    angular.module('nfseApp')
        .factory('ArquivoDesifRegistro0440', function ($resource) {
            return $resource('/api/arquivo-desif-registro-0440/:id', {}, {
                'consultar': {
                    method: 'POST',
                    url: 'api/arquivo-desif-registro-0440/consultar',
                    isArray: true
                }
            });
        });
})();