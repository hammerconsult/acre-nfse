(function () {
    'use strict';

    angular.module('nfseApp')
        .factory('ArquivoDesif', function ($resource) {
            return $resource('/api/arquivo-desif/:id', {}, {
                'consultar': {
                    method: 'POST',
                    url: 'api/arquivo-desif/consultar',
                    isArray: true
                },
                'validar': {
                    method: 'POST',
                    url: "api/arquivo-desif/validar"
                },
                'enviar': {
                    method: 'GET',
                    url: "api/arquivo-desif/enviar"
                },
                'consultarSituacao': {
                    method: 'GET',
                    url: "api/arquivo-desif/consultar-situacao"
                },
                'importarLegado': {
                    method: 'POST',
                    url: "api/arquivo-desif/importar-legado"
                }
            });
        });
})();