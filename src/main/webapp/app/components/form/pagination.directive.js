/* globals $ */
(function () {
    'use strict';

angular.module('nfseApp')
    .directive('nfseAppPagination', function() {
        return {
            templateUrl: 'app/components/form/pagination.html'
        };
    });
})();