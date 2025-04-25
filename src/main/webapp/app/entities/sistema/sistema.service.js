(function () {
    'use strict';

    angular.module('nfseApp')
        .factory('Sistema', function ($resource) {

            var resource = $resource('', {}, {
                'getVersaoSistema': {
                    url: '/api/sistema/versao',
                    responseType: 'text'
                }
            });

            return resource;
        })
    ;
})();