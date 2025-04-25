(function () {
    'use strict';

angular.module('nfseApp')
    .factory('MensagemContribuinteUsuarioService', function ($resource) {
        return $resource('api/mensagem-contribuinte-usuario/:id', {}, {
            'consultar': {
                url: 'api/mensagem-contribuinte-usuario/consultar',
                method: 'POST',
                isArray: true
            },
            'proximaMensagemNaoLida': {
                url: 'api/mensagem-contribuinte-usuario/proxima-nao-lida',
                method: 'GET'
            },
            'marcarComoLida': {
                url: 'api/mensagem-contribuinte-usuario/marcar-como-lida',
                method: 'POST'
            },
            'countNaoLidaNoPrazo': {
                url: 'api/mensagem-contribuinte-usuario/count-nao-lida-no-prazo',
                method: 'GET'
            }
        });
    });
})();
