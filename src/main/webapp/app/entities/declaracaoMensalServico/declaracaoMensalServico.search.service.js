(function () {
    'use strict';

angular.module('nfseApp')
    .factory('DeclaracaoMensalServicoSearch', function ($resource) {
        return $resource('api/_search/declaracaoMensalServicos/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
})();