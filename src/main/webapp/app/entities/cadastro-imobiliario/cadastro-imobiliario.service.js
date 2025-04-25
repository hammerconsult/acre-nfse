(function () {
    'use strict';

angular.module('nfseApp')
    .factory('CadastroImobiliario', function ($resource) {
        return $resource('api/cadastro-imobiliario/:id', {}, {
            'query': {
                method: 'GET',
                url: 'api/cadastro-imobiliario/search',
                isArray: true
            },
            'byInscricao': {
                method: 'GET',
                url: "api/cadastro-imobiliario/by-inscricao"
            }
        });
    });
})();