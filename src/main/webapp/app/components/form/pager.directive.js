/* globals $ */
(function () {
    'use strict';

angular.module('nfseApp')
    .directive('nfseAppPager', function() {
        return {
            templateUrl: 'app/components/form/pager.html'
        };
    });
})();