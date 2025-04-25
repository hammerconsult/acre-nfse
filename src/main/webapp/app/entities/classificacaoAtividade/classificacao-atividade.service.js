(function () {
    'use strict';

angular.module('nfseApp')
    .factory('ClassificacaoAtividade', function ($resource) {
        return $resource('api/classificacoes/', {}, {
            'query': {method: 'GET', isArray: true},
            'update': {method: 'PUT'}
        });
    });
})();