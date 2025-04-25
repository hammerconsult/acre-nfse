(function () {
    'use strict';

    angular.module('nfseApp')
        .controller('LogoutController', function (Auth) {
            Auth.logout();
        });
})();