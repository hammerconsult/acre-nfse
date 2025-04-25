(function () {
    'use strict';

    angular.module('nfseApp')
        .controller('MensagemContribuinteUsuarioDetailController', function ($scope, MensagemContribuinteUsuarioService, entity, $sanitize, Principal) {

            $scope.mensagemContribuinteUsuario = entity;

            $scope.trustAsHtml = function (value) {
                return $sanitize(value);
            };
        });
})();