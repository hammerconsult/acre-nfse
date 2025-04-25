(function () {
    'use strict';

angular.module('nfseApp')
    .factory('Password', function ($resource) {
        return $resource('api/account/change_password/', {}, {});
    });

angular.module('nfseApp')
    .factory('PasswordResetInit', function ($resource) {
        return $resource('api/account/reset_password/init', {}, {})
    });

angular.module('nfseApp')
    .factory('PasswordResetFinish', function ($resource) {
        return $resource('api/account/reset_password/finish', {}, {})
    });
})();