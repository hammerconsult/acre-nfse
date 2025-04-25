(function () {
    'use strict';

angular.module('nfseApp')
    .controller('RPSDetailController', function ($scope, $rootScope, $stateParams, entity, RPS) {
        $scope.rPS = entity;
        $scope.load = function (id) {
            RPS.get({id: id}, function(result) {
                $scope.rPS = result;
            });
        };
        $rootScope.$on('nfseApp:rPSUpdate', function(event, result) {
            $scope.rPS = result;
        });
    });
})();