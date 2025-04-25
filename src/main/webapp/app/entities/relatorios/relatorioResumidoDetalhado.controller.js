(function () {
    'use strict';

    angular.module('nfseApp')
        .controller('RelatorioResumidoDetalhadoController', function ($scope, $modalInstance) {

            $scope.resumido = function () {
                $modalInstance.close(false);
            };

            $scope.detalhado = function () {
                $modalInstance.close(true);
            };

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        });
})();