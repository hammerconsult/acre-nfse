(function () {
    'use strict';

    angular.module('nfseApp')
        .config(function ($stateProvider) {
            $stateProvider
                .state('account', {
                    abstract: true,
                    parent: 'out',
                });
        });
})();