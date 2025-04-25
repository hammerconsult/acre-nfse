(function () {
    'use strict';

angular.module('nfseApp')
    .factory('FaleConoscoService', function ($resource) {
        return $resource('api/externo/fale-conosco/:id', {}, {
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    return angular.toJson(data);
                }
            },
        });
    });
})();