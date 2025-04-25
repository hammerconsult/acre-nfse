(function () {
    'use strict';

    angular
        .module('nfseApp')
        .factory('EnumeradoService', EnumeradoService);

    EnumeradoService.$inject = ['$resource'];

    function EnumeradoService($resource) {
        return $resource('api/enumerado/:id', {}, {
            'values': {
                url: 'api/enumerado',
                method: 'POST',
                isArray: true
            }
        });
    }
})();
