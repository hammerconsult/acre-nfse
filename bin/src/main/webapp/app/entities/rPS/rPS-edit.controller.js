(function () {
    'use strict';

angular.module('nfseApp').controller('RPSEditController',
    ['$scope', '$stateParams', 'entity', 'RPS',
        function ($scope, $stateParams, entity, RPS) {

            $scope.rPS = entity;
            $scope.load = function (id) {
                RPS.get({id: id}, function (result) {
                    $scope.rPS = result;
                });
            };

            var onSaveFinished = function (result) {
                $scope.$emit('nfseApp:rPSUpdate', result);
            };

            $scope.save = function () {
                if ($scope.rPS.id != null) {
                    RPS.update($scope.rPS, onSaveFinished);
                } else {
                    RPS.save($scope.rPS, onSaveFinished);
                }
            };

        }]);
})();