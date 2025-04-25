(function () {
    'use strict';

angular.module('nfseApp').controller('SubstituicaoRPSDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'SubstituicaoRPS', 'RPS',
        function($scope, $stateParams, $modalInstance, entity, SubstituicaoRPS, RPS) {

        $scope.substituicaoRPS = entity;
        $scope.rpss = RPS.query();
        $scope.load = function(id) {
            SubstituicaoRPS.get({id : id}, function(result) {
                $scope.substituicaoRPS = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('nfseApp:substituicaoRPSUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.substituicaoRPS.id != null) {
                SubstituicaoRPS.update($scope.substituicaoRPS, onSaveFinished);
            } else {
                SubstituicaoRPS.save($scope.substituicaoRPS, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
})();
