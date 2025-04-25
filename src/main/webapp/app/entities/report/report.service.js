(function () {
    'use strict';

    angular.module('nfseApp')
        .factory('Report', function ($resource) {
            return $resource('spring/report/:id', {}, {
                'getByUuid': {
                    url: 'spring/report/get-by-uuid',
                    method: 'GET'
                }
            });
        });
})();
