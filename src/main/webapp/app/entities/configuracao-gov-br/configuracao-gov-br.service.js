(function () {
    'use strict';

    angular.module('nfseApp')
        .factory('ConfiguracaoGovBrService', function ($resource) {
            return $resource('api/externo/configuracao-gov-br/:id', {}, {
                'getConfiguracaoGovBr': {
                    method: 'GET',
                    url: "api/externo/configuracao-gov-br"
                }
            });
        });
})();