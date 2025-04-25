(function () {
    'use strict';

angular.module('nfseApp')
    .factory('SubstitutosTributariosService', function ($resource) {
        return $resource('publico/cadastro-economico/consultar-substitutos-tributarios/:id', {}, {
            'query': {
                method: 'GET',
                isArray: true
            }
        });
    });
})();