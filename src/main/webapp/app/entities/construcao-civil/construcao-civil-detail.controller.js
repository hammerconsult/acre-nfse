(function () {
    'use strict';

angular.module('nfseApp')
    .controller('ConstrucaoCivilDetailController', function ($modal, $state, $scope, entity) {
        $scope.obra = entity;
    });
})();