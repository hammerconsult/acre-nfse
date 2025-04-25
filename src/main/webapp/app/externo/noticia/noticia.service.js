(function () {
    'use strict';

    angular.module('nfseApp')
        .factory('Noticia', function ($resource) {
            return $resource('api/externo/noticia/:id', {}, {
                'buscarNoticias': {
                    url: 'api/externo/noticias',
                    method: 'GET',
                    isArray: true
                },
                'buscarUltimasNoticias': {
                    url: 'api/externo/ultimas-noticias',
                    method: 'GET',
                    isArray: true
                }
            });
        });
})();
