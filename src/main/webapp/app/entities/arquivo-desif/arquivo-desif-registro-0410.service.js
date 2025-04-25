(function () {
    'use strict';

    angular.module('nfseApp')
        .factory('ArquivoDesifRegistro0410', function ($resource) {
            return $resource('/api/arquivo-desif-registro-0410/:id', {}, {
                'consultar': {
                    method: 'POST',
                    url: 'api/arquivo-desif-registro-0410/consultar',
                    isArray: true
                }
            });
        });
})();