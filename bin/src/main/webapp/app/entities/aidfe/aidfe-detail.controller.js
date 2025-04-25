(function () {
    'use strict';

angular.module('nfseApp')
    .controller('AidfeDetailController', function ($scope, $rootScope, $stateParams, entity, Tomador) {
        $scope.aidfe = entity;
        $scope.load = function (id) {
            Tomador.get({id: id}, function (result) {
                $scope.aidfe = result;
            });
        };
        $rootScope.$on('nfseApp:aidfeUpdate', function (event, result) {
            $scope.aidfe = result;
        });
    });
})();