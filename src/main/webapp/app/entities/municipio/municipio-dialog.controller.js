(function () {
    'use strict';

angular.module('nfseApp').controller('MunicipioDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Municipio',
        function($scope, $stateParams, $modalInstance, entity, Municipio) {

        $scope.municipio = entity;
        $scope.load = function(id) {
            Municipio.get({id : id}, function(result) {
                $scope.municipio = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('nfseApp:municipioUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.municipio.id != null) {
                Municipio.update($scope.municipio, onSaveFinished);
            } else {
                Municipio.save($scope.municipio, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
})();