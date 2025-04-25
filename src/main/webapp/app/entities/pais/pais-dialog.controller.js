(function () {
    'use strict';

angular.module('nfseApp').controller('PaisDialogController',
    ['$scope','$state', '$stateParams',  'entity', 'Pais',"Notificacao",
        function($scope,$state, $stateParams, entity, Pais, Notificacao) {

        $scope.pais = entity;
        $scope.load = function(id) {
            Pais.get({id : id}, function(result) {
                $scope.pais = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('nfseApp:paisUpdate', result);
            $state.go("pais");
            Notificacao.info("Pais Salvo com sucesso.");
        };

        $scope.save = function () {
            if ($scope.pais.id != null) {
                Pais.update($scope.pais, onSaveFinished);
            } else {
                Pais.save($scope.pais, onSaveFinished);
            }
        };

        $scope.clear = function() {
            //$modalInstance.dismiss('cancel');
        };
}]);
})();