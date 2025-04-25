(function () {
    'use strict';

angular.module('nfseApp').controller('LoteServicoTomadoDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'LoteServicoTomado', 'RPS',
        function($scope, $stateParams, $modalInstance, entity, LoteServicoTomado, RPS) {

        $scope.loteServicoTomado = entity;
        $scope.load = function(id) {
            LoteServicoTomado.get({id : id}, function(result) {
                $scope.loteServicoTomado = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('nfseApp:loteServicoTomadoUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.loteServicoTomado.id != null) {
                LoteServicoTomado.update($scope.loteServicoTomado, onSaveFinished);
            } else {
                LoteServicoTomado.save($scope.loteServicoTomado, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
})();