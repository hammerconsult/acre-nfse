(function () {
    'use strict';

angular.module('nfseApp').controller('AvisoDebitoVencidoController',
    ['$scope', '$modalInstance',
        function($scope, $modalInstance) {
            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        }]);
})();