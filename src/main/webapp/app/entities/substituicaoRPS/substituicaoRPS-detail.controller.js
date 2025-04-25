(function () {
    'use strict';

angular.module('nfseApp')
    .controller('SubstituicaoRPSDetailController', function ($scope, $rootScope, $stateParams, entity, SubstituicaoRPS, RPS) {
        $scope.substituicaoRPS = entity;
        $scope.load = function (id) {
            SubstituicaoRPS.get({id: id}, function(result) {
                $scope.substituicaoRPS = result;
            });
        };
        $rootScope.$on('nfseApp:substituicaoRPSUpdate', function(event, result) {
            $scope.substituicaoRPS = result;
        });
    });
})();
