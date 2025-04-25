(function () {
    'use strict';

angular.module('nfseApp').controller('SubstituicaoNotaFiscalDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'SubstituicaoNotaFiscal', 'NotaFiscal',
        function($scope, $stateParams, $modalInstance, entity, SubstituicaoNotaFiscal, NotaFiscal) {

        $scope.substituicaoNotaFiscal = entity;
        $scope.notafiscals = NotaFiscal.query();
        $scope.load = function(id) {
            SubstituicaoNotaFiscal.get({id : id}, function(result) {
                $scope.substituicaoNotaFiscal = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('nfseApp:substituicaoNotaFiscalUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.substituicaoNotaFiscal.id != null) {
                SubstituicaoNotaFiscal.update($scope.substituicaoNotaFiscal, onSaveFinished);
            } else {
                SubstituicaoNotaFiscal.save($scope.substituicaoNotaFiscal, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
})();
