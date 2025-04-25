(function () {
    'use strict';

angular.module('nfseApp').controller('LoteRPSDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'LoteRPS', 'RPS',
        function($scope, $stateParams, $modalInstance, entity, LoteRPS, RPS) {

        $scope.loteRPS = entity;
        $scope.load = function(id) {
            LoteRPS.get({id : id}, function(result) {
                $scope.loteRPS = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('nfseApp:loteRPSUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.loteRPS.id != null) {
                LoteRPS.update($scope.loteRPS, onSaveFinished);
            } else {
                LoteRPS.save($scope.loteRPS, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
})();