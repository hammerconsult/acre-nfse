(function () {
    'use strict';

angular.module('nfseApp')
    .factory('NaturezaJuridica', function ($resource) {
        return $resource('api/naturezas/', {}, {
            'getFisicas': {url: 'api/naturezas-fisicas', method: 'GET', isArray: true},
            'getJuridicas': {url: 'api/naturezas-juridicas', method: 'GET', isArray: true},
            'update': {method: 'PUT'}
        });
    });
})();