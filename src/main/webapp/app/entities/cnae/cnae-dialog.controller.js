(function () {
    'use strict';

angular.module('nfseApp').controller('CnaeDialogController',
    ['$scope','$state', '$stateParams',  'entity', 'Cnae',"Notificacao",
        function($scope,$state, $stateParams, entity, Cnae, Notificacao) {

        $scope.cnae = entity;
        $scope.load = function(id) {
            Cnae.get({id : id}, function(result) {
                $scope.cnae = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('nfseApp:cnaeUpdate', result);
            $state.go("cnae");
            Notificacao.info("CNAE Salvo com sucesso.");
        };

        $scope.save = function () {
            if ($scope.cnae.id != null) {
                Cnae.update($scope.cnae, onSaveFinished);
            } else {
                Cnae.save($scope.cnae, onSaveFinished);
            }
        };

        $scope.clear = function() {
            //$modalInstance.dismiss('cancel');
        };
}]);
})();