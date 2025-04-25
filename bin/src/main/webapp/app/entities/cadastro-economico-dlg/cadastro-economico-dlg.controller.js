(function () {
    'use strict';

angular.module('nfseApp')
    .controller('CadastroEconomicoDialogController', function ($scope, $modalInstance, cadastrosEconomico) {

        $scope.cadastrosEconomico = cadastrosEconomico;

        $scope.ok = function (cadastroEconomico) {
            $modalInstance.close(cadastroEconomico);
        };
    });
})();